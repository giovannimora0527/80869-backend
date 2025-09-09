package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Usuario;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.model.UsuarioRq;
import com.uniminuto.clinica.repository.UsuarioRepository;
import com.uniminuto.clinica.service.UsuarioSevice;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author lmora
 */
@Service
public class UsuarioServiceImpl implements UsuarioSevice {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> encontrarTodosLosUsuarios() {
        return this.usuarioRepository.findAll();
    }

    @Override
    public List<Usuario> buscarPorRol(String rol) {
        return this.usuarioRepository.findByRol(rol);
    }

    @Override
    public Usuario buscarPorNombre(String username) throws BadRequestException {
        Optional<Usuario> optUser = this.usuarioRepository
                .findByUsername(username);
        if (!optUser.isPresent()) {
            throw new BadRequestException("No se encontro el usuario");
        }
        return optUser.get();
    }

    @Override
    public List<Usuario> encontrarPorActivo(boolean activo) {
        return this.usuarioRepository.findByActivo(activo);
    }

    @Override
    public RespuestaRs guardarUsuario(UsuarioRq usuarioNuevo)
            throws BadRequestException {
        // Paso 1. Llegan todos los campos completos
        this.validarCamposCrearUsuario(usuarioNuevo);
        
        // Paso 2. Consulto si el usuario existe por username
        Optional<Usuario> optUser = this.usuarioRepository
                .findByUsername(usuarioNuevo.getUsername()
                        .toLowerCase());
        if (optUser.isPresent()) {
             // paso 3. Si existe lanzo excepcion.
            throw new BadRequestException("El usuario ya existe.");
        }
       
        // Paso 4. Si no existe creo el usuario y lo guardo.
        this.guardarUsuarioNuevo(usuarioNuevo);
        
        // Paso 5. Devolver una respuesta.
        RespuestaRs respuesta = new RespuestaRs();
        respuesta.setMensaje("Se ha guardado el usuario correctamente.");
        respuesta.setStatus(200);
        return respuesta;
    }
    
    private void guardarUsuarioNuevo(UsuarioRq usuarioNuevo) {
        Usuario nuevo = new Usuario();
        nuevo.setActivo(true);
        nuevo.setFechaCreacion(LocalDateTime.now());
        nuevo.setPassword(this.encriptarPassword(usuarioNuevo.getPass()));
        nuevo.setRol(usuarioNuevo.getRol().toUpperCase());
        nuevo.setUsername(usuarioNuevo.getUsername().toLowerCase());        
        this.usuarioRepository.save(nuevo);
    }

    private void validarCamposCrearUsuario(UsuarioRq usuarioNuevo)
            throws BadRequestException {
        if (usuarioNuevo.getUsername() == null
                || usuarioNuevo.getUsername().isBlank()
                || usuarioNuevo.getUsername().isEmpty()) {
            throw new BadRequestException("El campo username es obligatorio");
        }
        if (usuarioNuevo.getPass() == null
                || usuarioNuevo.getPass().isBlank()
                || usuarioNuevo.getPass().isEmpty()) {
            throw new BadRequestException("El campo password es obligatorio");
        }
        if (usuarioNuevo.getRol() == null
                || usuarioNuevo.getRol().isBlank()
                || usuarioNuevo.getRol().isEmpty()) {
            throw new BadRequestException("El campo rol es obligatorio");
        }
    }
    
    private String encriptarPassword(String passAConvertir) {
        String algoritmo = "MD5";
         try {
            MessageDigest md = MessageDigest.getInstance(algoritmo); // Ej: "SHA-256", "MD5"
            byte[] hashBytes = md.digest(passAConvertir.getBytes());

            // Convertir a hexadecimal
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo no soportado: " + algoritmo, e);
        }
    }

}

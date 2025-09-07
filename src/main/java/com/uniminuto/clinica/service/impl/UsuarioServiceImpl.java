package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Usuario;
import com.uniminuto.clinica.repository.UsuarioRepository;
import com.uniminuto.clinica.service.UsuarioSevice;
import java.util.List;
import java.util.Optional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Optional<Usuario> optUser = this.usuarioRepository.findByUsername(username);
        if (!optUser.isPresent()) {
            throw new BadRequestException("No se encontró el usuario");
        }
        return optUser.get();
    }

    @Override
    public List<Usuario> encontrarPorActivo(boolean activo) {
        return this.usuarioRepository.findByActivo(activo);
    }

    // Método para buscar un usuario por número de documento
    public Usuario buscarPorNumeroDocumento(String numeroDocumento) throws BadRequestException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNumerodocumento(numeroDocumento);
        if (usuarioOpt.isPresent()) {
            return usuarioOpt.get();
        } else {
            throw new BadRequestException("No se encontró el usuario con el número de documento: " + numeroDocumento);
        }
    }
}

package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Usuario;
import com.uniminuto.clinica.model.AutenticatorRs;
import com.uniminuto.clinica.model.AuthenticatorRq;
import com.uniminuto.clinica.repository.UsuarioRepository;
import com.uniminuto.clinica.service.AutenticarService;
import com.uniminuto.clinica.service.CifrarService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import com.uniminuto.clinica.entity.Session;
import com.uniminuto.clinica.repository.SessionRepository;
import com.uniminuto.clinica.security.JwtUtil;

import javax.transaction.Transactional;

@Service
public class AutenticarServiceImpl implements AutenticarService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CifrarService cifrarService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private com.uniminuto.clinica.service.AuditoriaLoginService auditoriaLoginService;

    @org.springframework.beans.factory.annotation.Value("${seguridad.bloqueo.minutos:5}")
    private int minutosBloqueo;

    @Override
    @Transactional
    public AutenticatorRs autenticar(AuthenticatorRq request)
            throws BadRequestException {

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(request.getUsername());
        if (usuarioOpt.isEmpty()) {
            // registrar intento en auditoría (usuario no encontrado)
            com.uniminuto.clinica.entity.AuditoriaLogin al = new com.uniminuto.clinica.entity.AuditoriaLogin(
                    request.getUsername(), LocalDateTime.now(), null, false, "Usuario no encontrado");
            try { auditoriaLoginService.registrarIntento(al); } catch(Exception ex) {}
            throw new BadRequestException("Usuario o contraseña incorrectos");
        }
        Usuario usuario = usuarioOpt.get();
        // verificar bloqueo
        if (usuario.getFechaBloqueo() != null && usuario.getFechaBloqueo().isAfter(LocalDateTime.now())) {
            // registrar en auditoría intento bloqueado
            com.uniminuto.clinica.entity.AuditoriaLogin al = new com.uniminuto.clinica.entity.AuditoriaLogin(
                    usuario.getUsername(), LocalDateTime.now(), null, false, "Usuario bloqueado hasta " + usuario.getFechaBloqueo());
            try { auditoriaLoginService.registrarIntento(al); } catch(Exception ex) {}
            throw new BadRequestException("Usuario bloqueado temporalmente. Intente después de: " + usuario.getFechaBloqueo());
        }
        boolean passwordOk;
        if (passwordEncoder != null) {
            passwordOk = passwordEncoder.matches(request.getPassword(), usuario.getPassword());
        } else {
            passwordOk = usuario.getPassword().equals(this.cifrarService.encriptarPassword(request.getPassword()));
        }
        if (!passwordOk) {
            // incrementar contador de intentos
            Integer intentos = usuario.getIntentosFallidos() == null ? 0 : usuario.getIntentosFallidos();
            intentos = intentos + 1;
            usuario.setIntentosFallidos(intentos);
            String motivo = "Credenciales incorrectas";
            if (intentos >= 3) {
                usuario.setFechaBloqueo(LocalDateTime.now().plusMinutes(minutosBloqueo));
                motivo = "Usuario bloqueado por 3 intentos fallidos";
            }
            usuarioRepository.save(usuario);

            // registrar intento en auditoría
            com.uniminuto.clinica.entity.AuditoriaLogin al = new com.uniminuto.clinica.entity.AuditoriaLogin(
                    usuario.getUsername(), LocalDateTime.now(), null, false, motivo);
            try { auditoriaLoginService.registrarIntento(al); } catch(Exception ex) {}

            throw new BadRequestException(motivo);
        }
        // Login exitoso: resetear intentos y bloqueo
        usuario.setIntentosFallidos(0);
        usuario.setFechaBloqueo(null);
        usuarioRepository.save(usuario);

        // registrar intento exitoso en auditoría
        com.uniminuto.clinica.entity.AuditoriaLogin alOk = new com.uniminuto.clinica.entity.AuditoriaLogin(
            usuario.getUsername(), LocalDateTime.now(), null, true, "Login exitoso");
        try { auditoriaLoginService.registrarIntento(alOk); } catch(Exception ex) {}

        // Generar y devolver un JWT
        AutenticatorRs rta = new AutenticatorRs();
        String token = jwtUtil.generateToken(usuario);
        rta.setToken(token);

        // Creamos la sesión del usuario autenticado
        crearSesionUsuario(usuario, token);
        return rta;
    }

    /**
     * Crea y almacena la sesión del usuario autenticado.
     *
     * @param usuario Usuario autenticado
     * @param token   Token JWT generado
     */
    private void crearSesionUsuario(Usuario usuario, String token) {
        // Elimina cualquier sesión previa del usuario
        sessionRepository.deleteByUserId(usuario.getId().intValue());
        Session session = new Session();
        session.setUserId(usuario.getId().intValue());
        session.setToken(token);
        session.setFechaIniSesion(LocalDateTime.now());
        Date fechaExpiracion = jwtUtil.getExpirationDateFromToken(token);
        session.setFechaExpiracion(fechaExpiracion.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime());
        sessionRepository.save(session);
    }


}

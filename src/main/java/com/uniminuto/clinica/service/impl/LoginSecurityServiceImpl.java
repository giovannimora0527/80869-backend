package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.IntentoFallidoLogin;
import com.uniminuto.clinica.entity.Usuario;
import com.uniminuto.clinica.entity.UsuarioBloqueado;
import com.uniminuto.clinica.model.IntentoFallidoRq;
import com.uniminuto.clinica.model.UsuarioBloqueadoRq;
import com.uniminuto.clinica.repository.IntentoFallidoLoginRepository;
import com.uniminuto.clinica.repository.UsuarioBloqueadoRepository;
import com.uniminuto.clinica.repository.UsuarioRepository;
import com.uniminuto.clinica.service.LoginSecurityService;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LoginSecurityServiceImpl implements LoginSecurityService {

    private static final Logger logger = LoggerFactory.getLogger(LoginSecurityServiceImpl.class);

    @Autowired
    private IntentoFallidoLoginRepository intentoFallidoRepository;

    @Autowired
    private UsuarioBloqueadoRepository usuarioBloqueadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${security.login.max-attempts:3}")
    private int maxIntentosFallidos;

    @Value("${security.login.lockout-duration-minutes:5}")
    private int duracionBloqueoMinutos;

    @Value("${security.login.attempt-window-minutes:15}")
    private int ventanaIntentosMinutos;

    @Transactional
    @Override
    public void registrarIntentoFallido(IntentoFallidoRq intentoRq) throws BadRequestException {

        Optional<Usuario> usrOpt = usuarioRepository.findById(intentoRq.getUsuarioId());
        if(!usrOpt.isPresent()){
            throw new BadRequestException("Usuario que intenta ingresar no encontrado");
        }
        Usuario usuario = usrOpt.get();
        logger.warn("Intento fallido de login para usuario: {} desde IP: {}",
                usuario.getUsername(), intentoRq.getIp());

        // Registrar el intento fallido
        IntentoFallidoLogin intento = new IntentoFallidoLogin();
        intento.setUsuario(usuario);
        intento.setDireccionIp(intentoRq.getIp());
        intento.setDetalles("Credenciales inválidas.");
        intentoFallidoRepository.save(intento);

        // Verificar si se debe bloquear al usuario
        LocalDateTime ventanaInicio = LocalDateTime.now().minusMinutes(ventanaIntentosMinutos);
        Long intentosRecientes = intentoFallidoRepository.countRecentAttemptsByUsuarioId(
                usuario.getId(),
                ventanaInicio
        );

        logger.info("Usuario {} tiene {} intentos fallidos en los últimos {} minutos",
                usuario.getUsername(), intentosRecientes, ventanaIntentosMinutos);

        if (intentosRecientes >= maxIntentosFallidos) {
            UsuarioBloqueadoRq bloqueadoRq = new UsuarioBloqueadoRq();
            bloqueadoRq.setUsuarioId(intentoRq.getUsuarioId());
            bloqueadoRq.setMotivo("Credenciales Invalidas demasiandos intentos");
            bloqueadoRq.setMinutosBloqueo(5);
            bloqueadoRq.setIntentosFallidos(intentosRecientes.intValue());
            bloquearUsuario(bloqueadoRq);
        }
    }

    @Override
    public boolean estaUsuarioBloqueado(Long usuarioId) {
        Optional<UsuarioBloqueado> bloqueoOpt = usuarioBloqueadoRepository.findByUsuarioIdAndActivoTrue(usuarioId);

        if (bloqueoOpt.isEmpty()) {
            return false;
        }

        UsuarioBloqueado bloqueo = bloqueoOpt.get();

        // Verificar si el bloqueo sigue vigente
        if (bloqueo.getActivo()) {
            logger.info("Usuario ID {} está bloqueado hasta: {}", usuarioId, bloqueo.getFechaDesbloqueo());
            return true;
        } else {
            // El bloqueo expiró, desactivarlo
            desbloquearUsuario(bloqueo);
            return false;
        }
    }


    @Override
    public long getTiempoRestanteBloqueo(Long usuarioId) {
        Optional<UsuarioBloqueado> bloqueoOpt = usuarioBloqueadoRepository.findByUsuarioIdAndActivoTrue(usuarioId);

        if (bloqueoOpt.isEmpty()) {
            return 0;
        }

        UsuarioBloqueado bloqueo = bloqueoOpt.get();

        if (!bloqueo.getActivo()) {
            return 0;
        }

        return ChronoUnit.MINUTES.between(LocalDateTime.now(), bloqueo.getFechaDesbloqueo());
    }


    @Override
    @Transactional
    public void limpiarIntentosFallidos(Long usuarioId) {
        logger.info("Limpiando intentos fallidos para usuario ID: {}", usuarioId);
        intentoFallidoRepository.deleteByUsuarioId(usuarioId);
    }


    @Override
    public List<IntentoFallidoLogin> obtenerHistorialIntentos(Long usuarioId, int dias) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
        return intentoFallidoRepository.findByUsuarioIdAndFechaIntentoAfter(usuarioId, fechaLimite);
    }

    @Transactional
    @Override
    public void bloquearUsuarioManualmente(UsuarioBloqueadoRq bloqueadoRq) throws BadRequestException {

        Optional<Usuario>usrOpt = usuarioRepository.findById(bloqueadoRq.getUsuarioId());
        if(!usrOpt.isPresent()){
            throw new BadRequestException("Usuario que intenta bloquear, no existe");
        }
        Usuario usuario = usrOpt.get();

        logger.warn("Bloqueando usuario manualmente: {} por {} minutos. Motivo: {}",
                usuario.getUsername(), bloqueadoRq.getMinutosBloqueo(), bloqueadoRq.getMotivo());



        // Desactivar bloqueos previos
        Optional<UsuarioBloqueado> bloqueoExistente =
                usuarioBloqueadoRepository.findByUsuarioIdAndActivoTrue(usuario.getId());
        bloqueoExistente.ifPresent(this::desbloquearUsuario);

        // Crear nuevo bloqueo
        LocalDateTime fechaDesbloqueo = LocalDateTime.now().plusMinutes(bloqueadoRq.getMinutosBloqueo());
        UsuarioBloqueado nuevoBloqueo = new UsuarioBloqueado();
        nuevoBloqueo.setUsuario(usuario);
        nuevoBloqueo.setFechaDesbloqueo(fechaDesbloqueo);
        nuevoBloqueo.setMotivo(bloqueadoRq.getMotivo());
        usuarioBloqueadoRepository.save(nuevoBloqueo);
    }

    @Override
    @Transactional
    public void desbloquearUsuarioManualmente(Long usuarioId) {
        logger.info("Desbloqueando usuario manualmente ID: {}", usuarioId);
        Optional<UsuarioBloqueado> bloqueoOpt = usuarioBloqueadoRepository.findByUsuarioIdAndActivoTrue(usuarioId);
        bloqueoOpt.ifPresent(this::desbloquearUsuario);
        limpiarIntentosFallidos(usuarioId);
    }

    /**
     * Bloquea un usuario por intentos fallidos consecutivos
     */
    @Transactional
    private void bloquearUsuario(UsuarioBloqueadoRq bloqueadoRq) throws BadRequestException {

        Optional<Usuario>usrOpt = usuarioRepository.findById(bloqueadoRq.getUsuarioId());
        if(!usrOpt.isPresent()){
            throw new BadRequestException("Usuario que intenta bloquear, no existe");
        }
        Usuario usuario = usrOpt.get();
        logger.warn("Bloqueando usuario {} por {} intentos fallidos consecutivos",
                usuario.getUsername(), bloqueadoRq.getIntentosFallidos());

        // Verificar si ya está bloqueado
        Optional<UsuarioBloqueado> bloqueoExistente =
                usuarioBloqueadoRepository.findByUsuarioIdAndActivoTrue(usuario.getId());
        if (bloqueoExistente.isPresent()) {
            logger.info("Usuario {} ya está bloqueado", usuario.getUsername());
            return;
        }

        // Crear nuevo bloqueo
        LocalDateTime fechaDesbloqueo = LocalDateTime.now().plusMinutes(duracionBloqueoMinutos);
        UsuarioBloqueado nuevoBloqueo = new UsuarioBloqueado();
        nuevoBloqueo.setUsuario(usuario);
        nuevoBloqueo.setFechaDesbloqueo(fechaDesbloqueo);
        nuevoBloqueo.setMotivo(bloqueadoRq.getMotivo());
        nuevoBloqueo.setIntentosFallidos(bloqueadoRq.getIntentosFallidos());
        usuarioBloqueadoRepository.save(nuevoBloqueo);

        logger.info("Usuario {} (ID: {}) bloqueado hasta: {}",
                usuario.getUsername(), usuario.getId(), fechaDesbloqueo);
    }

    /**
     * Desbloquea un usuario
     */
    @Transactional
    private void desbloquearUsuario(UsuarioBloqueado bloqueo) {
        logger.info("Desbloqueando usuario: {} (ID: {})",
                bloqueo.getUsuario().getUsername(), bloqueo.getUsuario().getId());
        bloqueo.setActivo(false);
        usuarioBloqueadoRepository.save(bloqueo);
    }
}
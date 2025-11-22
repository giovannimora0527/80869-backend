package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.CodigoVerificacion;
import com.uniminuto.clinica.entity.Usuario;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.repository.CodigoVerificacionRepository;
import com.uniminuto.clinica.repository.UsuarioRepository;
import com.uniminuto.clinica.service.AuditoriaRecuperacionService;
import com.uniminuto.clinica.service.CifrarService;
import com.uniminuto.clinica.service.CodigoVerificacionService;
import com.uniminuto.clinica.service.EmailService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CodigoVerificacionServiceImpl implements CodigoVerificacionService {

    @Autowired
    private CodigoVerificacionRepository codigoVerificacionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CifrarService cifrarService;
    @Autowired
    private AuditoriaRecuperacionService auditoriaRecuperacionService;

    @Override
    @Transactional
    public RespuestaRs solicitarCodigo(String username) throws BadRequestException, MessagingException {
        RespuestaRs rta = new RespuestaRs();
        Optional<Usuario> optUser = usuarioRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            auditoriaRecuperacionService.registrarIntento(username, "Solicitud de código fallida: usuario no existe");
            rta.setStatus(200);
            rta.setMensaje("Si el usuario existe, se enviará un código de verificación a su correo");
            return rta;
        }

        Usuario usuario = optUser.get();
        List<CodigoVerificacion> codigosAnteriores = codigoVerificacionRepository.findByUsernameAndUsadoFalse(username);
        for (CodigoVerificacion cv : codigosAnteriores) {
            cv.setUsado(true);
            codigoVerificacionRepository.save(cv);
        }

        String codigo = generarCodigo();
        CodigoVerificacion nuevoCodigo = new CodigoVerificacion();
        nuevoCodigo.setUsername(username);
        nuevoCodigo.setCodigo(codigo);
        nuevoCodigo.setFechaCreacion(LocalDateTime.now());
        nuevoCodigo.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));
        nuevoCodigo.setUsado(false);
        nuevoCodigo.setIntentos(0);
        codigoVerificacionRepository.save(nuevoCodigo);

        String html = String.format("""
                <html>
                <body>
                    <h2>Recuperación de contraseña</h2>
                    <p>Hola <b>%s</b>,</p>
                    <p>Has solicitado recuperar tu contraseña.</p>
                    <p>Tu código de verificación es:</p>
                    <h1 style="color: #4CAF50; font-size: 48px; letter-spacing: 5px;">%s</h1>
                    <p><b>Este código es válido por 15 minutos.</b></p>
                    <p>Si no solicitaste este cambio, ignora este correo.</p>
                    <hr>
                    <small>Este mensaje fue generado automáticamente.</small>
                </body>
                </html>
                """, usuario.getUsername(), codigo);

        emailService.sendHtmlEmail(usuario.getEmail(), "Código de recuperación de contraseña", html, emailService.getTo());
        auditoriaRecuperacionService.registrarIntento(username, "Código de verificación enviado exitosamente");
        rta.setStatus(200);
        rta.setMensaje("Si el usuario existe, se enviará un código de verificación a su correo");
        return rta;
    }

    @Override
    @Transactional
    public RespuestaRs verificarCodigo(String username, String codigo, String nuevaPassword) throws BadRequestException {
        RespuestaRs rta = new RespuestaRs();
        Optional<CodigoVerificacion> optCodigo = codigoVerificacionRepository
            .findByUsernameAndCodigoAndUsadoFalseAndFechaExpiracionAfter(username, codigo, LocalDateTime.now());

        if (optCodigo.isEmpty()) {
            auditoriaRecuperacionService.registrarIntento(username, "Verificación fallida: código inválido o expirado");
            throw new BadRequestException("Código inválido o expirado");
        }

        CodigoVerificacion codigoVerif = optCodigo.get();
        if (codigoVerif.getIntentos() >= 3) {
            codigoVerif.setUsado(true);
            codigoVerificacionRepository.save(codigoVerif);
            auditoriaRecuperacionService.registrarIntento(username, "Verificación fallida: excedió intentos");
            throw new BadRequestException("Código bloqueado por exceso de intentos");
        }

        codigoVerif.setIntentos(codigoVerif.getIntentos() + 1);
        codigoVerificacionRepository.save(codigoVerif);

        Optional<Usuario> optUser = usuarioRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            throw new BadRequestException("Usuario no encontrado");
        }

        Usuario usuario = optUser.get();
        usuario.setPassword(cifrarService.encriptarPassword(nuevaPassword));
        usuarioRepository.save(usuario);

        codigoVerif.setUsado(true);
        codigoVerificacionRepository.save(codigoVerif);

        auditoriaRecuperacionService.registrarIntento(username, "Contraseña actualizada exitosamente");
        rta.setStatus(200);
        rta.setMensaje("Contraseña actualizada exitosamente");
        return rta;
    }

    @Override
    @Transactional
    public void limpiarCodigosExpirados() {
        codigoVerificacionRepository.deleteByFechaExpiracionBefore(LocalDateTime.now());
    }

    private String generarCodigo() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }
}

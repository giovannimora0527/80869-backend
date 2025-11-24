package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Usuario;
import com.uniminuto.clinica.model.*;
import com.uniminuto.clinica.repository.UsuarioRepository;
import com.uniminuto.clinica.service.AutenticarService;
import com.uniminuto.clinica.service.CifrarService;
import com.uniminuto.clinica.service.EmailService;
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

import javax.mail.MessagingException;
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
    private EmailService emailService;

    @Override
    @Transactional
    public AutenticatorRs autenticar(AuthenticatorRq request)
            throws BadRequestException {

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(request.getUsername());
        if (usuarioOpt.isEmpty()) {
            throw new BadRequestException("Usuario o contraseña incorrectos");
        }
        Usuario usuario = usuarioOpt.get();
        boolean passwordOk;
        if (passwordEncoder != null) {
            passwordOk = passwordEncoder.matches(request.getPassword(), usuario.getPassword());
        } else {
            passwordOk = usuario.getPassword().equals(this.cifrarService.encriptarPassword(request.getPassword()));
        }
        if (!passwordOk) {
            throw new BadRequestException("Usuario o contraseña incorrectos");
        }
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

    /**
     * Inicia el proceso de recuperación de contraseña generando un token
     *
     * @param request Objeto con el email del usuario
     * @return Respuesta con mensaje de confirmación
     * @throws BadRequestException Si hay un error en el proceso
     */
    @Transactional
    @Override
    public RespuestaRs iniciarRecuperacion(RecuperacionInicialRq request)
            throws BadRequestException, MessagingException {

        // Buscar usuario por email
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());

        if (usuarioOpt.isEmpty()) {
            // Por seguridad, no revelamos si el email existe o no
            RespuestaRs response = new RespuestaRs();
            response.setMensaje("Si el correo existe, recibirás un enlace de recuperación");
            response.setStatus(200);
            return response;
        }

        Usuario usuario = usuarioOpt.get();

        // Eliminar tokens previos del usuario
        sessionRepository.deleteByUserId(Math.toIntExact(usuario.getId()));

        // Generar nuevo token
        String token = jwtUtil.generateToken(usuario);
        crearSesionUsuario(usuario, token);


        emailService.enviarCorreo(usuario.getEmail(), "Recuperación de contraseña", construirEmailRecuperacion(token), "cristian.gonzalez-du@uniminuto.edu.co" );

        RespuestaRs response = new RespuestaRs();
        response.setMensaje("Si el correo existe, recibirás un enlace de recuperación");
        response.setStatus(200);
        return response;
    }

    /**
     * Restablece la contraseña del usuario usando el token de recuperación
     *
     * @param request Objeto con el token y la nueva contraseña
     * @return Respuesta indicando el éxito de la operación
     * @throws BadRequestException Si el token es inválido o hay errores de validación
     */
    @Transactional
    @Override
    public RespuestaRs restablecerPassword(RecuperacionRq request)
            throws BadRequestException {

        // Buscar token
        Optional<Session> resetTokenOpt =
                sessionRepository.findByToken(request.getToken());

        if (resetTokenOpt.isEmpty()) {
            throw new BadRequestException("Token inválido o expirado");
        }

        Session resetToken = resetTokenOpt.get();




        // Validar complejidad de contraseña
        validarComplejidadPassword(request.getContrasena());

        Optional<Usuario> usrOpt = usuarioRepository.findById(Long.valueOf(resetToken.getUserId()));

        if(usrOpt.isEmpty()){
            throw new BadRequestException("Usuario consultado no encontrado");
        }
        // Actualizar contraseña
        Usuario usuario = usrOpt.get();
        String passwordEncriptada;

        if (passwordEncoder != null) {
            passwordEncriptada = passwordEncoder.encode(request.getContrasena());
        } else {
            passwordEncriptada = cifrarService.encriptarPassword(request.getContrasena());
        }

        usuario.setPassword(passwordEncriptada);
        usuarioRepository.save(usuario);

        // Cerrar todas las sesiones activas del usuario por seguridad
        sessionRepository.deleteByUserId(usuario.getId().intValue());

        RespuestaRs response = new RespuestaRs();
        response.setMensaje("Contraseña restablecida exitosamente");
        response.setStatus(200);

        return response;
    }

    /**
     * Valida que la contraseña cumpla con los requisitos de complejidad
     *
     * @param password Contraseña a validar
     * @throws BadRequestException Si la contraseña no cumple los requisitos
     */
    private void validarComplejidadPassword(String password) throws BadRequestException {
        if (password == null || password.length() < 8) {
            throw new BadRequestException("La contraseña debe tener al menos 8 caracteres");
        }

        // Validaciones adicionales
        boolean tieneMayuscula = password.chars().anyMatch(Character::isUpperCase);
        boolean tieneMinuscula = password.chars().anyMatch(Character::isLowerCase);
        boolean tieneNumero = password.chars().anyMatch(Character::isDigit);

        if (!tieneMayuscula || !tieneMinuscula || !tieneNumero) {
            throw new BadRequestException(
                    "La contraseña debe contener al menos una mayúscula, una minúscula y un número");
        }
    }

    /**
     * Construye el contenido HTML del email de recuperación
     */
    private String construirEmailRecuperacion(String token) {
        String urlRecuperacion = "https://localhost:4200/restablecer-password?token=" + token;

        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .container { background-color: #f4f4f4; padding: 20px; border-radius: 10px; }" +
                "        .header { background-color: #007bff; color: white; padding: 20px; text-align: center; border-radius: 10px 10px 0 0; }" +
                "        .content { background-color: white; padding: 30px; border-radius: 0 0 10px 10px; }" +
                "        .button { display: inline-block; padding: 12px 30px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                "        .button:hover { background-color: #0056b3; }" +
                "        .footer { text-align: center; margin-top: 20px; font-size: 12px; color: #666; }" +
                "        .warning { background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 10px; margin: 20px 0; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "            <h1> Clinica </h1>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <h2>Recuperación de Contraseña</h2>" +
                "            <p>Hola,</p>" +
                "            <p>Hemos recibido una solicitud para restablecer la contraseña de tu cuenta.</p>" +
                "            <p>Para continuar con el proceso, haz clic en el siguiente botón:</p>" +
                "            <div style='text-align: center;'>" +
                "                <a href='" + urlRecuperacion + "' class='button'>Restablecer Contraseña</a>" +
                "            </div>" +
                "            <p>O copia y pega el siguiente enlace en tu navegador:</p>" +
                "            <p style='word-break: break-all; background-color: #f8f9fa; padding: 10px; border-radius: 5px;'>" +
                "                <a href='" + urlRecuperacion + "'>" + urlRecuperacion + "</a>" +
                "            </p>" +
                "            <div class='warning'>" +
                "                <strong>⚠️ Importante:</strong>" +
                "                <ul>" +
                "                    <li>Este enlace es válido por 24 horas</li>" +
                "                    <li>Solo puede ser usado una vez</li>" +
                "                    <li>Si no solicitaste este cambio, ignora este correo</li>" +
                "                </ul>" +
                "            </div>" +
                "            <p>Si tienes alguna pregunta o problema, no dudes en contactarnos.</p>" +
                "            <p>Saludos,<br><strong>Equipo de Clinica </strong></p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>Este es un correo automático, por favor no respondas a este mensaje.</p>" +
                "            <p>&copy; 2024 Clinica. Todos los derechos reservados.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}

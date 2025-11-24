package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.AuditoriaLogin;
import com.uniminuto.clinica.entity.AuditoriaRecuperacion;
import com.uniminuto.clinica.entity.Session;
import com.uniminuto.clinica.entity.Usuario;
import com.uniminuto.clinica.model.AutenticatorRs;
import com.uniminuto.clinica.model.AuthenticatorRq;
import com.uniminuto.clinica.model.RecuperarContrasenaRq;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.repository.AuditoriaLoginRepository;
import com.uniminuto.clinica.repository.AuditoriaRecuperacionRepository;
import com.uniminuto.clinica.repository.SessionRepository;
import com.uniminuto.clinica.repository.UsuarioRepository;
import com.uniminuto.clinica.security.JwtUtil;
import com.uniminuto.clinica.service.AutenticarService;
import com.uniminuto.clinica.service.CifrarService;
import com.uniminuto.clinica.service.EmailService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

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

    @Autowired
    private AuditoriaRecuperacionRepository auditoriaRecuperacionRepository;

    @Autowired
    private AuditoriaLoginRepository auditoriaLoginRepository;

    // Parámetros de seguridad (configurables en application.properties)
    @Value("${seguridad.login.max-intentos:3}")
    private int maxIntentosLogin;

    @Value("${seguridad.login.bloqueo-minutos:5}")
    private int minutosBloqueo;

    // =======================
    //   LOGIN CON BLOQUEO
    // =======================
    @Override
    @Transactional
    public AutenticatorRs autenticar(AuthenticatorRq request) throws BadRequestException {

        String username = request.getUsername();
        String ip = obtenerIpCliente();

        // Mensaje específico cuando el usuario queda bloqueado
        String mensajeBloqueoUsuario =
                "Su usuario ha sido bloqueado por cantidad de intentos erróneos. " +
                        "Vuelva a intentar en " + minutosBloqueo + " minutos.";

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            // Usuario no existe: registramos auditoría y devolvemos mensaje genérico
            guardarAuditoriaLogin(username, ip,
                    "Intento de inicio de sesión: usuario no encontrado.");
            throw new BadRequestException("Usuario o contraseña incorrectos");
        }

        Usuario usuario = usuarioOpt.get();

        // 1. Verificar si el usuario está bloqueado por intentos fallidos previos
        if (usuario.getFechaBloqueo() != null
                && LocalDateTime.now().isBefore(usuario.getFechaBloqueo())) {

            guardarAuditoriaLogin(username, ip,
                    "Intento de inicio de sesión con cuenta bloqueada hasta "
                            + usuario.getFechaBloqueo() + ".");

            // Ahora sí devolvemos mensaje específico de bloqueo
            throw new BadRequestException(mensajeBloqueoUsuario);
        }

        // 2. Validar la contraseña
        boolean passwordOk;
        if (passwordEncoder != null) {
            passwordOk = passwordEncoder.matches(request.getPassword(), usuario.getPassword());
        } else {
            passwordOk = usuario.getPassword()
                    .equals(this.cifrarService.encriptarPassword(request.getPassword()));
        }

        // 3. Contraseña incorrecta: sumar intento, bloquear si supera el máximo y auditar
        if (!passwordOk) {
            int intentos = usuario.getIntentosFallidos() == null
                    ? 0
                    : usuario.getIntentosFallidos();
            intentos++;
            usuario.setIntentosFallidos(intentos);

            String descripcion = "Contraseña incorrecta. Intento "
                    + intentos + " de " + maxIntentosLogin + ".";

            // Mensaje por defecto (cuando aún no se ha bloqueado)
            String mensajeError = "Usuario o contraseña incorrectos";

            if (intentos >= maxIntentosLogin) {
                LocalDateTime bloqueoHasta = LocalDateTime.now().plusMinutes(minutosBloqueo);
                usuario.setFechaBloqueo(bloqueoHasta);
                descripcion = descripcion + " Usuario bloqueado hasta " + bloqueoHasta + ".";
                // Cuando se bloquea, usamos mensaje de bloqueo
                mensajeError = mensajeBloqueoUsuario;
            }

            usuarioRepository.save(usuario);
            guardarAuditoriaLogin(username, ip, descripcion);

            throw new BadRequestException(mensajeError);
        }

        // 4. Contraseña correcta: reiniciar contador de intentos y limpiar bloqueo
        usuario.setIntentosFallidos(0);
        usuario.setFechaBloqueo(null);
        usuarioRepository.save(usuario);

        guardarAuditoriaLogin(username, ip, "Inicio de sesión exitoso.");

        // 5. Generar y devolver JWT
        AutenticatorRs rta = new AutenticatorRs();
        String token = jwtUtil.generateToken(usuario);
        rta.setToken(token);

        // Crear la sesión del usuario autenticado
        crearSesionUsuario(usuario, token);
        return rta;
    }

    // ==================================
    //   RECUPERACIÓN DE CONTRASEÑA
    // ==================================
    @Override
    @Transactional
    public RespuestaRs recuperarContrasena(RecuperarContrasenaRq request) {
        String username = request.getUsername();
        String descripcion;

        // Buscar usuario por username
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            // Usuario inválido -> solo auditoría y mensaje genérico
            descripcion = "Intento de recuperación de contraseña: usuario no encontrado.";
            guardarAuditoriaRecuperacion(username, descripcion);

            RespuestaRs rs = new RespuestaRs();
            rs.setMensaje("Si el usuario existe, se enviará un correo con instrucciones.");
            rs.setStatus(200);
            return rs;
        }

        Usuario usuario = usuarioOpt.get();

        // Generar contraseña temporal
        String passwordTemporalPlano = generarPasswordTemporal();
        String passwordHash = cifrarService.encriptarPassword(passwordTemporalPlano);
        usuario.setPassword(passwordHash);
        usuarioRepository.save(usuario);

        try {
            String body = "Hola " + usuario.getUsername() + ",\n\n"
                    + "Se ha generado una contraseña temporal para tu cuenta:\n\n"
                    + passwordTemporalPlano + "\n\n"
                    + "Por favor, inicia sesión con esta contraseña temporal y cámbiala "
                    + "tan pronto como sea posible.\n\n"
                    + "Atentamente,\n"
                    + "Clínica";

            emailService.enviarCorreoSimple(
                    usuario.getEmail(),
                    "Recuperación de contraseña",
                    body
            );

            descripcion = "Contraseña temporal generada y enviado correo de recuperación.";
        } catch (BadRequestException e) {
            descripcion = "Error al enviar la contraseña temporal por correo: " + e.getMessage();
        }

        // Registrar auditoría del caso válido
        guardarAuditoriaRecuperacion(username, descripcion);

        // Respuesta genérica (sin revelar si el usuario existe o no)
        RespuestaRs rs = new RespuestaRs();
        rs.setMensaje("Si el usuario existe, se enviará un correo con instrucciones.");
        rs.setStatus(200);
        return rs;
    }

    // =======================
    //   MÉTODOS PRIVADOS
    // =======================

    /**
     * Guarda un registro de auditoría de recuperación de contraseña.
     */
    private void guardarAuditoriaRecuperacion(String username, String descripcion) {
        AuditoriaRecuperacion audit = new AuditoriaRecuperacion();
        audit.setUsernameIngresado(username);
        audit.setDescripcion(descripcion);
        audit.setFecha(LocalDateTime.now());
        auditoriaRecuperacionRepository.save(audit);
    }

    /**
     * Guarda un registro de auditoría de intentos de login.
     */
    private void guardarAuditoriaLogin(String username, String ip, String descripcion) {
        AuditoriaLogin audit = new AuditoriaLogin();
        audit.setUsername(username);
        audit.setIp(ip);
        audit.setDescripcion(descripcion);
        audit.setFecha(LocalDateTime.now());
        auditoriaLoginRepository.save(audit);
    }

    /**
     * Genera una contraseña temporal aleatoria.
     */
    private String generarPasswordTemporal() {
        final String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) { // 10 caracteres
            int idx = random.nextInt(caracteres.length());
            sb.append(caracteres.charAt(idx));
        }
        return sb.toString();
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
        session.setFechaExpiracion(
                fechaExpiracion.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime()
        );
        sessionRepository.save(session);
    }

    /**
     * Obtiene la IP del cliente desde la request actual (si está disponible).
     */
    private String obtenerIpCliente() {
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null) {
                return null;
            }
            HttpServletRequest req = attrs.getRequest();
            String ip = req.getHeader("X-Forwarded-For");
            if (ip == null || ip.isBlank()) {
                ip = req.getRemoteAddr();
            }
            return ip;
        } catch (Exception e) {
            return null;
        }
    }
}





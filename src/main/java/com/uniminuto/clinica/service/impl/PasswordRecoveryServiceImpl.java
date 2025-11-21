package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.AuditoriaLog;
import com.uniminuto.clinica.entity.Usuario;
import com.uniminuto.clinica.model.PasswordRecoveryRq;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.repository.AuditoriaLogRepository;
import com.uniminuto.clinica.repository.UsuarioRepository;
import com.uniminuto.clinica.service.EmailService;
import com.uniminuto.clinica.service.PasswordRecoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementación del servicio de recuperación de contraseñas.
 * 
 * @author Giovanni Mora Jaimes
 * @version 1.0.0
 * @since 2024
 */
@Service
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {

    private static final int TOKEN_EXPIRATION_HOURS = 24;
    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuditoriaLogRepository auditoriaLogRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Solicita la recuperación de contraseña para un usuario.
     * Genera una contraseña temporal, la encripta con BCrypt y la envía por email.
     * Por seguridad, siempre retorna el mismo mensaje genérico.
     * 
     * @param request Datos de la solicitud con el nombre de usuario
     * @param ipAddress Dirección IP desde donde se realiza la solicitud
     * @return Respuesta genérica sin revelar si el usuario existe o no
     */
    @Override
    @Transactional
    public RespuestaRs solicitarRecuperacion(PasswordRecoveryRq request, String ipAddress) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(request.getUsername());

        // Por seguridad, siempre devolver el mismo mensaje
        RespuestaRs respuesta = new RespuestaRs();
        respuesta.setStatus(200);
        respuesta.setMensaje("Si el usuario existe, se enviará una contraseña temporal al correo registrado");

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Generar contraseña temporal
            String passwordTemporal = generarPasswordTemporal();
            
            // LOG TEMPORAL PARA PRUEBAS - Mostrar contraseña generada
            System.out.println("╔════════════════════════════════════════════════════════════╗");
            System.out.println("║  CONTRASEÑA TEMPORAL GENERADA (Solo para pruebas)         ║");
            System.out.println("╠════════════════════════════════════════════════════════════╣");
            System.out.println("║  Usuario: " + usuario.getUsername());
            System.out.println("║  Contraseña Temporal: " + passwordTemporal);
            System.out.println("║  Email: " + usuario.getEmail());
            System.out.println("╚════════════════════════════════════════════════════════════╝");

            // Encriptar con BCrypt y guardar
            String passwordEncriptada = passwordEncoder.encode(passwordTemporal);
            usuario.setPassword(passwordEncriptada);

            // Generar token de recuperación (para futuras validaciones)
            String token = generarToken();
            usuario.setPasswordResetToken(token);
            usuario.setTokenExpiracion(LocalDateTime.now().plusHours(TOKEN_EXPIRATION_HOURS));

            usuarioRepository.save(usuario);

            // Registrar en auditoría
            auditoriaLogRepository.save(AuditoriaLog.recuperacionPassword(usuario, ipAddress));

            // Enviar email con contraseña temporal
            try {
                enviarEmailRecuperacion(usuario, passwordTemporal);
            } catch (Exception e) {
                System.err.println("Error enviando email de recuperación: " + e.getMessage());
                // No revelar el error al cliente por seguridad
            }
        }

        return respuesta;
    }

    /**
     * Genera una contraseña temporal aleatoria de 10 caracteres.
     * Incluye letras mayúsculas, minúsculas, números y caracteres especiales.
     * 
     * @return Contraseña temporal generada
     */
    private String generarPasswordTemporal() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(10);
        
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(CARACTERES.length());
            password.append(CARACTERES.charAt(index));
        }
        
        return password.toString();
    }

    /**
     * Genera un token aleatorio de 32 caracteres para validación.
     * 
     * @return Token generado
     */
    private String generarToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(32);
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        
        for (int i = 0; i < 32; i++) {
            int index = random.nextInt(caracteres.length());
            token.append(caracteres.charAt(index));
        }
        
        return token.toString();
    }

    /**
     * Envía email con la contraseña temporal al usuario.
     * 
     * @param usuario Usuario que solicita recuperación
     * @param passwordTemporal Contraseña temporal generada
     * @throws RuntimeException si falla el envío del email
     */
    private void enviarEmailRecuperacion(Usuario usuario, String passwordTemporal) {
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            return;
        }

        String asunto = "Recuperación de Contraseña - Clínica";
        
        String cuerpo = String.format(
            "Hola %s,\n\n" +
            "Has solicitado la recuperación de tu contraseña.\n\n" +
            "Tu contraseña temporal es: %s\n\n" +
            "Esta contraseña expira en %d horas.\n\n" +
            "Por seguridad, te recomendamos cambiar esta contraseña después de iniciar sesión.\n\n" +
            "Si no solicitaste este cambio, por favor contacta al administrador inmediatamente.\n\n" +
            "Saludos,\n" +
            "Sistema de Clínica",
            usuario.getUsername(),
            passwordTemporal,
            TOKEN_EXPIRATION_HOURS
        );

        try {
            emailService.enviarCorreoSimple(usuario.getEmail(), asunto, cuerpo);
        } catch (Exception e) {
            System.err.println("Error al enviar email: " + e.getMessage());
            throw new RuntimeException("Error al enviar email de recuperación");
        }
    }
}

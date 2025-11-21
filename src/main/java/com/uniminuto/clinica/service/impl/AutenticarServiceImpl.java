package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.AuditoriaLog;
import com.uniminuto.clinica.entity.Usuario;
import com.uniminuto.clinica.model.AutenticatorRs;
import com.uniminuto.clinica.model.AuthenticatorRq;
import com.uniminuto.clinica.repository.AuditoriaLogRepository;
import com.uniminuto.clinica.repository.UsuarioRepository;
import com.uniminuto.clinica.service.AutenticarService;
import com.uniminuto.clinica.service.CifrarService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.uniminuto.clinica.entity.Session;
import com.uniminuto.clinica.repository.SessionRepository;
import com.uniminuto.clinica.security.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

/**
 * Implementación del servicio de autenticación con control de seguridad.
 * 
 * <p>Esta clase implementa el proceso completo de autenticación incluyendo:</p>
 * <ul>
 *   <li>Validación de credenciales con BCrypt password encoder</li>
 *   <li>Control de intentos fallidos (máximo 3 intentos)</li>
 *   <li>Bloqueo temporal de usuarios por 5 minutos</li>
 *   <li>Registro exhaustivo de eventos de auditoría</li>
 *   <li>Generación y gestión de tokens JWT</li>
 *   <li>Rastreo de dirección IP del cliente</li>
 * </ul>
 * 
 * <p>Constantes de configuración:</p>
 * <ul>
 *   <li>{@code MAX_INTENTOS}: 3 intentos fallidos permitidos</li>
 *   <li>{@code MINUTOS_BLOQUEO}: 5 minutos de bloqueo temporal</li>
 * </ul>
 * 
 * @author Giovanni Mora Jaimes
 * @version 1.0
 * @since 2025-11-21
 * @see AutenticarService
 * @see PasswordEncoder
 * @see JwtUtil
 */
@Service
public class AutenticarServiceImpl implements AutenticarService {

    /** Número máximo de intentos fallidos antes de bloquear el usuario */
    private static final int MAX_INTENTOS = 3;
    
    /** Tiempo de bloqueo en minutos después de exceder intentos fallidos */
    private static final int MINUTOS_BLOQUEO = 5;

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
    private AuditoriaLogRepository auditoriaLogRepository;

    @Autowired(required = false)
    private HttpServletRequest request;

    /**
     * Autentica un usuario validando credenciales y aplicando políticas de seguridad.
     * 
     * <p>Flujo de autenticación:</p>
     * <ol>
     *   <li>Obtiene la dirección IP del cliente</li>
     *   <li>Verifica existencia del usuario en la base de datos</li>
     *   <li>Valida si el usuario está bloqueado temporalmente</li>
     *   <li>Valida la contraseña con BCrypt</li>
     *   <li>Si falla: incrementa contador de intentos y registra en auditoría</li>
     *   <li>Si excede intentos: bloquea usuario por tiempo configurado</li>
     *   <li>Si éxito: resetea intentos, genera JWT y registra sesión</li>
     * </ol>
     * 
     * @param authenticatorRq Credenciales del usuario (username y password)
     * @return AutenticatorRs con token JWT, datos del usuario y mensaje de éxito
     * @throws BadRequestException en los siguientes casos:
     *         <ul>
     *           <li>Usuario no existe en el sistema</li>
     *           <li>Contraseña incorrecta</li>
     *           <li>Usuario bloqueado temporalmente</li>
     *           <li>Usuario inactivo en el sistema</li>
     *         </ul>
     */
    @Override
    @Transactional
    public AutenticatorRs autenticar(AuthenticatorRq authenticatorRq)
            throws BadRequestException {

        String ipAddress = obtenerIpCliente();
        
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(authenticatorRq.getUsername());
        if (usuarioOpt.isEmpty()) {
            // Registrar intento fallido sin revelar que el usuario no existe
            auditoriaLogRepository.save(AuditoriaLog.loginFallido(
                authenticatorRq.getUsername(), ipAddress, 0
            ));
            throw new BadRequestException("Usuario o contraseña incorrectos");
        }

        Usuario usuario = usuarioOpt.get();

        // Verificar si el usuario está bloqueado
        if (usuario.estaBloqueado()) {
            long minutosRestantes = java.time.Duration.between(
                LocalDateTime.now(), 
                usuario.getBloqueadoHasta()
            ).toMinutes();
            
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("bloqueado", true);
            errorData.put("bloqueadoHasta", usuario.getBloqueadoHasta().toString());
            errorData.put("mensaje", "Usuario bloqueado. Intente nuevamente en " + minutosRestantes + " minutos");
            
            throw new BadRequestException(
                "Usuario bloqueado. Intente nuevamente en " + minutosRestantes + " minutos"
            );
        }

        // Verificar contraseña
        boolean passwordOk = false;
        try {
            System.out.println("DEBUG: passwordEncoder is null? " + (passwordEncoder == null));
            System.out.println("DEBUG: Usuario password hash: " + usuario.getPassword());
            System.out.println("DEBUG: Input password: " + authenticatorRq.getPassword());
            
            if (passwordEncoder != null) {
                passwordOk = passwordEncoder.matches(authenticatorRq.getPassword(), usuario.getPassword());
                System.out.println("DEBUG: BCrypt match result: " + passwordOk);
            } else {
                // Usar CifrarService como fallback
                String passwordEncriptada = this.cifrarService.encriptarPassword(authenticatorRq.getPassword());
                passwordOk = usuario.getPassword().equals(passwordEncriptada);
                System.out.println("DEBUG: CifrarService match result: " + passwordOk);
            }
        } catch (Exception e) {
            System.err.println("Error verificando contraseña: " + e.getMessage());
            e.printStackTrace();
            passwordOk = false;
        }

        if (!passwordOk) {
            // Registrar intento fallido
            registrarIntentoFallido(usuario, ipAddress);
            
            int intentosRestantes = MAX_INTENTOS - (usuario.getIntentosFallidos() != null ? usuario.getIntentosFallidos() : 0);
            
            if (intentosRestantes <= 0) {
                throw new BadRequestException("Usuario bloqueado por exceder intentos");
            }
            
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("bloqueado", false);
            errorData.put("intentosRestantes", intentosRestantes);
            errorData.put("mensaje", "Credenciales incorrectas. Le quedan " + intentosRestantes + " intentos");
            
            throw new BadRequestException(
                "Credenciales incorrectas. Le quedan " + intentosRestantes + " intentos"
            );
        }

        // Login exitoso - resetear intentos y actualizar último acceso
        registrarLoginExitoso(usuario, ipAddress);

        // Generar y devolver un JWT
        AutenticatorRs rta = new AutenticatorRs();
        String token = jwtUtil.generateToken(usuario);
        rta.setToken(token);
        rta.setUsername(usuario.getUsername());
        rta.setEmail(usuario.getEmail());
        rta.setRol(usuario.getRol());

        // Crear sesión del usuario autenticado
        crearSesionUsuario(usuario, token);
        
        return rta;
    }

    /**
     * Registra un intento fallido de login.
     */
    private void registrarIntentoFallido(Usuario usuario, String ipAddress) {
        int intentos = (usuario.getIntentosFallidos() != null ? usuario.getIntentosFallidos() : 0) + 1;
        usuario.setIntentosFallidos(intentos);
        usuario.setUltimoIntento(LocalDateTime.now());

        int intentosRestantes = MAX_INTENTOS - intentos;

        // Registrar en auditoría
        auditoriaLogRepository.save(AuditoriaLog.loginFallido(
            usuario.getUsername(), ipAddress, intentosRestantes
        ));

        // Si excede el máximo, bloquear usuario
        if (intentos >= MAX_INTENTOS) {
            LocalDateTime bloqueadoHasta = LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEO);
            usuario.setBloqueadoHasta(bloqueadoHasta);
            auditoriaLogRepository.save(AuditoriaLog.usuarioBloqueado(usuario, ipAddress));
        }

        usuarioRepository.save(usuario);
    }

    /**
     * Registra un login exitoso y resetea los intentos fallidos.
     */
    private void registrarLoginExitoso(Usuario usuario, String ipAddress) {
        usuario.setIntentosFallidos(0);
        usuario.setBloqueadoHasta(null);
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuario.setIpUltimaConexion(ipAddress);
        
        usuarioRepository.save(usuario);
        
        // Registrar en auditoría
        auditoriaLogRepository.save(AuditoriaLog.loginExitoso(usuario, ipAddress));
    }

    /**
     * Crea y almacena la sesión del usuario autenticado.
     *
     * @param usuario Usuario autenticado
     * @param token   Token JWT generado
     */
    private void crearSesionUsuario(Usuario usuario, String token) {
        // Elimina cualquier sesión previa del usuario
        sessionRepository.deleteByUserId(usuario.getId());
        Session session = new Session();
        session.setUserId(usuario.getId());
        session.setToken(token);
        session.setFechaIniSesion(LocalDateTime.now());
        Date fechaExpiracion = jwtUtil.getExpirationDateFromToken(token);
        session.setFechaExpiracion(fechaExpiracion.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime());
        sessionRepository.save(session);
    }

    /**
     * Obtiene la IP del cliente.
     */
    private String obtenerIpCliente() {
        if (request == null) {
            return "UNKNOWN";
        }
        
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // Si viene con múltiples IPs, tomar la primera
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip != null ? ip : "UNKNOWN";
    }
}

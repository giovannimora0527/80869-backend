package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.PasswordRecoveryApi;
import com.uniminuto.clinica.model.PasswordRecoveryRq;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.service.PasswordRecoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Controlador REST para gestión de recuperación de contraseñas.
 * Procesa solicitudes de usuarios que olvidaron su contraseña y genera contraseñas temporales.
 * 
 * @author Giovanni Mora Jaimes
 * @version 1.0.0
 * @since 2024
 */
@RestController
public class PasswordRecoveryApiController implements PasswordRecoveryApi {

    @Autowired
    private PasswordRecoveryService passwordRecoveryService;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Override
    public ResponseEntity<RespuestaRs> solicitarRecuperacion(PasswordRecoveryRq recoveryRq) {
        String ipAddress = obtenerIpCliente();
        RespuestaRs respuesta = passwordRecoveryService.solicitarRecuperacion(recoveryRq, ipAddress);
        return ResponseEntity.ok(respuesta);
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
        
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip != null ? ip : "UNKNOWN";
    }
}

package com.uniminuto.clinica.service;

import com.uniminuto.clinica.model.PasswordRecoveryRq;
import com.uniminuto.clinica.model.RespuestaRs;

/**
 * Servicio para recuperación de contraseñas.
 */
public interface PasswordRecoveryService {

    /**
     * Solicita recuperación de contraseña.
     */
    RespuestaRs solicitarRecuperacion(PasswordRecoveryRq request, String ipAddress);
}

package com.uniminuto.clinica.service;

import com.uniminuto.clinica.model.AutenticatorRs;
import com.uniminuto.clinica.model.AuthenticatorRq;
import org.apache.coyote.BadRequestException;

/**
 * Servicio de autenticación de usuarios.
 * Gestiona el proceso de login con control de intentos fallidos y bloqueo temporal.
 * 
 * @author Giovanni Mora Jaimes
 * @version 1.0
 * @since 2025-11-21
 */
public interface AutenticarService {

    /**
     * Autentica un usuario en el sistema validando sus credenciales.
     * Implementa las siguientes funcionalidades de seguridad:
     * <ul>
     *   <li>Validación de credenciales con BCrypt</li>
     *   <li>Control de intentos fallidos (máximo 3)</li>
     *   <li>Bloqueo temporal de 5 minutos tras exceder intentos</li>
     *   <li>Registro de eventos de auditoría (login exitoso/fallido/bloqueo)</li>
     *   <li>Generación de token JWT para sesión</li>
     * </ul>
     * 
     * @param request Objeto con las credenciales del usuario (username y password)
     * @return Objeto AutenticatorRs con el token JWT y datos del usuario
     * @throws BadRequestException si las credenciales son incorrectas o el usuario está bloqueado
     */
    AutenticatorRs autenticar(AuthenticatorRq request) throws BadRequestException;
}

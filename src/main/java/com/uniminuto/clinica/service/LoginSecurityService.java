package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.IntentoFallidoLogin;
import com.uniminuto.clinica.entity.Usuario;
import com.uniminuto.clinica.model.IntentoFallidoRq;
import com.uniminuto.clinica.model.UsuarioBloqueadoRq;
import org.apache.coyote.BadRequestException;

import javax.transaction.Transactional;
import java.util.List;

public interface LoginSecurityService {

    /**
     * Registra un intento fallido de login
     */

    @Transactional
    void registrarIntentoFallido(IntentoFallidoRq intentoRq) throws BadRequestException;

    /**
     * Verifica si un usuario está bloqueado por ID
     */
    boolean estaUsuarioBloqueado(Long usuarioId);


    /**
     * Obtiene el tiempo restante de bloqueo en minutos por ID
     */
    long getTiempoRestanteBloqueo(Long usuarioId);

    /**
     * Limpia los intentos fallidos de un usuario (después de login exitoso)
     */
    void limpiarIntentosFallidos(Long usuarioId);

    /**
     * Obtiene el historial de intentos fallidos de un usuario
     */
    List<IntentoFallidoLogin> obtenerHistorialIntentos(Long usuarioId, int dias);

    /**
     * Bloquea manualmente un usuario
     */
    @Transactional
    void bloquearUsuarioManualmente(UsuarioBloqueadoRq bloqueadoRq) throws BadRequestException;

    /**
     * Desbloquea manualmente un usuario
     */
    void desbloquearUsuarioManualmente(Long usuarioId);
}

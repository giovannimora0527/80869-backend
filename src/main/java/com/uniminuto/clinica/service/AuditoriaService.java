package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.AuditoriaLog;
import com.uniminuto.clinica.model.AuditoriaEstadisticasRs;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para consultar logs de auditoría.
 */
public interface AuditoriaService {

    /**
     * Consulta logs con filtros.
     */
    List<AuditoriaLog> consultarLogs(String tipoEvento, String username, String nivel, 
                                     LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Obtiene estadísticas generales.
     */
    AuditoriaEstadisticasRs obtenerEstadisticas();

    /**
     * Registra un log de auditoría.
     */
    void registrarLog(AuditoriaLog log);
}

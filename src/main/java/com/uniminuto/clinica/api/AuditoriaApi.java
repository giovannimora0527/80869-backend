package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.AuditoriaLog;
import com.uniminuto.clinica.model.AuditoriaEstadisticasRs;
import com.uniminuto.clinica.model.AuditoriaFiltroRq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

/**
 * API para consultar logs de auditoría.
 */
@RequestMapping("/auditoria")
public interface AuditoriaApi {

    /**
     * Consulta logs con filtros.
     */
    @GetMapping("/logs")
    ResponseEntity<List<AuditoriaLog>> consultarLogs(
        @RequestParam(required = false) String tipoEvento,
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String nivel,
        @RequestParam(required = false) String fechaInicio,
        @RequestParam(required = false) String fechaFin
    );

    /**
     * Obtiene estadísticas de auditoría.
     */
    @GetMapping("/estadisticas")
    ResponseEntity<AuditoriaEstadisticasRs> obtenerEstadisticas();
}

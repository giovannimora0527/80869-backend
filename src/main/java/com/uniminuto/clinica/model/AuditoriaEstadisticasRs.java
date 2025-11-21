package com.uniminuto.clinica.model;

import lombok.Data;
import java.util.Map;

/**
 * Response con estadísticas de auditoría.
 */
@Data
public class AuditoriaEstadisticasRs {
    private Long totalEventos;
    private Map<String, Long> eventosPorTipo;
    private Map<String, Long> eventosPorNivel;
    private Long loginExitosos;
    private Long loginFallidos;
    private Long usuariosBloqueados;
}

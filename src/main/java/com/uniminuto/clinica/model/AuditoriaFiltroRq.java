package com.uniminuto.clinica.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Filtro para consultar logs de auditoría.
 */
@Data
public class AuditoriaFiltroRq {
    private String tipoEvento;
    private String username;
    private String nivel;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer page = 0;
    private Integer size = 100;
}

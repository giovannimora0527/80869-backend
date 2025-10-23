package com.uniminuto.clinica.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RecetaRq {
    private Long id;

    @NotNull(message = "El campo citaId es obligatorio")
    private Integer citaId;

    @NotNull(message = "El campo medicamentoId es obligatorio")
    private Integer medicamentoId;

    @NotNull(message = "El campo dosis es obligatorio")
    private String dosis;

    @NotNull(message = "El campo indicaciones es obligatorio")
    private String indicaciones;
}

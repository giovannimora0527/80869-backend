package com.uniminuto.clinica.model;

import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class RecetaRq implements Serializable {

    @NotNull(message = "El campo 'citaId' es obligatorio.")
    private Long citaId;

    @NotNull(message = "El campo 'medicamentoId' es obligatorio.")
    private Long medicamentoId;

    @NotBlank(message = "El campo 'dosis' es obligatorio y no puede estar en blanco.")
    private String dosis;

    private String indicaciones;
}
package com.uniminuto.clinica.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

@Data
public class CitaRq implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id; // Agregar el atributo ID para actualizaciones

    @NotNull
    private Long pacienteId;

    @NotNull
    private Long medicoId;

    @NotNull
    private LocalDateTime fechaHora;

    @NotNull
    private String estado;

    private String motivo;
}
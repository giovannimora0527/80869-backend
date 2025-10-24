package com.uniminuto.clinica.model;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CitaRq {

    private Long id;

    @NotNull(message = "El campo medicoId es obligatorio")
    private Long medicoId;

    @NotNull(message = "El campo pacienteId es obligatorio")
    private Long pacienteId;

    @NotNull(message = "El campo fechaHora es obligatorio")
    private LocalDateTime fechaHora;

    @NotNull(message = "El campo estado es obligatorio")
    private String estado;

    @NotNull(message = "El campo motivo es obligatorio")
    private String motivo;
}
package com.uniminuto.clinica.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CitaRs implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDateTime fechaHora;
    private String estado;
    private String motivo;

    // Solo los IDs y nombres de las relaciones para una respuesta limpia
    private Long pacienteId;
    private String nombreCompletoPaciente;
    private Long medicoId;
    private String nombreCompletoMedico;
}
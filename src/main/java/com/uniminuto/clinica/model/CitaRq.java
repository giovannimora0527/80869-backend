package com.uniminuto.clinica.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CitaRq {
    private Long pacienteId;
    private Long medicoId;
    private LocalDateTime fechaHora;
    private String estado;
    private String motivo;
    
}



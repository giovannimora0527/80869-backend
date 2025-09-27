package com.uniminuto.clinica.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecetaRq {
    private String dosis;
    private String indicaciones;
    private Integer citaId;
    private Long medicamentoId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaRegistro;
}

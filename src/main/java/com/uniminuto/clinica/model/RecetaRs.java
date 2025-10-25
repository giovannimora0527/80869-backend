// Archivo: com.uniminuto.clinica.model.RecetaRs.java

package com.uniminuto.clinica.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RecetaRs implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDateTime fechaCreacionRegistro;
    private String dosis;
    private String indicaciones;

   
    private Long citaId;
    private String nombreMedicamento;
}
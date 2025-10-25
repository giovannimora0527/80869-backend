package com.uniminuto.clinica.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PacienteRs implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String direccion;
    private LocalDateTime fechaNacimiento;
    private String genero;
}
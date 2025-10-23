package com.uniminuto.clinica.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class PacienteRq {
    private Long id;
    private Integer usuarioId;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String genero;
    private String telefono;
    private String direccion;
}

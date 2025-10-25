package com.uniminuto.clinica.model;

import lombok.Data;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class PacienteRq implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "El tipo de documento es obligatorio.")
    private String tipoDocumento;

    @NotNull(message = "El número de documento es obligatorio.")
    private String numeroDocumento;

    @NotNull(message = "El nombre del paciente es obligatorio.")
    private String nombres;

    @NotNull(message = "El apellido del paciente es obligatorio.")
    private String apellidos;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    private LocalDateTime fechaNacimiento;

    @NotNull(message = "El género es obligatorio.")
    private String genero;


    private String telefono;
    private String direccion;
}
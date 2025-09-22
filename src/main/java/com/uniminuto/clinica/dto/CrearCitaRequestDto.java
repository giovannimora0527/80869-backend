package com.uniminuto.clinica.dto;

import lombok.Data;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Future;

/**
 * DTO para la solicitud de creación de una nueva cita.
 * Contiene toda la información necesaria para programar una cita médica.
 *
 * @author AI
 */
@Data
public class CrearCitaRequestDto {

    /**
     * ID del paciente que solicita la cita.
     * No puede ser nulo.
     */
    @NotNull(message = "El ID del paciente es obligatorio")
    private Long pacienteId;

    /**
     * ID del médico con quien se programa la cita.
     * No puede ser nulo.
     */
    @NotNull(message = "El ID del médico es obligatorio")
    private Long medicoId;

    /**
     * Fecha y hora programada para la cita.
     * Debe ser una fecha futura.
     */
    @NotNull(message = "La fecha y hora de la cita es obligatoria")
    @Future(message = "La fecha de la cita debe ser futura")
    private LocalDateTime fechaHora;

    /**
     * Motivo de la consulta médica.
     * No puede estar vacío.
     */
    @NotBlank(message = "El motivo de la cita es obligatorio")
    private String motivo;

    /**
     * Estado inicial de la cita (por defecto será "PROGRAMADA").
     * Campo opcional en el request.
     */
    private String estado;
}
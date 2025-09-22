package com.uniminuto.clinica.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO para un elemento de cita en el listado.
 * Contiene información resumida de la cita sin datos sensibles.
 *
 * @author AI
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CitaResumenDto {

    /**
     * ID único de la cita.
     */
    private Long id;

    /**
     * Fecha y hora programada de la cita.
     */
    private LocalDateTime fechaHora;

    /**
     * Estado actual de la cita.
     */
    private String estado;

    /**
     * Motivo de la cita.
     */
    private String motivo;

    /**
     * Información básica del paciente.
     */
    private PacienteResumenDto paciente;

    /**
     * Información básica del médico.
     */
    private MedicoResumenDto medico;

    /**
     * DTO interno para información resumida del paciente.
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PacienteResumenDto {
        private Long id;
        private String nombres;
        private String apellidos;
        private String tipoDocumento;
        private String numeroDocumento;
    }

    /**
     * DTO interno para información resumida del médico.
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MedicoResumenDto {
        private Long id;
        private String nombres;
        private String apellidos;
        private String numeroDocumento;
        private String especializacion;
    }
}
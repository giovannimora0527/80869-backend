package com.uniminuto.clinica.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO para la respuesta de creación de una cita.
 * Contiene la información de la cita creada exitosamente.
 *
 * @author AI
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrearCitaResponseDto {

    /**
     * ID único de la cita creada.
     */
    private Long citaId;

    /**
     * Información del paciente.
     */
    private PacienteInfoDto paciente;

    /**
     * Información del médico.
     */
    private MedicoInfoDto medico;

    /**
     * Fecha y hora programada de la cita.
     */
    private LocalDateTime fechaHora;

    /**
     * Estado de la cita.
     */
    private String estado;

    /**
     * Motivo de la consulta.
     */
    private String motivo;

    /**
     * Mensaje de confirmación.
     */
    private String mensaje;

    /**
     * DTO interno para información básica del paciente.
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PacienteInfoDto {
        private Long id;
        private String nombres;
        private String apellidos;
        private String numeroDocumento;
        private String tipoDocumento;
    }

    /**
     * DTO interno para información básica del médico.
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MedicoInfoDto {
        private Long id;
        private String nombres;
        private String apellidos;
        private String numeroDocumento;
        private String especializacion;
    }
}
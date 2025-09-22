package com.uniminuto.clinica.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la respuesta de creación de recetas médicas.
 * Contiene toda la información de la receta creada incluyendo datos del médico y paciente.
 * 
 * @author AI
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearRecetaResponseDto {
    
    /** ID de la receta creada */
    private Long recetaId;
    
    /** ID de la cita asociada */
    private Long citaId;
    
    /** Fecha y hora de creación de la receta */
    private LocalDateTime fechaCreacion;
    
    /** Estado de la receta */
    private String estado;
    
    /** Información del médico que prescribe */
    private MedicoInfoDto medico;
    
    /** Información del paciente */
    private PacienteInfoDto paciente;
    
    /** Lista de medicamentos prescritos */
    private List<MedicamentoRecetaDto> medicamentos;
    
    /** Observaciones generales */
    private String observaciones;
    
    /** Indicaciones generales */
    private String indicacionesGenerales;
    
    /** Duración total del tratamiento */
    private Integer duracionTotalTratamiento;
    
    /** Mensaje de confirmación */
    private String mensaje;
    
    /**
     * DTO anidado para información básica del médico
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicoInfoDto {
        private Long id;
        private String nombres;
        private String apellidos;
        private String especialidad;
        private String numeroLicencia;
    }
    
    /**
     * DTO anidado para información básica del paciente
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PacienteInfoDto {
        private Long id;
        private String nombres;
        private String apellidos;
        private String numeroDocumento;
        private String tipoDocumento;
    }
}
package com.uniminuto.clinica.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para el listado de recetas médicas.
 * 
 * <p>Este DTO optimiza la transferencia de datos en operaciones de listado,
 * incluyendo solo la información esencial de recetas junto con resúmenes
 * de entidades relacionadas (paciente, médico, medicamento, cita).</p>
 * 
 * <p>Estructura optimizada para:</p>
 * <ul>
 *   <li>Consultas de listado paginado</li>
 *   <li>Respuestas de API eficientes</li>
 *   <li>Reducción de transferencia de datos innecesarios</li>
 * </ul>
 * 
 * @author Daniel Donado
 * @version 1.0
 * @since 2024-09-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecetaListadoDto {
    
    /**
     * Identificador único de la receta.
     * Clave primaria para referenciar la receta específica.
     */
    private Long id;
    
    /**
     * Timestamp de creación del registro en la base de datos.
     * Utilizado para ordenamiento cronológico y auditoría.
     */
    private LocalDateTime fechaCreacionRegistro;
    
    /**
     * Dosificación prescrita del medicamento.
     * Incluye cantidad, frecuencia y duración del tratamiento.
     * 
     * @example "500mg cada 8 horas por 7 días"
     */
    private String dosis;
    
    /**
     * Indicaciones médicas adicionales para el paciente.
     * Instrucciones especiales de administración o precauciones.
     * 
     * @example "Tomar con alimentos, evitar alcohol"
     */
    private String indicaciones;
    
    /**
     * Información resumida del paciente que recibe la receta.
     * DTO anidado con datos esenciales para identificación.
     */
    private PacienteResumenDto paciente;
    
    /**
     * Información resumida del médico que prescribió la receta.
     * DTO anidado con datos profesionales del prescriptor.
     */
    private MedicoResumenDto medico;
    
    /**
     * Información resumida del medicamento prescrito.
     * DTO anidado con datos del fármaco.
     */
    private MedicamentoResumenDto medicamento;
    
    /**
     * Información resumida de la cita médica asociada.
     * DTO anidado con contexto de la consulta donde se prescribió.
     */
    private CitaResumenDto cita;
    
    /**
     * DTO anidado que contiene información resumida del paciente.
     * Optimizado para listados sin exponer datos sensibles completos.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PacienteResumenDto {
        /**
         * Identificador único del paciente.
         */
        private Long id;
        
        /**
         * Nombre completo del paciente para identificación.
         */
        private String nombre;
        
        /**
         * Número de documento de identidad del paciente.
         */
        private String documento;
        
        /**
         * Correo electrónico del paciente para contacto.
         */
        private String email;
    }
    
    /**
     * DTO anidado que contiene información resumida del médico prescriptor.
     * Incluye datos profesionales esenciales para identificación.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicoResumenDto {
        /**
         * Identificador único del médico.
         */
        private Long id;
        
        /**
         * Nombre completo del médico prescriptor.
         */
        private String nombre;
        
        /**
         * Especialización médica del profesional.
         */
        private String especializacion;
        
        /**
         * Número de registro profesional del médico.
         */
        private String registroProfesional;
    }
    
    /**
     * DTO anidado que contiene información resumida del medicamento.
     * Datos farmacológicos esenciales para identificación del fármaco.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicamentoResumenDto {
        /**
         * Identificador único del medicamento.
         */
        private Long id;
        
        /**
         * Nombre comercial del medicamento.
         */
        private String nombre;
        
        /**
         * Principio activo del fármaco.
         */
        private String principioActivo;
        
        /**
         * Descripción adicional del medicamento.
         */
        private String descripcion;
    }
    
    /**
     * DTO anidado que contiene información resumida de la cita médica.
     * Proporciona contexto de la consulta donde se prescribió la receta.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CitaResumenDto {
        /**
         * Identificador único de la cita médica.
         */
        private Long id;
        
        /**
         * Fecha y hora programada para la cita médica.
         */
        private LocalDateTime fechaHora;
        
        /**
         * Estado actual de la cita (programada, completada, cancelada).
         */
        private String estado;
        
        /**
         * Motivo o descripción de la consulta médica.
         */
        private String motivo;
    }
}
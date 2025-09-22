package com.uniminuto.clinica.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * DTO para la request de creación de recetas médicas.
 * Contiene toda la información necesaria para crear una receta con múltiples medicamentos.
 * 
 * @author AI
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearRecetaRequestDto {
    
    /** ID de la cita a la que se asocia la receta */
    @NotNull(message = "El ID de la cita es obligatorio")
    private Long citaId;
    
    /** Lista de medicamentos prescritos */
    @NotEmpty(message = "La receta debe contener al menos un medicamento")
    @Valid
    private List<MedicamentoRecetaDto> medicamentos;
    
    /** Observaciones generales del médico sobre la receta */
    @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
    private String observaciones;
    
    /** Indicaciones generales para el paciente */
    @Size(max = 1000, message = "Las indicaciones no pueden exceder 1000 caracteres")
    private String indicacionesGenerales;
    
    /** Duración total del tratamiento en días (opcional) */
    private Integer duracionTotalTratamiento;
}
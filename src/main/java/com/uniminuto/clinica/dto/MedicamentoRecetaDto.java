package com.uniminuto.clinica.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * DTO para representar un medicamento dentro de una receta.
 * Contiene toda la información necesaria para la prescripción.
 * 
 * @author AI
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoRecetaDto {
    
    /** ID del medicamento (opcional si se proporciona nombre) */
    private Long medicamentoId;
    
    /** Nombre del medicamento */
    @NotBlank(message = "El nombre del medicamento es obligatorio")
    @Size(min = 2, max = 200, message = "El nombre del medicamento debe tener entre 2 y 200 caracteres")
    private String nombre;
    
    /** Dosis prescrita */
    @NotBlank(message = "La dosis es obligatoria")
    @Size(min = 1, max = 100, message = "La dosis debe tener entre 1 y 100 caracteres")
    private String dosis;
    
    /** Frecuencia de administración */
    @NotBlank(message = "La frecuencia es obligatoria")
    @Size(min = 1, max = 100, message = "La frecuencia debe tener entre 1 y 100 caracteres")
    private String frecuencia;
    
    /** Duración del tratamiento en días */
    @NotNull(message = "La duración del tratamiento es obligatoria")
    @Positive(message = "La duración debe ser un número positivo")
    private Integer duracionDias;
    
    /** Indicaciones especiales para este medicamento */
    @Size(max = 500, message = "Las indicaciones no pueden exceder 500 caracteres")
    private String indicaciones;
}
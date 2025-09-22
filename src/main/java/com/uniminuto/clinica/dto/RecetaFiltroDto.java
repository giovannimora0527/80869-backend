package com.uniminuto.clinica.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para parámetros de filtrado y paginación en consultas de recetas.
 * 
 * <p>Este DTO encapsula todos los criterios de búsqueda disponibles para el listado
 * de recetas médicas, incluyendo filtros por entidades relacionadas, rangos de fechas,
 * búsqueda de texto y configuración de paginación.</p>
 * 
 * <p>Características principales:</p>
 * <ul>
 *   <li>Validaciones integradas para parámetros de paginación</li>
 *   <li>Valores por defecto optimizados para uso común</li>
 *   <li>Soporte para filtros combinados con lógica AND</li>
 *   <li>Flexibilidad en ordenamiento por cualquier campo</li>
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
public class RecetaFiltroDto {
    
    /**
     * Número de página para la paginación (basado en cero).
     * Define qué página de resultados se desea obtener.
     * 
     * @min 0 La primera página es la página 0
     * @default 0 Comienza desde la primera página
     */
    @Min(value = 0, message = "El número de página debe ser mayor o igual a 0")
    @Builder.Default
    private Integer pagina = 0;
    
    /**
     * Cantidad de elementos por página.
     * Controla cuántos registros se incluyen en cada página de resultados.
     * 
     * @min 1 Debe haber al menos un elemento por página
     * @max 100 Límite máximo para evitar sobrecarga del sistema
     * @default 20 Tamaño óptimo para la mayoría de casos de uso
     */
    @Min(value = 1, message = "El tamaño de página debe ser mayor a 0")
    @Max(value = 100, message = "El tamaño de página no puede ser mayor a 100")
    @Builder.Default
    private Integer tamano = 20;
    
    /**
     * Campo de la entidad por el cual ordenar los resultados.
     * Puede ser cualquier campo válido de la entidad Receta.
     * 
     * @default "fechaCreacionRegistro" Ordena por fecha de creación (más recientes primero)
     * @example "id", "dosis", "fechaCreacionRegistro"
     */
    @Builder.Default
    private String ordenarPor = "fechaCreacionRegistro";
    
    /**
     * Dirección del ordenamiento de los resultados.
     * Determina si el orden es ascendente o descendente.
     * 
     * @default "DESC" Orden descendente (más recientes primero)
     * @values "ASC" para ascendente, "DESC" para descendente
     */
    @Builder.Default
    private String direccion = "DESC";
    
    /**
     * Filtro por identificador único del paciente.
     * Cuando se especifica, solo se devuelven recetas del paciente indicado.
     */
    private Long pacienteId;
    
    /**
     * Filtro por identificador único del médico prescriptor.
     * Cuando se especifica, solo se devuelven recetas prescritas por el médico indicado.
     */
    private Long medicoId;
    
    /**
     * Filtro por identificador único del medicamento.
     * Cuando se especifica, solo se devuelven recetas que contienen el medicamento indicado.
     */
    private Long medicamentoId;
    
    /**
     * Fecha y hora de inicio para filtrado temporal.
     * Cuando se especifica, solo se devuelven recetas creadas desde esta fecha (inclusive).
     * 
     * @format ISO 8601 LocalDateTime (yyyy-MM-ddTHH:mm:ss)
     * @example "2024-01-01T00:00:00"
     */
    private LocalDateTime fechaDesde;
    
    /**
     * Fecha y hora de fin para filtrado temporal.
     * Cuando se especifica junto con fechaDesde, se devuelven recetas creadas 
     * en el rango de fechas especificado (ambas fechas inclusive).
     * 
     * @format ISO 8601 LocalDateTime (yyyy-MM-ddTHH:mm:ss)
     * @example "2024-12-31T23:59:59"
     */
    private LocalDateTime fechaHasta;
    
    /**
     * Texto de búsqueda libre en campos de dosis e indicaciones.
     * Realiza búsqueda de coincidencias parciales (LIKE) en los campos textuales
     * de la receta para encontrar contenido específico.
     * 
     * @example "500mg", "cada 8 horas", "acetaminofen"
     */
    private String texto;
}
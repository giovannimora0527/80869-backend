package com.uniminuto.clinica.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import java.time.LocalDateTime;

/**
 * DTO para la solicitud de listado paginado de citas con filtros.
 * Incluye parámetros de paginación, ordenamiento y filtros opcionales.
 *
 * @author AI
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListarCitasRequestDto {

    /**
     * Número de página (basado en 0).
     * Valor por defecto: 0
     */
    @Min(value = 0, message = "El número de página debe ser mayor o igual a 0")
    @Builder.Default
    private Integer page = 0;

    /**
     * Tamaño de página (número de elementos por página).
     * Valor por defecto: 20, máximo: 100
     */
    @Min(value = 1, message = "El tamaño de página debe ser mayor a 0")
    @Max(value = 100, message = "El tamaño de página no puede ser mayor a 100")
    @Builder.Default
    private Integer size = 20;

    /**
     * ID del médico para filtrar citas.
     * Opcional - si no se especifica, no se aplica filtro por médico.
     */
    private Long medicoId;

    /**
     * ID del paciente para filtrar citas.
     * Opcional - si no se especifica, no se aplica filtro por paciente.
     */
    private Long pacienteId;

    /**
     * Estado de la cita para filtrar.
     * Valores posibles: PROGRAMADA, CONFIRMADA, CANCELADA, COMPLETADA
     * Opcional - si no se especifica, no se aplica filtro por estado.
     */
    private String estado;

    /**
     * Fecha y hora de inicio del rango de búsqueda.
     * Formato: yyyy-MM-ddTHH:mm:ss
     * Opcional - si no se especifica, no se aplica filtro de fecha inicial.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime desde;

    /**
     * Fecha y hora de fin del rango de búsqueda.
     * Formato: yyyy-MM-ddTHH:mm:ss
     * Opcional - si no se especifica, no se aplica filtro de fecha final.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime hasta;

    /**
     * Campo por el cual ordenar los resultados.
     * Valor por defecto: fechaHora
     * Valores permitidos: fechaHora, id, estado
     */
    @Builder.Default
    private String sortBy = "fechaHora";

    /**
     * Dirección del ordenamiento.
     * Valor por defecto: desc (descendente)
     * Valores permitidos: asc, desc
     */
    @Builder.Default
    private String sortDir = "desc";
}
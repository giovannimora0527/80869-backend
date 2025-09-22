package com.uniminuto.clinica.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO para la respuesta paginada del listado de citas.
 * Contiene los datos de las citas y metadatos de paginación.
 *
 * @author AI
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListarCitasResponseDto {

    /**
     * Lista de citas en la página actual.
     */
    private List<CitaResumenDto> citas;

    /**
     * Metadatos de paginación.
     */
    private PaginacionMetadata paginacion;

    /**
     * Información de los filtros aplicados.
     */
    private FiltrosAplicados filtros;

    /**
     * DTO interno para metadatos de paginación.
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaginacionMetadata {
        
        /**
         * Página actual (basada en 0).
         */
        private Integer paginaActual;
        
        /**
         * Tamaño de página solicitado.
         */
        private Integer tamañoPagina;
        
        /**
         * Número total de elementos que coinciden con los filtros.
         */
        private Long totalElementos;
        
        /**
         * Número total de páginas.
         */
        private Integer totalPaginas;
        
        /**
         * Indica si esta es la primera página.
         */
        private Boolean esPrimeraPagina;
        
        /**
         * Indica si esta es la última página.
         */
        private Boolean esUltimaPagina;
        
        /**
         * Indica si hay página anterior.
         */
        private Boolean tienePaginaAnterior;
        
        /**
         * Indica si hay página siguiente.
         */
        private Boolean tienePaginaSiguiente;
        
        /**
         * Número de elementos en la página actual.
         */
        private Integer elementosEnPagina;
    }

    /**
     * DTO interno para información de filtros aplicados.
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FiltrosAplicados {
        private Long medicoId;
        private Long pacienteId;
        private String estado;
        private String desde;
        private String hasta;
        private String ordenadoPor;
        private String direccionOrden;
    }
}
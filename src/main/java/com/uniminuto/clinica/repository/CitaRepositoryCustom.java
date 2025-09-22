package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.dto.ListarCitasRequestDto;
import com.uniminuto.clinica.entity.Cita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repositorio personalizado para consultas complejas de citas.
 * Implementa funcionalidades de búsqueda avanzada con filtros múltiples.
 *
 * @author AI
 */
public interface CitaRepositoryCustom {

    /**
     * Busca citas aplicando filtros múltiples con paginación.
     * Ordena por fechaHora descendente con id como tie-breaker.
     * 
     * @param filtros Parámetros de filtrado
     * @param pageable Configuración de paginación y ordenamiento
     * @return Página de citas que coinciden con los filtros
     */
    Page<Cita> findCitasConFiltros(ListarCitasRequestDto filtros, Pageable pageable);

    /**
     * Busca citas aplicando filtros múltiples con paginación y autorización por rol.
     * Filtra automáticamente según el tipo de usuario autenticado.
     * 
     * @param filtros Parámetros de filtrado
     * @param pageable Configuración de paginación y ordenamiento
     * @param usuarioId ID del usuario autenticado
     * @param tipoUsuario Tipo de usuario (PACIENTE, MEDICO, ADMIN)
     * @return Página de citas que coinciden con los filtros y permisos
     */
    Page<Cita> findCitasConFiltrosYAutorizacion(
            ListarCitasRequestDto filtros, 
            Pageable pageable,
            Long usuarioId,
            String tipoUsuario);
}
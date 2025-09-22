package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.dto.CrearCitaRequestDto;
import com.uniminuto.clinica.dto.CrearCitaResponseDto;
import com.uniminuto.clinica.dto.ListarCitasRequestDto;
import com.uniminuto.clinica.dto.ListarCitasResponseDto;
import java.util.List;

/**
 * Servicio para gestionar operaciones de {@link Cita}.
 */
public interface CitaService {
    /**
     * Crea una nueva cita.
     * @param cita entidad a persistir
     * @return cita creada
     */
    Cita crearCita(Cita cita);

    /**
     * Lista las citas en orden descendente por fecha y hora.
     * @return citas más recientes primero
     */
    List<Cita> listarCitasRecientes();
    
    /**
     * Crea una nueva cita con validaciones completas de negocio.
     * Valida existencia de paciente y médico, disponibilidad de horarios,
     * y reglas de negocio antes de crear la cita.
     * 
     * @param request DTO con los datos de la cita a crear
     * @return DTO con la información de la cita creada
     * @throws com.uniminuto.clinica.exception.PacienteNoEncontradoException si el paciente no existe
     * @throws com.uniminuto.clinica.exception.MedicoNoEncontradoException si el médico no existe
     * @throws com.uniminuto.clinica.exception.ConflictoHorarioException si hay conflicto de horarios
     * @throws com.uniminuto.clinica.exception.HorarioNoValidoException si la hora está fuera del horario de atención
     */
    CrearCitaResponseDto crearCitaConValidaciones(CrearCitaRequestDto request);
    
    /**
     * Lista las citas con filtros y paginación.
     * Aplica filtros opcionales por médico, paciente, estado y rango de fechas.
     * Ordena por fecha y hora descendente con paginación configurable.
     * 
     * @param request DTO con los parámetros de filtrado y paginación
     * @return DTO con la página de citas y metadatos de paginación
     */
    ListarCitasResponseDto listarCitasConFiltros(ListarCitasRequestDto request);
    
    /**
     * Lista las citas con filtros, paginación y autorización por rol.
     * Aplica automáticamente filtros de seguridad según el tipo de usuario.
     * 
     * @param request DTO con los parámetros de filtrado y paginación
     * @param usuarioId ID del usuario autenticado
     * @param tipoUsuario Tipo de usuario (PACIENTE, MEDICO, ADMIN)
     * @return DTO con la página de citas autorizadas y metadatos de paginación
     */
    ListarCitasResponseDto listarCitasConFiltrosYAutorizacion(
            ListarCitasRequestDto request, 
            Long usuarioId, 
            String tipoUsuario);
}



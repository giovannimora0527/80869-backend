package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.dto.CrearRecetaRequestDto;
import com.uniminuto.clinica.dto.CrearRecetaResponseDto;
import com.uniminuto.clinica.dto.RecetaListadoDto;
import com.uniminuto.clinica.dto.RecetaFiltroDto;
import org.springframework.data.domain.Page;
import java.util.List;

/**
 * Servicio de negocio para la gestión integral de recetas médicas.
 * 
 * <p>Esta interfaz define todas las operaciones de negocio relacionadas con recetas médicas,
 * incluyendo creación, consulta, listado y filtrado. Implementa la lógica de negocio
 * necesaria para mantener la integridad de los datos y aplicar las reglas clínicas.</p>
 * 
 * <p>Funcionalidades principales:</p>
 * <ul>
 *   <li>Creación de recetas con validaciones completas</li>
 *   <li>Listado paginado con filtros avanzados</li>
 *   <li>Consultas especializadas por paciente y médico</li>
 *   <li>Transformación de entidades a DTOs optimizados</li>
 * </ul>
 * 
 * @author Daniel Donado
 * @version 1.0
 * @since 2024-09-21
 */
public interface RecetaService {
    /**
     * Crea una nueva receta básica asociada a una cita médica.
     * 
     * <p>Este método proporciona funcionalidad básica de creación sin validaciones
     * extensas. Para operaciones de producción se recomienda usar
     * {@link #crearRecetaCompleta(CrearRecetaRequestDto, Long)}.</p>
     * 
     * @param receta Entidad receta a persistir con datos básicos
     * @return Receta creada con ID asignado y timestamp automático
     */
    Receta crearReceta(Receta receta);

    /**
     * Lista todas las recetas del sistema sin paginación.
     * 
     * <p><strong>Nota:</strong> Este método puede retornar grandes cantidades de datos
     * y afectar el rendimiento. Se recomienda usar {@link #listarRecetasConFiltros(RecetaFiltroDto)}
     * para consultas de producción.</p>
     * 
     * @return Lista completa de todas las recetas en el sistema
     * @deprecated Usar {@link #listarRecetasConFiltros(RecetaFiltroDto)} para mejor rendimiento
     */
    @Deprecated
    List<Receta> listarRecetas();
    
    /**
     * Crea una nueva receta médica completa aplicando todas las validaciones de negocio.
     * 
     * <p>Este método implementa el flujo completo de creación de recetas con validaciones
     * exhaustivas para garantizar la integridad de los datos clínicos.</p>
     * 
     * <p>Validaciones aplicadas:</p>
     * <ul>
     *   <li>Existencia y estado válido de la cita médica</li>
     *   <li>Autorización del médico prescriptor</li>
     *   <li>Validación de medicamentos prescritos</li>
     *   <li>Verificación de duplicados (según reglas de negocio)</li>
     *   <li>Integridad referencial de entidades relacionadas</li>
     * </ul>
     * 
     * @param request DTO con todos los datos necesarios para crear la receta
     * @param medicoId Identificador del médico que autoriza la prescripción
     * @return DTO con la información de la receta creada y datos relacionados
     * @throws com.uniminuto.clinica.exception.CitaNoEncontradaException si la cita no existe
     * @throws com.uniminuto.clinica.exception.EstadoCitaInvalidoException si la cita no está en estado válido
     * @throws com.uniminuto.clinica.exception.MedicoNoAutorizadoException si el médico no está autorizado
     * @throws com.uniminuto.clinica.exception.RecetaDuplicadaException si ya existe una receta activa
     */
    CrearRecetaResponseDto crearRecetaCompleta(CrearRecetaRequestDto request, Long medicoId);
    
    /**
     * Valida que una cita esté en estado apropiado para generar recetas médicas.
     * 
     * <p>Verifica que la cita se encuentre en uno de los estados que permiten
     * la prescripción de medicamentos según las reglas de negocio del sistema.</p>
     * 
     * @param citaId Identificador único de la cita a validar
     * @return {@code true} si la cita está en estado válido para prescripción, {@code false} en caso contrario
     */
    boolean validarEstadoCitaParaReceta(Long citaId);
    
    /**
     * Verifica la autorización de un médico para prescribir recetas en una cita específica.
     * 
     * <p>Implementa las reglas de autorización que determinan si un médico puede
     * crear recetas para una cita determinada, considerando la asignación del médico
     * a la cita y otros criterios de seguridad.</p>
     * 
     * @param citaId Identificador único de la cita médica
     * @param medicoId Identificador único del médico prescriptor
     * @return {@code true} si el médico está autorizado para prescribir en la cita, {@code false} en caso contrario
     */
    boolean verificarAutorizacionMedico(Long citaId, Long medicoId);
    
    /**
     * Lista recetas aplicando filtros dinámicos y paginación optimizada.
     * 
     * <p>Método principal para consultas de recetas que permite combinar múltiples criterios
     * de filtrado con paginación eficiente. Retorna DTOs optimizados para listado con
     * información resumida de entidades relacionadas.</p>
     * 
     * @param filtros DTO con criterios de filtrado, paginación y ordenamiento
     * @return Página de recetas transformadas a DTOs de listado con información completa
     */
    Page<RecetaListadoDto> listarRecetasConFiltros(RecetaFiltroDto filtros);
    
    /**
     * Lista todas las recetas prescritas para un paciente específico.
     * 
     * <p>Endpoint especializado para consultas del historial médico de un paciente,
     * útil para seguimiento clínico y revisión de tratamientos previos.</p>
     * 
     * @param pacienteId Identificador único del paciente
     * @param pagina Número de página a consultar (basado en cero)
     * @param tamano Cantidad máxima de recetas por página
     * @return Página de recetas del paciente con información detallada
     */
    Page<RecetaListadoDto> listarRecetasPorPaciente(Long pacienteId, int pagina, int tamano);
    
    /**
     * Lista todas las recetas prescritas por un médico específico.
     * 
     * <p>Endpoint especializado para consultas del historial de prescripciones de un médico,
     * útil para análisis de patrones de prescripción y reportes profesionales.</p>
     * 
     * @param medicoId Identificador único del médico prescriptor
     * @param pagina Número de página a consultar (basado en cero)
     * @param tamano Cantidad máxima de recetas por página
     * @return Página de recetas prescritas por el médico con información detallada
     */
    Page<RecetaListadoDto> listarRecetasPorMedico(Long medicoId, int pagina, int tamano);
}



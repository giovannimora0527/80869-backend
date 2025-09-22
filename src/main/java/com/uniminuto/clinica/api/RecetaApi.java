package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.dto.CrearRecetaRequestDto;
import com.uniminuto.clinica.dto.CrearRecetaResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;

/**
 * API REST para la gestión integral de recetas médicas.
 * 
 * <p>Esta interfaz define todos los endpoints disponibles para operaciones relacionadas
 * con recetas médicas, incluyendo creación, consulta y listado con filtros avanzados.
 * Proporciona tanto endpoints básicos para compatibilidad como endpoints avanzados
 * para funcionalidades de producción.</p>
 * 
 * <p>Funcionalidades principales:</p>
 * <ul>
 *   <li>Creación de recetas con validaciones completas</li>
 *   <li>Listado paginado con múltiples filtros</li>
 *   <li>Consultas especializadas por paciente y médico</li>
 *   <li>Soporte completo para CORS</li>
 * </ul>
 * 
 * <p>Todas las respuestas utilizan formato JSON y incluyen códigos de estado HTTP
 * apropiados para cada operación.</p>
 * 
 * @author Daniel Donado
 * @version 1.0
 * @since 2024-09-21
 */
@CrossOrigin(origins = "*")
@RequestMapping("/receta")
public interface RecetaApi {

    /**
     * Crea una receta básica asociada a una cita médica existente.
     * 
     * <p>Endpoint básico para la creación rápida de recetas. Para operaciones
     * de producción se recomienda usar el endpoint de creación completa.</p>
     * 
     * @param receta Entidad receta con datos básicos a crear
     * @return ResponseEntity con la receta creada y código 201 (Created)
     */
    @RequestMapping(value = "/crear",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<Receta> crearReceta(@RequestBody Receta receta);

    /**
     * Lista todas las recetas del sistema sin paginación.
     * 
     * <p><strong>Nota:</strong> Este endpoint puede retornar grandes volúmenes de datos.
     * Para consultas de producción se recomienda usar el endpoint de listado con filtros.</p>
     * 
     * @return ResponseEntity con lista completa de recetas ordenadas por fecha de creación descendente
     * @deprecated Usar {@link #listarRecetasConFiltros} para mejor rendimiento
     */
    @Deprecated
    @RequestMapping(value = "/listar",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Receta>> listarRecetas();
    
    /**
     * Crea una nueva receta médica completa aplicando todas las validaciones de negocio.
     * 
     * <p>Endpoint principal para la creación de recetas en entornos de producción.
     * Implementa validaciones exhaustivas para garantizar la integridad de los datos
     * clínicos y el cumplimiento de las reglas de negocio.</p>
     * 
     * <p>Validaciones aplicadas automáticamente:</p>
     * - La cita debe existir y estar en estado válido (FINALIZADA/EN_CURSO)
     * - El médico debe ser el asignado a la cita
     * - La receta debe contener al menos un medicamento
     * - Los datos de medicamentos deben ser válidos
     * - No se permite crear recetas duplicadas (opcional según reglas de negocio)
     * 
     * Autorización:
     * - Solo médicos autenticados pueden usar este endpoint
     * - El médico debe ser el mismo asignado a la cita
     * 
     * @param request DTO con los datos de la receta a crear
     * @return DTO con la información de la receta creada
     * 
     * @apiNote Códigos de respuesta:
     * - 201: Receta creada exitosamente
     * - 400: Datos de entrada inválidos o falta información requerida
     * - 403: Médico no autorizado para crear receta en esta cita
     * - 404: Cita no encontrada
     * - 409: Ya existe una receta activa para esta cita (si aplica regla de negocio)
     * - 422: Estado de la cita no permite crear receta
     */
    @RequestMapping(value = "/crear-completa",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<CrearRecetaResponseDto> crearRecetaCompleta(@Valid @RequestBody CrearRecetaRequestDto request);
    
    /**
     * Valida si una cita está en estado apropiado para crear receta.
     * 
     * Endpoint de utilidad para verificar si una cita puede tener una receta
     * antes de proceder con la creación.
     * 
     * @param citaId ID de la cita a validar
     * @return Respuesta indicando si la cita es válida para receta
     */
    @RequestMapping(value = "/validar-cita/{citaId}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<Boolean> validarCitaParaReceta(@PathVariable("citaId") Long citaId);
    
    /**
     * Lista recetas con filtros y paginación.
     * 
     * Permite obtener un listado paginado de recetas con múltiples filtros aplicables:
     * - Por paciente, médico o medicamento
     * - Por rango de fechas de creación
     * - Por texto en dosis o indicaciones
     * - Ordenamiento configurable
     * 
     * @param pagina Número de página (base 0)
     * @param tamano Tamaño de página (1-100)
     * @param ordenarPor Campo por el cual ordenar (default: fechaCreacionRegistro)
     * @param direccion Dirección del ordenamiento ASC/DESC (default: DESC)
     * @param pacienteId Filtro por ID del paciente (opcional)
     * @param medicoId Filtro por ID del médico (opcional)
     * @param medicamentoId Filtro por ID del medicamento (opcional)
     * @param fechaDesde Filtro por fecha de inicio (opcional, formato: yyyy-MM-ddTHH:mm:ss)
     * @param fechaHasta Filtro por fecha de fin (opcional, formato: yyyy-MM-ddTHH:mm:ss)
     * @param texto Filtro por texto en dosis o indicaciones (opcional)
     * @return ResponseEntity con página de recetas filtradas y código 200 (OK)
     */
    @RequestMapping(value = "/listado",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<org.springframework.data.domain.Page<com.uniminuto.clinica.dto.RecetaListadoDto>> listarRecetasConFiltros(
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamano", defaultValue = "20") Integer tamano,
            @RequestParam(value = "ordenarPor", defaultValue = "fechaCreacionRegistro") String ordenarPor,
            @RequestParam(value = "direccion", defaultValue = "DESC") String direccion,
            @RequestParam(value = "pacienteId", required = false) Long pacienteId,
            @RequestParam(value = "medicoId", required = false) Long medicoId,
            @RequestParam(value = "medicamentoId", required = false) Long medicamentoId,
            @RequestParam(value = "fechaDesde", required = false) String fechaDesde,
            @RequestParam(value = "fechaHasta", required = false) String fechaHasta,
            @RequestParam(value = "texto", required = false) String texto);
    
    /**
     * Lista todas las recetas prescritas para un paciente específico.
     * 
     * <p>Endpoint especializado para consultar el historial médico completo
     * de un paciente, útil para seguimiento clínico y revisión de tratamientos.</p>
     * 
     * @param pacienteId Identificador único del paciente
     * @param pagina Número de página a consultar (basado en cero)
     * @param tamano Cantidad de recetas por página (1-100)
     * @return ResponseEntity con página de recetas del paciente y código 200 (OK)
     */
    @RequestMapping(value = "/paciente/{pacienteId}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<org.springframework.data.domain.Page<com.uniminuto.clinica.dto.RecetaListadoDto>> listarRecetasPorPaciente(
            @PathVariable("pacienteId") Long pacienteId,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamano", defaultValue = "20") Integer tamano);
    
    /**
     * Lista todas las recetas prescritas por un médico específico.
     * 
     * <p>Endpoint especializado para consultar el historial de prescripciones
     * de un médico, útil para análisis de patrones y reportes profesionales.</p>
     * 
     * @param medicoId Identificador único del médico prescriptor
     * @param pagina Número de página a consultar (basado en cero)
     * @param tamano Cantidad de recetas por página (1-100)
     * @return ResponseEntity con página de recetas del médico y código 200 (OK)
     */
    @RequestMapping(value = "/medico/{medicoId}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<org.springframework.data.domain.Page<com.uniminuto.clinica.dto.RecetaListadoDto>> listarRecetasPorMedico(
            @PathVariable("medicoId") Long medicoId,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamano", defaultValue = "20") Integer tamano);
}



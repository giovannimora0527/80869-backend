package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.dto.CrearCitaRequestDto;
import com.uniminuto.clinica.dto.CrearCitaResponseDto;
import com.uniminuto.clinica.dto.ListarCitasResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * API para operaciones de {@link Cita}.
 */
@CrossOrigin(origins = "*")
@RequestMapping("/cita")
public interface CitaApi {

    /**
     * Crea una nueva cita relacionando paciente y médico.
     * @param cita cuerpo de la solicitud
     * @return cita creada
     */
    @RequestMapping(value = "/crear",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<Cita> crearCita(@RequestBody Cita cita);

    /**
     * Lista las citas por fecha y hora descendente.
     * @return listado de citas
     */
    @RequestMapping(value = "/listar-recientes",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Cita>> listarCitasRecientes();
    
    /**
     * Crea una nueva cita con validaciones completas de negocio.
     * Incluye validación de existencia de paciente y médico, disponibilidad
     * de horarios, y reglas de negocio de la clínica.
     * 
     * @param request DTO con los datos de la cita a crear
     * @return DTO con la información de la cita creada
     */
    @RequestMapping(value = "/crear-con-validaciones",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<CrearCitaResponseDto> crearCitaConValidaciones(@Valid @RequestBody CrearCitaRequestDto request);
    
    /**
     * Lista las citas con filtros opcionales y paginación.
     * Permite filtrar por médico, paciente, estado y rango de fechas.
     * Ordena por fecha y hora descendente por defecto.
     * 
     * @param page Número de página (basado en 0), por defecto: 0
     * @param size Tamaño de página, por defecto: 20, máximo: 100
     * @param medicoId ID del médico para filtrar (opcional)
     * @param pacienteId ID del paciente para filtrar (opcional)
     * @param estado Estado de la cita para filtrar (opcional)
     * @param desde Fecha y hora de inicio del rango (opcional)
     * @param hasta Fecha y hora de fin del rango (opcional)
     * @param sortBy Campo por el cual ordenar (fechaHora, id, estado)
     * @param sortDir Dirección del ordenamiento (asc, desc)
     * @return Página de citas con metadatos de paginación
     */
    @RequestMapping(value = "/listar",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<ListarCitasResponseDto> listarCitas(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) Long medicoId,
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            @RequestParam(defaultValue = "fechaHora") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir);
}



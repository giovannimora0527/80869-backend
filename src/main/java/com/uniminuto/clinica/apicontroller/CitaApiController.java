package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.CitaApi;
import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.service.CitaService;
import com.uniminuto.clinica.dto.CrearCitaRequestDto;
import com.uniminuto.clinica.dto.CrearCitaResponseDto;
import com.uniminuto.clinica.dto.ListarCitasRequestDto;
import com.uniminuto.clinica.dto.ListarCitasResponseDto;
import com.uniminuto.clinica.exception.PacienteNoEncontradoException;
import com.uniminuto.clinica.exception.MedicoNoEncontradoException;
import com.uniminuto.clinica.exception.ConflictoHorarioException;
import com.uniminuto.clinica.exception.HorarioNoValidoException;
import com.uniminuto.clinica.model.RespuestaRs;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import javax.validation.Valid;

/**
 * Controlador REST para {@link Cita}.
 */
@RestController
public class CitaApiController implements CitaApi {

    @Autowired
    private CitaService citaService;

    @Override
    public ResponseEntity<Cita> crearCita(Cita cita) {
        return ResponseEntity.ok(citaService.crearCita(cita));
    }

    @Override
    public ResponseEntity<List<Cita>> listarCitasRecientes() {
        return ResponseEntity.ok(citaService.listarCitasRecientes());
    }
    
    @Override
    public ResponseEntity<CrearCitaResponseDto> crearCitaConValidaciones(@Valid CrearCitaRequestDto request) {
        try {
            CrearCitaResponseDto response = citaService.crearCitaConValidaciones(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Las excepciones específicas son manejadas por los @ExceptionHandler
            throw e;
        }
    }
    
    @Override
    public ResponseEntity<ListarCitasResponseDto> listarCitas(
            Integer page,
            Integer size,
            Long medicoId,
            Long pacienteId,
            String estado,
            LocalDateTime desde,
            LocalDateTime hasta,
            String sortBy,
            String sortDir) {
        
        // Validar parámetros de entrada
        validarParametrosPaginacion(page, size);
        validarParametrosOrdenamiento(sortBy, sortDir);
        validarRangoFechas(desde, hasta);
        
        // Construir DTO de request
        ListarCitasRequestDto request = ListarCitasRequestDto.builder()
                .page(page)
                .size(size)
                .medicoId(medicoId)
                .pacienteId(pacienteId)
                .estado(estado)
                .desde(desde)
                .hasta(hasta)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .build();
        
        // Ejecutar consulta
        ListarCitasResponseDto response = citaService.listarCitasConFiltros(request);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Maneja errores cuando no se encuentra un paciente.
     */
    @ExceptionHandler(PacienteNoEncontradoException.class)
    public ResponseEntity<RespuestaRs> manejarPacienteNoEncontrado(PacienteNoEncontradoException e) {
        RespuestaRs respuesta = new RespuestaRs();
        respuesta.setMessage("PACIENTE_NO_ENCONTRADO: " + e.getMessage());
        respuesta.setEstaFuncionando(false);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }
    
    /**
     * Maneja errores cuando no se encuentra un médico.
     */
    @ExceptionHandler(MedicoNoEncontradoException.class)
    public ResponseEntity<RespuestaRs> manejarMedicoNoEncontrado(MedicoNoEncontradoException e) {
        RespuestaRs respuesta = new RespuestaRs();
        respuesta.setMessage("MEDICO_NO_ENCONTRADO: " + e.getMessage());
        respuesta.setEstaFuncionando(false);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }
    
    /**
     * Maneja errores de conflictos de horario.
     */
    @ExceptionHandler(ConflictoHorarioException.class)
    public ResponseEntity<RespuestaRs> manejarConflictoHorario(ConflictoHorarioException e) {
        RespuestaRs respuesta = new RespuestaRs();
        respuesta.setMessage("CONFLICTO_HORARIO: " + e.getMessage());
        respuesta.setEstaFuncionando(false);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }
    
    /**
     * Maneja errores de horarios no válidos.
     */
    @ExceptionHandler(HorarioNoValidoException.class)
    public ResponseEntity<RespuestaRs> manejarHorarioNoValido(HorarioNoValidoException e) {
        RespuestaRs respuesta = new RespuestaRs();
        respuesta.setMessage("HORARIO_NO_VALIDO: " + e.getMessage());
        respuesta.setEstaFuncionando(false);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }
    
    /**
     * Maneja errores de validación de entrada.
     */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaRs> manejarErroresValidacion(
            org.springframework.web.bind.MethodArgumentNotValidException e) {
        
        StringBuilder mensaje = new StringBuilder("Errores de validación: ");
        BindingResult result = e.getBindingResult();
        
        for (FieldError error : result.getFieldErrors()) {
            mensaje.append(error.getField())
                   .append(": ")
                   .append(error.getDefaultMessage())
                   .append("; ");
        }
        
        RespuestaRs respuesta = new RespuestaRs();
        respuesta.setMessage("ERROR_VALIDACION: " + mensaje.toString());
        respuesta.setEstaFuncionando(false);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }
    
    /**
     * Valida los parámetros de paginación.
     */
    private void validarParametrosPaginacion(Integer page, Integer size) {
        if (page != null && page < 0) {
            throw new IllegalArgumentException("El número de página debe ser mayor o igual a 0");
        }
        
        if (size != null && (size < 1 || size > 100)) {
            throw new IllegalArgumentException("El tamaño de página debe estar entre 1 y 100");
        }
    }
    
    /**
     * Valida los parámetros de ordenamiento.
     */
    private void validarParametrosOrdenamiento(String sortBy, String sortDir) {
        if (sortBy != null && !sortBy.matches("fechaHora|id|estado")) {
            throw new IllegalArgumentException(
                "Campo de ordenamiento inválido. Valores permitidos: fechaHora, id, estado");
        }
        
        if (sortDir != null && !sortDir.matches("(?i)asc|desc")) {
            throw new IllegalArgumentException(
                "Dirección de ordenamiento inválida. Valores permitidos: asc, desc");
        }
    }
    
    /**
     * Valida el rango de fechas.
     */
    private void validarRangoFechas(LocalDateTime desde, LocalDateTime hasta) {
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new IllegalArgumentException(
                "La fecha de inicio debe ser anterior o igual a la fecha de fin");
        }
    }
    
    /**
     * Maneja errores de argumentos inválidos.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RespuestaRs> manejarArgumentosInvalidos(IllegalArgumentException e) {
        RespuestaRs respuesta = new RespuestaRs();
        respuesta.setMessage("PARAMETROS_INVALIDOS: " + e.getMessage());
        respuesta.setEstaFuncionando(false);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }
}



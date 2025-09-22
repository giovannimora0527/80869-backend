package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.entity.Medico;
import com.uniminuto.clinica.repository.CitaRepository;
import com.uniminuto.clinica.repository.PacienteRepository;
import com.uniminuto.clinica.repository.MedicoRepository;
import com.uniminuto.clinica.service.CitaService;
import com.uniminuto.clinica.dto.CrearCitaRequestDto;
import com.uniminuto.clinica.dto.CrearCitaResponseDto;
import com.uniminuto.clinica.dto.ListarCitasRequestDto;
import com.uniminuto.clinica.dto.ListarCitasResponseDto;
import com.uniminuto.clinica.dto.CitaResumenDto;
import com.uniminuto.clinica.exception.PacienteNoEncontradoException;
import com.uniminuto.clinica.exception.MedicoNoEncontradoException;
import com.uniminuto.clinica.exception.ConflictoHorarioException;
import com.uniminuto.clinica.exception.HorarioNoValidoException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio {@link CitaService}.
 */
@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private CitaRepository citaRepository;
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private MedicoRepository medicoRepository;

    /** {@inheritDoc} */
    @Override
    public Cita crearCita(Cita cita) {
        return citaRepository.save(cita);
    }

    /** {@inheritDoc} */
    @Override
    public List<Cita> listarCitasRecientes() {
        return citaRepository.findAllByOrderByFechaHoraDesc();
    }
    
    /** {@inheritDoc} */
    @Override
    @Transactional
    public CrearCitaResponseDto crearCitaConValidaciones(CrearCitaRequestDto request) {
        // 1. Validar que el paciente existe
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(request.getPacienteId());
        if (!pacienteOpt.isPresent()) {
            throw new PacienteNoEncontradoException(
                "No se encontró un paciente con ID: " + request.getPacienteId());
        }
        Paciente paciente = pacienteOpt.get();
        
        // 2. Validar que el médico existe
        Optional<Medico> medicoOpt = medicoRepository.findById(request.getMedicoId());
        if (!medicoOpt.isPresent()) {
            throw new MedicoNoEncontradoException(
                "No se encontró un médico con ID: " + request.getMedicoId());
        }
        Medico medico = medicoOpt.get();
        
        // 3. Validar horario de atención (Lunes a Viernes, 8:00 AM - 6:00 PM)
        validarHorarioAtencion(request.getFechaHora());
        
        // 4. Validar disponibilidad del médico (no debe tener citas en el mismo horario)
        validarDisponibilidadMedico(medico, request.getFechaHora());
        
        // 5. Validar que el paciente no tenga otra cita el mismo día
        validarCitaPacienteMismoDia(paciente, request.getFechaHora());
        
        // 6. Crear la entidad Cita
        Cita nuevaCita = new Cita();
        nuevaCita.setPaciente(paciente);
        nuevaCita.setMedico(medico);
        nuevaCita.setFechaHora(request.getFechaHora());
        nuevaCita.setMotivo(request.getMotivo());
        nuevaCita.setEstado(request.getEstado() != null ? request.getEstado() : "PROGRAMADA");
        
        // 7. Guardar la cita
        Cita citaGuardada = citaRepository.save(nuevaCita);
        
        // 8. Construir el response DTO
        return CrearCitaResponseDto.builder()
                .citaId(citaGuardada.getId())
                .paciente(CrearCitaResponseDto.PacienteInfoDto.builder()
                        .id(paciente.getId())
                        .nombres(paciente.getNombres())
                        .apellidos(paciente.getApellidos())
                        .numeroDocumento(paciente.getNumeroDocumento())
                        .tipoDocumento(paciente.getTipoDocumento())
                        .build())
                .medico(CrearCitaResponseDto.MedicoInfoDto.builder()
                        .id(medico.getId())
                        .nombres(medico.getNombres())
                        .apellidos(medico.getApellidos())
                        .numeroDocumento(medico.getNumeroDocumento())
                        .especializacion("General") // TODO: obtener de la entidad cuando esté disponible
                        .build())
                .fechaHora(citaGuardada.getFechaHora())
                .estado(citaGuardada.getEstado())
                .motivo(citaGuardada.getMotivo())
                .mensaje("Cita creada exitosamente")
                .build();
    }
    
    /**
     * Valida que la cita esté dentro del horario de atención.
     * Horario: Lunes a Viernes, 8:00 AM - 6:00 PM
     */
    private void validarHorarioAtencion(LocalDateTime fechaHora) {
        DayOfWeek diaSemana = fechaHora.getDayOfWeek();
        LocalTime hora = fechaHora.toLocalTime();
        
        // Validar día de la semana (Lunes a Viernes)
        if (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) {
            throw new HorarioNoValidoException(
                "No se pueden programar citas los fines de semana. " +
                "Horario de atención: Lunes a Viernes de 8:00 AM a 6:00 PM");
        }
        
        // Validar horario (8:00 AM - 6:00 PM)
        LocalTime horaInicio = LocalTime.of(8, 0);  // 8:00 AM
        LocalTime horaFin = LocalTime.of(18, 0);    // 6:00 PM
        
        if (hora.isBefore(horaInicio) || hora.isAfter(horaFin)) {
            throw new HorarioNoValidoException(
                "La hora seleccionada está fuera del horario de atención. " +
                "Horario: Lunes a Viernes de 8:00 AM a 6:00 PM");
        }
    }
    
    /**
     * Valida que el médico no tenga conflictos de horario.
     * Considera un rango de 30 minutos antes y después de la hora solicitada.
     */
    private void validarDisponibilidadMedico(Medico medico, LocalDateTime fechaHora) {
        LocalDateTime fechaInicio = fechaHora.minusMinutes(30);
        LocalDateTime fechaFin = fechaHora.plusMinutes(30);
        
        List<Cita> citasConflictivas = citaRepository.findConflictingAppointments(
                medico, fechaInicio, fechaFin);
        
        if (!citasConflictivas.isEmpty()) {
            throw new ConflictoHorarioException(
                "El médico " + medico.getNombres() + " " + medico.getApellidos() + 
                " ya tiene una cita programada en este horario. " +
                "Por favor seleccione otra hora.");
        }
    }
    
    /**
     * Valida que el paciente no tenga otra cita el mismo día.
     */
    private void validarCitaPacienteMismoDia(Paciente paciente, LocalDateTime fechaHora) {
        LocalDateTime inicioDia = fechaHora.toLocalDate().atStartOfDay();
        LocalDateTime finDia = fechaHora.toLocalDate().atTime(23, 59, 59);
        
        List<Cita> citasDelDia = citaRepository.findPatientAppointmentsInRange(
                paciente, inicioDia, finDia);
        
        if (!citasDelDia.isEmpty()) {
            throw new ConflictoHorarioException(
                "El paciente " + paciente.getNombres() + " " + paciente.getApellidos() + 
                " ya tiene una cita programada para este día. " +
                "Solo se permite una cita por paciente por día.");
        }
    }
    
    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public ListarCitasResponseDto listarCitasConFiltros(ListarCitasRequestDto request) {
        // Crear configuración de paginación
        Pageable pageable = crearConfiguracionPaginacion(request);
        
        // Ejecutar consulta paginada
        Page<Cita> paginaCitas = citaRepository.findCitasConFiltros(request, pageable);
        
        // Mapear a DTOs y construir respuesta
        return construirRespuestaPaginada(paginaCitas, request);
    }
    
    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public ListarCitasResponseDto listarCitasConFiltrosYAutorizacion(
            ListarCitasRequestDto request, 
            Long usuarioId, 
            String tipoUsuario) {
        
        // Crear configuración de paginación
        Pageable pageable = crearConfiguracionPaginacion(request);
        
        // Ejecutar consulta paginada con autorización
        Page<Cita> paginaCitas = citaRepository.findCitasConFiltrosYAutorizacion(
                request, pageable, usuarioId, tipoUsuario);
        
        // Mapear a DTOs y construir respuesta
        return construirRespuestaPaginada(paginaCitas, request);
    }
    
    /**
     * Crea la configuración de paginación basada en los parámetros del request.
     */
    private Pageable crearConfiguracionPaginacion(ListarCitasRequestDto request) {
        // Determinar dirección del ordenamiento
        Sort.Direction direction = "asc".equalsIgnoreCase(request.getSortDir()) 
                ? Sort.Direction.ASC 
                : Sort.Direction.DESC;
        
        // Crear ordenamiento
        Sort sort = Sort.by(direction, request.getSortBy());
        
        // Agregar tie-breaker si no se está ordenando por id
        if (!"id".equals(request.getSortBy())) {
            sort = sort.and(Sort.by(Sort.Direction.DESC, "id"));
        }
        
        // Crear configuración de página
        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }
    
    /**
     * Construye la respuesta paginada mapeando las entidades a DTOs.
     */
    private ListarCitasResponseDto construirRespuestaPaginada(
            Page<Cita> paginaCitas, 
            ListarCitasRequestDto request) {
        
        // Mapear citas a DTOs
        List<CitaResumenDto> citasDto = paginaCitas.getContent()
                .stream()
                .map(this::mapearCitaAResumen)
                .collect(Collectors.toList());
        
        // Crear metadatos de paginación
        ListarCitasResponseDto.PaginacionMetadata paginacion = 
                ListarCitasResponseDto.PaginacionMetadata.builder()
                        .paginaActual(paginaCitas.getNumber())
                        .tamañoPagina(paginaCitas.getSize())
                        .totalElementos(paginaCitas.getTotalElements())
                        .totalPaginas(paginaCitas.getTotalPages())
                        .esPrimeraPagina(paginaCitas.isFirst())
                        .esUltimaPagina(paginaCitas.isLast())
                        .tienePaginaAnterior(paginaCitas.hasPrevious())
                        .tienePaginaSiguiente(paginaCitas.hasNext())
                        .elementosEnPagina(paginaCitas.getNumberOfElements())
                        .build();
        
        // Crear información de filtros aplicados
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        ListarCitasResponseDto.FiltrosAplicados filtros = 
                ListarCitasResponseDto.FiltrosAplicados.builder()
                        .medicoId(request.getMedicoId())
                        .pacienteId(request.getPacienteId())
                        .estado(request.getEstado())
                        .desde(request.getDesde() != null ? request.getDesde().format(formatter) : null)
                        .hasta(request.getHasta() != null ? request.getHasta().format(formatter) : null)
                        .ordenadoPor(request.getSortBy())
                        .direccionOrden(request.getSortDir())
                        .build();
        
        // Construir respuesta completa
        return ListarCitasResponseDto.builder()
                .citas(citasDto)
                .paginacion(paginacion)
                .filtros(filtros)
                .build();
    }
    
    /**
     * Mapea una entidad Cita a un DTO CitaResumenDto.
     */
    private CitaResumenDto mapearCitaAResumen(Cita cita) {
        return CitaResumenDto.builder()
                .id(cita.getId())
                .fechaHora(cita.getFechaHora())
                .estado(cita.getEstado())
                .motivo(cita.getMotivo())
                .paciente(CitaResumenDto.PacienteResumenDto.builder()
                        .id(cita.getPaciente().getId())
                        .nombres(cita.getPaciente().getNombres())
                        .apellidos(cita.getPaciente().getApellidos())
                        .tipoDocumento(cita.getPaciente().getTipoDocumento())
                        .numeroDocumento(cita.getPaciente().getNumeroDocumento())
                        .build())
                .medico(CitaResumenDto.MedicoResumenDto.builder()
                        .id(cita.getMedico().getId())
                        .nombres(cita.getMedico().getNombres())
                        .apellidos(cita.getMedico().getApellidos())
                        .numeroDocumento(cita.getMedico().getNumeroDocumento())
                        .especializacion("General") // TODO: obtener de la entidad cuando esté disponible
                        .build())
                .build();
    }
}



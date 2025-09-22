package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.entity.RecetaPrincipal;
import com.uniminuto.clinica.entity.RecetaMedicamento;
import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.entity.Medico;
import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.entity.Medicamento;
import com.uniminuto.clinica.repository.RecetaRepository;
import com.uniminuto.clinica.repository.RecetaPrincipalRepository;
import com.uniminuto.clinica.repository.CitaRepository;
import com.uniminuto.clinica.repository.MedicoRepository;
import com.uniminuto.clinica.repository.MedicamentoRepository;
import com.uniminuto.clinica.service.RecetaService;
import com.uniminuto.clinica.dto.CrearRecetaRequestDto;
import com.uniminuto.clinica.dto.CrearRecetaResponseDto;
import com.uniminuto.clinica.dto.MedicamentoRecetaDto;
import com.uniminuto.clinica.dto.RecetaListadoDto;
import com.uniminuto.clinica.dto.RecetaFiltroDto;
import com.uniminuto.clinica.exception.CitaNoEncontradaException;
import com.uniminuto.clinica.exception.EstadoCitaInvalidoException;
import com.uniminuto.clinica.exception.MedicoNoAutorizadoException;
import com.uniminuto.clinica.exception.RecetaDuplicadaException;
import com.uniminuto.clinica.exception.MedicamentoNoEncontradoException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de negocio para la gestión integral de recetas médicas.
 * 
 * <p>Esta clase implementa toda la lógica de negocio definida en {@link RecetaService},
 * proporcionando funcionalidades completas para la creación, consulta y listado de recetas
 * médicas con validaciones exhaustivas y optimizaciones de rendimiento.</p>
 * 
 * <p>Características de la implementación:</p>
 * <ul>
 *   <li>Transacciones gestionadas para operaciones críticas</li>
 *   <li>Validaciones de integridad referencial</li>
 *   <li>Conversión optimizada entre entidades y DTOs</li>
 *   <li>Manejo de errores con excepciones específicas</li>
 *   <li>Logging para auditoría y depuración</li>
 * </ul>
 * 
 * @author Daniel Donado
 * @version 1.0
 * @since 2024-09-21
 */
@Service
public class RecetaServiceImpl implements RecetaService {

    /**
     * Repositorio para operaciones de persistencia de recetas.
     * Proporciona acceso a consultas básicas y personalizadas.
     */
    @Autowired
    private RecetaRepository recetaRepository;
    
    /**
     * Repositorio para operaciones de persistencia de recetas principales.
     * Maneja la entidad padre de las recetas complejas.
     */
    @Autowired
    private RecetaPrincipalRepository recetaPrincipalRepository;
    
    /**
     * Repositorio para consultas de citas médicas.
     * Utilizado para validaciones de estado y autorización.
     */
    @Autowired
    private CitaRepository citaRepository;
    
    /**
     * Repositorio para consultas de médicos.
     * Utilizado para validaciones de autorización y datos profesionales.
     */
    @Autowired
    private MedicoRepository medicoRepository;
    
    /**
     * Repositorio para consultas de medicamentos.
     * Utilizado para validaciones de existencia y datos farmacológicos.
     */
    @Autowired
    private MedicamentoRepository medicamentoRepository;

    /**
     * {@inheritDoc}
     * 
     * <p>Implementación básica que establece automáticamente el timestamp
     * de creación antes de persistir la entidad.</p>
     */
    @Override
    public Receta crearReceta(Receta receta) {
        receta.setFechaCreacionRegistro(LocalDateTime.now());
        return recetaRepository.save(receta);
    }

    /**
     * {@inheritDoc}
     * 
     * <p><strong>Nota:</strong> Este método está marcado como deprecated
     * en la interfaz del servicio debido a problemas de rendimiento con
     * grandes volúmenes de datos.</p>
     */
    @Override
    public List<Receta> listarRecetas() {
        return recetaRepository.findAllByOrderByFechaCreacionRegistroDesc();
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>Implementación transaccional que garantiza la consistencia de datos
     * durante todo el proceso de creación de recetas complejas.</p>
     */
    @Override
    @Transactional
    public CrearRecetaResponseDto crearRecetaCompleta(CrearRecetaRequestDto request, Long medicoId) {
        // 1. Validar que la cita existe
        Optional<Cita> citaOpt = citaRepository.findById(request.getCitaId());
        if (!citaOpt.isPresent()) {
            throw new CitaNoEncontradaException(request.getCitaId());
        }
        
        Cita cita = citaOpt.get();
        
        // 2. Validar estado de la cita
        if (!validarEstadoCitaParaReceta(request.getCitaId())) {
            throw new EstadoCitaInvalidoException(request.getCitaId(), cita.getEstado());
        }
        
        // 3. Validar autorización del médico
        if (!verificarAutorizacionMedico(request.getCitaId(), medicoId)) {
            throw new MedicoNoAutorizadoException(medicoId, request.getCitaId());
        }
        
        // 4. Verificar si ya existe receta activa (opcional según reglas de negocio)
        if (recetaPrincipalRepository.existeRecetaActivaParaCita(request.getCitaId())) {
            throw new RecetaDuplicadaException(request.getCitaId());
        }
        
        // 5. Crear la receta principal
        RecetaPrincipal recetaPrincipal = RecetaPrincipal.builder()
                .cita(cita)
                .medico(cita.getMedico())
                .paciente(cita.getPaciente())
                .fechaCreacion(LocalDateTime.now())
                .estado("ACTIVA")
                .observaciones(request.getObservaciones())
                .indicacionesGenerales(request.getIndicacionesGenerales())
                .duracionTotalTratamiento(request.getDuracionTotalTratamiento())
                .usuarioCreacion("MEDICO_" + medicoId)
                .fechaModificacion(LocalDateTime.now())
                .build();
        
        // 6. Guardar la receta principal
        recetaPrincipal = recetaPrincipalRepository.save(recetaPrincipal);
        
        // 7. Crear y guardar los medicamentos asociados
        List<RecetaMedicamento> medicamentos = new ArrayList<>();
        final RecetaPrincipal recetaFinal = recetaPrincipal;
        
        IntStream.range(0, request.getMedicamentos().size())
                .forEach(i -> {
                    MedicamentoRecetaDto medDto = request.getMedicamentos().get(i);
                    
                    // Buscar medicamento por ID o crear entrada libre
                    Medicamento medicamento = null;
                    if (medDto.getMedicamentoId() != null) {
                        medicamento = medicamentoRepository.findById(medDto.getMedicamentoId())
                                .orElseThrow(() -> MedicamentoNoEncontradoException.porNombre(medDto.getNombre()));
                    }
                    
                    RecetaMedicamento recetaMedicamento = RecetaMedicamento.builder()
                            .recetaPrincipal(recetaFinal)
                            .medicamento(medicamento)
                            .nombreMedicamento(medDto.getNombre())
                            .dosis(medDto.getDosis())
                            .frecuencia(medDto.getFrecuencia())
                            .duracionDias(medDto.getDuracionDias())
                            .indicaciones(medDto.getIndicaciones())
                            .orden(i + 1)
                            .build();
                    
                    medicamentos.add(recetaMedicamento);
                });
        
        recetaPrincipal.setMedicamentos(medicamentos);
        
        // 8. Construir y retornar respuesta
        return construirRespuestaReceta(recetaPrincipal, request.getMedicamentos());
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean validarEstadoCitaParaReceta(Long citaId) {
        Optional<Cita> citaOpt = citaRepository.findById(citaId);
        if (!citaOpt.isPresent()) {
            return false;
        }
        
        String estadoCita = citaOpt.get().getEstado();
        // Solo se pueden crear recetas para citas finalizadas o en curso
        return "FINALIZADA".equals(estadoCita) || "EN_CURSO".equals(estadoCita);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean verificarAutorizacionMedico(Long citaId, Long medicoId) {
        Optional<Cita> citaOpt = citaRepository.findById(citaId);
        if (!citaOpt.isPresent()) {
            return false;
        }
        
        return citaOpt.get().getMedico().getId().equals(medicoId);
    }
    
    /**
     * Construye la respuesta DTO con la información completa de la receta creada.
     */
    private CrearRecetaResponseDto construirRespuestaReceta(RecetaPrincipal receta, List<MedicamentoRecetaDto> medicamentosDto) {
        // Información del médico
        CrearRecetaResponseDto.MedicoInfoDto medicoInfo = CrearRecetaResponseDto.MedicoInfoDto.builder()
                .id(receta.getMedico().getId())
                .nombres(receta.getMedico().getNombres())
                .apellidos(receta.getMedico().getApellidos())
                .especialidad(receta.getMedico().getEspecializacion() != null ? 
                            receta.getMedico().getEspecializacion().getNombre() : "No especificada")
                .numeroLicencia(receta.getMedico().getRegistroProfesional())
                .build();
        
        // Información del paciente
        CrearRecetaResponseDto.PacienteInfoDto pacienteInfo = CrearRecetaResponseDto.PacienteInfoDto.builder()
                .id(receta.getPaciente().getId())
                .nombres(receta.getPaciente().getNombres())
                .apellidos(receta.getPaciente().getApellidos())
                .numeroDocumento(receta.getPaciente().getNumeroDocumento())
                .tipoDocumento(receta.getPaciente().getTipoDocumento())
                .build();
        
        return CrearRecetaResponseDto.builder()
                .recetaId(receta.getId())
                .citaId(receta.getCita().getId())
                .fechaCreacion(receta.getFechaCreacion())
                .estado(receta.getEstado())
                .medico(medicoInfo)
                .paciente(pacienteInfo)
                .medicamentos(medicamentosDto)
                .observaciones(receta.getObservaciones())
                .indicacionesGenerales(receta.getIndicacionesGenerales())
                .duracionTotalTratamiento(receta.getDuracionTotalTratamiento())
                .mensaje("Receta creada exitosamente")
                .build();
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>Implementación que construye dinámicamente los parámetros de paginación
     * y ordenamiento, ejecuta la consulta optimizada con JOIN FETCH y convierte
     * los resultados a DTOs de listado.</p>
     * 
     * <p>Proceso de ejecución:</p>
     * <ol>
     *   <li>Construcción del objeto Pageable con ordenamiento dinámico</li>
     *   <li>Ejecución de consulta parametrizada con filtros opcionales</li>
     *   <li>Conversión de entidades a DTOs optimizados</li>
     * </ol>
     */
    @Override
    public Page<RecetaListadoDto> listarRecetasConFiltros(RecetaFiltroDto filtros) {
        // Crear objeto Pageable para paginación y ordenamiento
        Sort sort = Sort.by(
            "DESC".equalsIgnoreCase(filtros.getDireccion()) ? 
                Sort.Direction.DESC : Sort.Direction.ASC,
            filtros.getOrdenarPor()
        );
        
        Pageable pageable = PageRequest.of(filtros.getPagina(), filtros.getTamano(), sort);
        
        // Ejecutar consulta con filtros
        Page<Receta> recetasPage = recetaRepository.findRecetasConFiltros(
            filtros.getPacienteId(),
            filtros.getMedicoId(),
            filtros.getMedicamentoId(),
            filtros.getFechaDesde(),
            filtros.getFechaHasta(),
            filtros.getTexto(),
            pageable
        );
        
        // Convertir a DTOs
        return recetasPage.map(this::convertirARecetaListadoDto);
    }
    
    /** {@inheritDoc} */
    @Override
    public Page<RecetaListadoDto> listarRecetasPorPaciente(Long pacienteId, int pagina, int tamano) {
        Pageable pageable = PageRequest.of(pagina, tamano, 
            Sort.by(Sort.Direction.DESC, "fechaCreacionRegistro"));
        
        Page<Receta> recetasPage = recetaRepository.findByPacienteId(pacienteId, pageable);
        
        return recetasPage.map(this::convertirARecetaListadoDto);
    }
    
    /** {@inheritDoc} */
    @Override
    public Page<RecetaListadoDto> listarRecetasPorMedico(Long medicoId, int pagina, int tamano) {
        Pageable pageable = PageRequest.of(pagina, tamano, 
            Sort.by(Sort.Direction.DESC, "fechaCreacionRegistro"));
        
        Page<Receta> recetasPage = recetaRepository.findByMedicoId(medicoId, pageable);
        
        return recetasPage.map(this::convertirARecetaListadoDto);
    }
    
    /**
     * Convierte una entidad Receta a RecetaListadoDto.
     * 
     * @param receta Entidad a convertir
     * @return DTO con información de la receta
     */
    private RecetaListadoDto convertirARecetaListadoDto(Receta receta) {
        // Información del paciente
        RecetaListadoDto.PacienteResumenDto pacienteDto = null;
        if (receta.getCita() != null && receta.getCita().getPaciente() != null) {
            Paciente paciente = receta.getCita().getPaciente();
            pacienteDto = RecetaListadoDto.PacienteResumenDto.builder()
                    .id(paciente.getId())
                    .nombre(paciente.getNombres() + " " + paciente.getApellidos())
                    .documento(paciente.getNumeroDocumento())
                    .email("") // No hay email en la entidad Paciente actual
                    .build();
        }
        
        // Información del médico
        RecetaListadoDto.MedicoResumenDto medicoDto = null;
        if (receta.getCita() != null && receta.getCita().getMedico() != null) {
            Medico medico = receta.getCita().getMedico();
            String especializacionNombre = "";
            if (medico.getEspecializacion() != null) {
                especializacionNombre = medico.getEspecializacion().getNombre();
            }
            
            medicoDto = RecetaListadoDto.MedicoResumenDto.builder()
                    .id(medico.getId())
                    .nombre(medico.getNombres() + " " + medico.getApellidos())
                    .especializacion(especializacionNombre)
                    .registroProfesional(medico.getRegistroProfesional())
                    .build();
        }
        
        // Información del medicamento
        RecetaListadoDto.MedicamentoResumenDto medicamentoDto = null;
        if (receta.getMedicamento() != null) {
            Medicamento medicamento = receta.getMedicamento();
            medicamentoDto = RecetaListadoDto.MedicamentoResumenDto.builder()
                    .id(medicamento.getId())
                    .nombre(medicamento.getNombre())
                    .principioActivo("") // No hay principio activo en la entidad actual
                    .descripcion(medicamento.getDescripcion())
                    .build();
        }
        
        // Información de la cita
        RecetaListadoDto.CitaResumenDto citaDto = null;
        if (receta.getCita() != null) {
            Cita cita = receta.getCita();
            citaDto = RecetaListadoDto.CitaResumenDto.builder()
                    .id(cita.getId())
                    .fechaHora(cita.getFechaHora())
                    .estado(cita.getEstado())
                    .motivo(cita.getMotivo())
                    .build();
        }
        
        return RecetaListadoDto.builder()
                .id(receta.getId())
                .fechaCreacionRegistro(receta.getFechaCreacionRegistro())
                .dosis(receta.getDosis())
                .indicaciones(receta.getIndicaciones())
                .paciente(pacienteDto)
                .medico(medicoDto)
                .medicamento(medicamentoDto)
                .cita(citaDto)
                .build();
    }
}



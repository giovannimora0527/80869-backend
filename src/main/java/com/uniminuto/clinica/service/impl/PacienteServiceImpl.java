package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.model.PacienteRq;
import com.uniminuto.clinica.model.PacienteRs;
import com.uniminuto.clinica.repository.PacienteRepository;
import com.uniminuto.clinica.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.coyote.BadRequestException;

@Service
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    @Autowired
    public PacienteServiceImpl(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public List<PacienteRs> obtenerTodos() {
        return pacienteRepository.findAll().stream()
                .map(this::mapToPacienteRs)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PacienteRs> buscarPorDocumento(String documento) {
        return pacienteRepository.findByNumeroDocumento(documento)
                .map(this::mapToPacienteRs);
    }

    @Override
    public PacienteRs guardar(PacienteRq pacienteRq) throws BadRequestException {
        // Asegúrate de que el documento no exista
        if (pacienteRepository.findByNumeroDocumento(pacienteRq.getNumeroDocumento()).isPresent()) {
            throw new BadRequestException("El número de documento ya existe.");
        }

        // Mapeamos el DTO de entrada a la entidad
        Paciente paciente = mapToPaciente(pacienteRq);

        // Guardamos la entidad
        Paciente pacienteGuardado = pacienteRepository.save(paciente);

        // Devolvemos el DTO de respuesta
        return mapToPacienteRs(pacienteGuardado);
    }

    // ✅ NUEVO MÉTODO AGREGADO - Para actualizar pacientes
    @Override
    public PacienteRs actualizar(Long id, PacienteRq pacienteRq) throws BadRequestException {
        Optional<Paciente> pacienteExistente = pacienteRepository.findById(id);

        if (pacienteExistente.isEmpty()) {
            throw new BadRequestException("Paciente no encontrado con ID: " + id);
        }

        Paciente paciente = pacienteExistente.get();

        // Verificar si se está cambiando el documento y si ya existe en otro paciente
        if (!paciente.getNumeroDocumento().equals(pacienteRq.getNumeroDocumento())) {
            Optional<Paciente> pacienteConMismoDocumento = pacienteRepository.findByNumeroDocumento(pacienteRq.getNumeroDocumento());
            if (pacienteConMismoDocumento.isPresent() && !pacienteConMismoDocumento.get().getId().equals(id)) {
                throw new BadRequestException("El número de documento ya existe en otro paciente.");
            }
        }

        // Actualizar los campos
        paciente.setNombres(pacienteRq.getNombres());
        paciente.setApellidos(pacienteRq.getApellidos());
        paciente.setNumeroDocumento(pacienteRq.getNumeroDocumento());
        paciente.setTipoDocumento(pacienteRq.getTipoDocumento());
        paciente.setFechaNacimiento(pacienteRq.getFechaNacimiento().toLocalDate());
        paciente.setGenero(pacienteRq.getGenero().charAt(0));
        paciente.setTelefono(pacienteRq.getTelefono());
        paciente.setDireccion(pacienteRq.getDireccion());

        // Guardar y retornar
        Paciente pacienteActualizado = pacienteRepository.save(paciente);
        return mapToPacienteRs(pacienteActualizado);
    }

    @Override
    public void eliminar(Long id) {
        pacienteRepository.deleteById(id);
    }

    @Override
    public List<PacienteRs> obtenerPacientesOrdenadosPorFechaNacimiento() {
        return pacienteRepository.findAllByOrderByFechaNacimientoDesc().stream()
                .map(this::mapToPacienteRs)
                .collect(Collectors.toList());
    }

    // --- Métodos de mapeo (transformación) ---

    /**
     * Convierte una entidad Paciente a un DTO PacienteRs.
     * Este método se usa en los métodos GET.
     */
    private PacienteRs mapToPacienteRs(Paciente paciente) {
        PacienteRs dto = new PacienteRs();
        dto.setId(paciente.getId());
        dto.setNombres(paciente.getNombres());
        dto.setApellidos(paciente.getApellidos());
        dto.setNumeroDocumento(paciente.getNumeroDocumento());
        dto.setTipoDocumento(paciente.getTipoDocumento());
        dto.setFechaNacimiento(paciente.getFechaNacimiento().atStartOfDay());
        dto.setGenero(String.valueOf(paciente.getGenero()));
        dto.setTelefono(paciente.getTelefono());
        dto.setDireccion(paciente.getDireccion());
        return dto;
    }

    /**
     * Convierte un DTO PacienteRq a una entidad Paciente.
     * Este método se usa en el método guardar.
     */
    private Paciente mapToPaciente(PacienteRq dto) {
        Paciente paciente = new Paciente();
        paciente.setNombres(dto.getNombres());
        paciente.setApellidos(dto.getApellidos());
        paciente.setNumeroDocumento(dto.getNumeroDocumento());
        paciente.setTipoDocumento(dto.getTipoDocumento());
        paciente.setFechaNacimiento(dto.getFechaNacimiento().toLocalDate());
        paciente.setGenero(dto.getGenero().charAt(0));
        paciente.setTelefono(dto.getTelefono());
        paciente.setDireccion(dto.getDireccion());
        return paciente;
    }
}
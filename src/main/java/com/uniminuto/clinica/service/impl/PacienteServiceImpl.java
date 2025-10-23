package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.model.PacienteRq;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.repository.PacienteRepository;
import com.uniminuto.clinica.service.PacienteService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Override
    public List<Paciente> listarPacientes() {
        return this.pacienteRepository.findAll();
    }

    @Override
    public Paciente buscarPorDocumento(String documento) throws BadRequestException {
        return this.pacienteRepository.findByNumeroDocumento(documento)
                .orElseThrow(() -> new BadRequestException("No se encontró el paciente con ese documento"));
    }

    @Override
    public List<Paciente> listarPacientesOrdenadoPorFechaNacimiento() {
        return this.pacienteRepository.findAllByOrderByFechaNacimientoAsc();
    }

    @Override
    public RespuestaRs guardarPaciente(PacienteRq pacienteRq) throws BadRequestException {
        validarFormulario(pacienteRq);

        Optional<Paciente> opt = pacienteRepository.findByNumeroDocumento(pacienteRq.getNumeroDocumento());
        if (opt.isPresent()) {
            throw new BadRequestException("Existe un paciente con el mismo número de documento");
        }

        Paciente nuevo = convertirAPaciente(pacienteRq);
        pacienteRepository.save(nuevo);

        RespuestaRs rta = new RespuestaRs();
        rta.setStatus(200);
        rta.setMensaje("Paciente creado exitosamente");
        return rta;
    }

    @Override
    public RespuestaRs actualizarPaciente(PacienteRq pacienteRq) throws BadRequestException {
        if (pacienteRq.getId() == null) {
            throw new BadRequestException("El id del paciente es obligatorio");
        }

        Paciente actual = pacienteRepository.findById(pacienteRq.getId())
                .orElseThrow(() -> new BadRequestException("No se puede actualizar el paciente porque no existe."));

        if (pacienteRq.getNumeroDocumento() != null &&
                !pacienteRq.getNumeroDocumento().equals(actual.getNumeroDocumento())) {
            pacienteRepository.findByNumeroDocumento(pacienteRq.getNumeroDocumento())
                    .ifPresent(p -> {
                        try {
                            throw new BadRequestException("Ya existe un paciente con ese número de documento");
                        } catch (BadRequestException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        // Actualizamos campos si vienen en la request
        actual.setUsuarioId(pacienteRq.getUsuarioId() != null ? pacienteRq.getUsuarioId() : actual.getUsuarioId());
        actual.setTipoDocumento(pacienteRq.getTipoDocumento() != null ? pacienteRq.getTipoDocumento() : actual.getTipoDocumento());
        actual.setNumeroDocumento(pacienteRq.getNumeroDocumento() != null ? pacienteRq.getNumeroDocumento() : actual.getNumeroDocumento());
        actual.setNombres(pacienteRq.getNombres() != null ? pacienteRq.getNombres() : actual.getNombres());
        actual.setApellidos(pacienteRq.getApellidos() != null ? pacienteRq.getApellidos() : actual.getApellidos());
        actual.setFechaNacimiento(pacienteRq.getFechaNacimiento() != null ? pacienteRq.getFechaNacimiento() : actual.getFechaNacimiento());
        actual.setGenero(pacienteRq.getGenero() != null ? pacienteRq.getGenero() : actual.getGenero());
        actual.setTelefono(pacienteRq.getTelefono() != null ? pacienteRq.getTelefono() : actual.getTelefono());
        actual.setDireccion(pacienteRq.getDireccion() != null ? pacienteRq.getDireccion() : actual.getDireccion());

        pacienteRepository.save(actual);

        RespuestaRs rta = new RespuestaRs();
        rta.setStatus(200);
        rta.setMensaje("Paciente actualizado correctamente");
        return rta;
    }

    private Paciente convertirAPaciente(PacienteRq rq) {
        Paciente p = new Paciente();
        p.setUsuarioId(rq.getUsuarioId());
        p.setTipoDocumento(rq.getTipoDocumento());
        p.setNumeroDocumento(rq.getNumeroDocumento());
        p.setNombres(rq.getNombres());
        p.setApellidos(rq.getApellidos());
        p.setFechaNacimiento(rq.getFechaNacimiento());
        p.setGenero(rq.getGenero());
        p.setTelefono(rq.getTelefono());
        p.setDireccion(rq.getDireccion());
        return p;
    }

    private void validarFormulario(PacienteRq rq) throws BadRequestException {
        if (rq.getNumeroDocumento() == null || rq.getNumeroDocumento().isEmpty()) {
            throw new BadRequestException("El número de documento es obligatorio");
        }
        if (rq.getNombres() == null || rq.getNombres().isEmpty()) {
            throw new BadRequestException("El campo nombres es obligatorio");
        }
        if (rq.getApellidos() == null || rq.getApellidos().isEmpty()) {
            throw new BadRequestException("El campo apellidos es obligatorio");
        }
    }
}

package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.entity.Medico;
import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.model.CitaRq;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.repository.CitaRepository;
import com.uniminuto.clinica.repository.MedicoRepository;
import com.uniminuto.clinica.repository.PacienteRepository;
import com.uniminuto.clinica.service.CitaService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Override
    public List<Cita> listarCitas() {
        return this.citaRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Cita::getFechaHora).reversed())
                .toList();
    }

    @Override
    public RespuestaRs guardarCita(CitaRq citaRq) throws BadRequestException {
        this.validarFormulario(citaRq);

        // validar existencia de paciente
        Optional<Paciente> optPaciente = this.pacienteRepository.findById(citaRq.getPacienteId().longValue());
        if (!optPaciente.isPresent()) {
            throw new BadRequestException("Paciente no encontrado");
        }

        // validar existencia de medico
        Optional<Medico> optMedico = this.medicoRepository.findById(citaRq.getMedicoId().longValue());
        if (!optMedico.isPresent()) {
            throw new BadRequestException("Medico no encontrado");
        }

        Cita nuevo = convertirACita(citaRq, optPaciente.get(), optMedico.get());
        this.citaRepository.save(nuevo);

        RespuestaRs rta = new RespuestaRs();
        rta.setStatus(200);
        rta.setMensaje("Cita creada exitosamente");
        return rta;
    }

    @Override
    public RespuestaRs actualizarCita(CitaRq citaRq) throws BadRequestException {
        if (citaRq.getId() == null) {
            throw new BadRequestException("El id de la cita es obligatorio");
        }

        Optional<Cita> optCita = this.citaRepository.findById(citaRq.getId());
        if (!optCita.isPresent()) {
            throw new BadRequestException("No se puede actualizar la cita porque no existe.");
        }

        Cita citaActual = optCita.get();

        // Si cambian las referencias a paciente o medico, validar existencia
        if (citaRq.getPacienteId() != null &&
                !citaActual.getPaciente().getId().equals(citaRq.getPacienteId().longValue())) {
            Optional<Paciente> optPaciente = this.pacienteRepository.findById(citaRq.getPacienteId().longValue());
            if (!optPaciente.isPresent()) {
                throw new BadRequestException("Paciente no encontrado");
            }
            citaActual.setPaciente(optPaciente.get());
        }

        if (citaRq.getMedicoId() != null &&
                !citaActual.getMedico().getId().equals(citaRq.getMedicoId().longValue())) {
            Optional<Medico> optMedico = this.medicoRepository.findById(citaRq.getMedicoId().longValue());
            if (!optMedico.isPresent()) {
                throw new BadRequestException("Medico no encontrado");
            }
            citaActual.setMedico(optMedico.get());
        }

        citaActual.setFechaHora(citaRq.getFechaHora() == null ? citaActual.getFechaHora() : citaRq.getFechaHora());
        citaActual.setEstado(citaRq.getEstado() == null ? citaActual.getEstado() : citaRq.getEstado());
        citaActual.setMotivo(citaRq.getMotivo() == null ? citaActual.getMotivo() : citaRq.getMotivo());

        this.citaRepository.save(citaActual);

        RespuestaRs rta = new RespuestaRs();
        rta.setMensaje("Se ha actualizado el registro satisfactoriamente");
        rta.setStatus(200);
        return rta;
    }

    private Cita convertirACita(CitaRq rq, Paciente paciente, Medico medico) {
        Cita c = new Cita();
        c.setPaciente(paciente);
        c.setMedico(medico);
        c.setFechaHora(rq.getFechaHora());
        c.setEstado(rq.getEstado());
        c.setMotivo(rq.getMotivo());
        return c;
    }

    private void validarFormulario(CitaRq rq) throws BadRequestException {
        if (rq.getPacienteId() == null) {
            throw new BadRequestException("El campo pacienteId es obligatorio");
        }
        if (rq.getMedicoId() == null) {
            throw new BadRequestException("El campo medicoId es obligatorio");
        }
        if (rq.getFechaHora() == null) {
            throw new BadRequestException("El campo fechaHora es obligatorio");
        }
        if (rq.getEstado() == null || rq.getEstado().isEmpty()) {
            throw new BadRequestException("El campo estado es obligatorio");
        }
    }
}

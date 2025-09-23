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

import java.time.LocalDateTime;
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
    public List<Cita> obtenerTodasLasCitas() {
        return this.citaRepository.findAllByOrderByFechaDesc();
    }

    @Override
    public RespuestaRs guardarCita(CitaRq citaRq) throws BadRequestException {
        Optional<Paciente> optPaciente = this.pacienteRepository.findById(citaRq.getPacienteId());
        if (optPaciente.isEmpty()) {
            throw new BadRequestException("El paciente con ID " + citaRq.getPacienteId() + " no existe");
        }
        Optional<Medico> optMedico = this.medicoRepository.findById(citaRq.getMedicoId());
        if (optMedico.isEmpty()) {
            throw new BadRequestException("El medico con ID " + citaRq.getPacienteId() + " no existe");
        }

        LocalDateTime fechaInicioCita = LocalDateTime.parse(citaRq.getFechaHora());
        LocalDateTime fechaFinCita = fechaInicioCita.plusMinutes(15);
        List<Cita> citasExistentesMedico = this.citaRepository.findByMedicoAndFechaHoraBetween(
                optMedico.get(), fechaInicioCita, fechaFinCita);
        if (citasExistentesMedico != null && !citasExistentesMedico.isEmpty()) {
            throw new BadRequestException("El medico con ID " + citaRq.getMedicoId() +
                    " ya tiene una cita agendada en el horario solicitado");
        }

        List<Cita> citasExistentesPaciente = this.citaRepository.findByPacienteAndFechaHoraBetween(
                optPaciente.get(), fechaInicioCita, fechaFinCita);
        if (citasExistentesPaciente.isEmpty()) {
            throw new BadRequestException("El paciente con ID " + citaRq.getPacienteId() +
                    " ya tiene una cita agendada en el horario solicitado");
        }

        Cita citaAGuardar = this.converterCitaRqToCita(citaRq, optPaciente.get(), optMedico.get());
        this.citaRepository.save(citaAGuardar);
        RespuestaRs respuesta = new RespuestaRs();
        respuesta.setStatus(200);
        respuesta.setMensaje("Cita creada exitosamente");
        return respuesta;
    }


    private Cita converterCitaRqToCita(CitaRq citaRq, Paciente paciente, Medico medico) {
        Cita cita = new Cita();
        cita.setFechaHora(LocalDateTime.parse(citaRq.getFechaHora()));
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setEstado("Programada");
        cita.setMotivo(citaRq.getMotivo());
        return cita;
    }

}

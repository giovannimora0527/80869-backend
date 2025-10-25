package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.entity.Medico;
import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.model.CitaRq;
import com.uniminuto.clinica.model.CitaRs;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.repository.CitaRepository;
import com.uniminuto.clinica.repository.MedicoRepository;
import com.uniminuto.clinica.repository.PacienteRepository;
import com.uniminuto.clinica.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.coyote.BadRequestException;
import java.time.LocalDateTime;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Override
    public RespuestaRs guardarCita(CitaRq citaNueva) throws BadRequestException {
        this.validarCampos(citaNueva);

        Optional<Paciente> paciente = this.pacienteRepository.findById(citaNueva.getPacienteId());
        if (!paciente.isPresent()) {
            throw new BadRequestException("No existe el paciente con el ID proporcionado.");
        }

        Optional<Medico> medico = this.medicoRepository.findById(citaNueva.getMedicoId());
        if (!medico.isPresent()) {
            throw new BadRequestException("No existe el médico con el ID proporcionado.");
        }

        if (citaNueva.getId() != null) {
            Optional<Cita> citaExistente = this.citaRepository.findById(citaNueva.getId());
            if (!citaExistente.isPresent()) {
                throw new BadRequestException("No se encontró la cita con el ID proporcionado para actualizar.");
            }

            List<Cita> citasDuplicadas = this.citaRepository.findByMedicoIdAndFechaHoraAndIdNot(
                    citaNueva.getMedicoId(),
                    citaNueva.getFechaHora(),
                    citaNueva.getId()
            );

            if (!citasDuplicadas.isEmpty()) {
                throw new BadRequestException("Ya existe una cita agendada para este médico en la fecha y hora seleccionada.");
            }

            // Actualizar todos los campos de la cita existente
            Cita citaActualizada = citaExistente.get();
            citaActualizada.setMedico(medico.get()); // Actualiza el médico
            citaActualizada.setFechaHora(citaNueva.getFechaHora()); // Actualiza la fecha y hora
            citaActualizada.setMotivo(citaNueva.getMotivo()); // Actualiza el motivo
            citaActualizada.setEstado(citaNueva.getEstado().toUpperCase()); // Actualiza el estado
            this.citaRepository.save(citaActualizada);

            RespuestaRs rta = new RespuestaRs();
            rta.setMensaje("La cita se ha actualizado correctamente.");
            rta.setStatus(200);
            return rta;
        }

        List<Cita> citasExistentes = this.citaRepository.findByMedicoIdAndFechaHora(
                citaNueva.getMedicoId(),
                citaNueva.getFechaHora()
        );

        if (!citasExistentes.isEmpty()) {
            throw new BadRequestException("Ya existe una cita agendada para este médico en la fecha y hora seleccionada.");
        }

        Cita nuevaCita = new Cita();
        nuevaCita.setPaciente(paciente.get());
        nuevaCita.setMedico(medico.get());
        nuevaCita.setFechaHora(citaNueva.getFechaHora());
        nuevaCita.setEstado(citaNueva.getEstado().toUpperCase());
        nuevaCita.setMotivo(citaNueva.getMotivo());

        this.citaRepository.save(nuevaCita);

        RespuestaRs rta = new RespuestaRs();
        rta.setMensaje("La cita se ha guardado correctamente.");
        rta.setStatus(200);
        return rta;
    }

    private void validarCampos(CitaRq citaNueva) throws BadRequestException {
        if (citaNueva.getPacienteId() == null) {
            throw new BadRequestException("El campo 'pacienteId' es obligatorio.");
        }
        if (citaNueva.getMedicoId() == null) {
            throw new BadRequestException("El campo 'medicoId' es obligatorio.");
        }
        if (citaNueva.getFechaHora() == null) {
            throw new BadRequestException("El campo 'fechaHora' es obligatorio.");
        }
        if (citaNueva.getEstado() == null || citaNueva.getEstado().isBlank()) {
            throw new BadRequestException("El campo 'estado' es obligatorio.");
        }
    }

    @Override
    public List<CitaRs> listarCitasRecientes() {
        // Recupera las citas ordenadas por fecha y hora descendente
        List<Cita> citas = citaRepository.findAllByOrderByFechaHoraDesc();
        return citas.stream()
                .map(this::mapToCitaRs)
                .collect(Collectors.toList());
    }

    private CitaRs mapToCitaRs(Cita cita) {
        CitaRs dto = new CitaRs();
        dto.setId(cita.getId());
        dto.setFechaHora(cita.getFechaHora());
        dto.setEstado(cita.getEstado());
        dto.setMotivo(cita.getMotivo());
        if (cita.getPaciente() != null) {
            dto.setPacienteId(cita.getPaciente().getId());
            dto.setNombreCompletoPaciente(cita.getPaciente().getNombres() + " " + cita.getPaciente().getApellidos());
        }
        if (cita.getMedico() != null) {
            dto.setMedicoId(cita.getMedico().getId());
            dto.setNombreCompletoMedico(cita.getMedico().getNombres() + " " + cita.getMedico().getApellidos());
        }
        return dto;
    }
}
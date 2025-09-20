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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public RespuestaRs guardarCita(CitaRq citaRq) {
        RespuestaRs respuesta = new RespuestaRs();

        // Validación de campos obligatorios
        if (citaRq.getPacienteId() == null) {
            return construirRespuestaError("❌ El campo pacienteId es obligatorio", 400);
        }
        if (citaRq.getMedicoId() == null) {
            return construirRespuestaError("❌ El campo medicoId es obligatorio", 400);
        }
        if (citaRq.getFechaHora() == null) {
            return construirRespuestaError("❌ El campo fechaHora es obligatorio", 400);
        }
        if (citaRq.getEstado() == null || citaRq.getEstado().isEmpty()) {
            return construirRespuestaError("❌ El campo estado es obligatorio", 400);
        }

        // Validar paciente
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(citaRq.getPacienteId());
        if (pacienteOpt.isEmpty()) {
            return construirRespuestaError("❌ No existe el paciente con id " + citaRq.getPacienteId(), 404);
        }

        // Validar médico
        Optional<Medico> medicoOpt = medicoRepository.findById(citaRq.getMedicoId());
        if (medicoOpt.isEmpty()) {
            return construirRespuestaError("❌ No existe el médico con id " + citaRq.getMedicoId(), 404);
        }

        // Crear y guardar cita
        Cita cita = new Cita();
        cita.setPaciente(pacienteOpt.get());
        cita.setMedico(medicoOpt.get());
        cita.setFechahora(citaRq.getFechaHora());
        cita.setEstado(citaRq.getEstado());
        cita.setMotivo(citaRq.getMotivo());

        citaRepository.save(cita);

        // Respuesta exitosa
        RespuestaRs respuestaOk = new RespuestaRs();
        respuestaOk.setMensaje("✅ Cita guardada con éxito");
        respuestaOk.setEstaFuncionando(true);
        respuestaOk.setStatus(201);
        return respuestaOk;
    }

    @Override
    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    @Override
    public List<Cita> listarCitasFechaReciente() {
        return citaRepository.findAllByOrderByFechahoraDesc();
    }

    // Método privado para construir respuestas de error
    private RespuestaRs construirRespuestaError(String mensaje, int status) {
        RespuestaRs respuesta = new RespuestaRs();
        respuesta.setMensaje(mensaje);
        respuesta.setEstaFuncionando(false);
        respuesta.setStatus(status);
        return respuesta;
    }
}

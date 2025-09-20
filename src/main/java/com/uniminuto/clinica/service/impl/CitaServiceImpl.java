package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.model.CitaCrearRq;
import com.uniminuto.clinica.repository.CitaRepository;
import com.uniminuto.clinica.service.CitaService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CitaServiceImpl implements CitaService {

    private final CitaRepository repo;

    public CitaServiceImpl(CitaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Cita crear(CitaCrearRq rq) {
        if (rq.getPacienteId() == null || rq.getMedicoId() == null
                || rq.getFechaHora() == null || rq.getEstado() == null) {
            throw new IllegalArgumentException("Datos obligatorios: pacienteId, medicoId, fechaHora, estado");
        }
        Cita c = new Cita();
        c.setPacienteId(rq.getPacienteId());
        c.setMedicoId(rq.getMedicoId());
        c.setFechaHora(Timestamp.valueOf(rq.getFechaHora())); // "YYYY-MM-DD HH:MM:SS"
        c.setEstado(rq.getEstado());
        c.setMotivo(rq.getMotivo());
        return repo.save(c);
    }

    @Override
    public List<Cita> listarRecientes() {
        return repo.findAllByOrderByFechaHoraDesc();
    }
}
package com.uniminuto.clinica.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uniminuto.clinica.service.PacienteService;
import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.repository.PacienteRepository;

@Service
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteServiceImpl(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public List<Paciente> findAllPacientes() {
        return pacienteRepository.findAll();
    }

    @Override
    public Paciente savePaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

}

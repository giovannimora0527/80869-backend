package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    public List<Paciente> listarPacientes() {
        return repository.findAll();
    }

    public Optional<Paciente> buscarPorDocumento(String documento) {
        return repository.findByDocumentoIdentidad(documento);
    }
}

package com.uniminuto.clinica.service;

import java.util.List;

import com.uniminuto.clinica.entity.Paciente;

public interface PacienteService {
    List<Paciente> findAllPacientes();
    Paciente savePaciente(Paciente paciente);

public Optional<Paciente> buscarPorDocumento(String documento) {
    return pacienteRepository.findByDocumento(documento);
}

}

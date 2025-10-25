package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Paciente;
import java.util.Optional;

public interface BusPacienteService {
    Optional<Paciente> buscarPorDocumento(String documento);
}
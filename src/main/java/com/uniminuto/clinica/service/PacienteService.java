package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Paciente;
import java.util.List;

public interface PacienteService {
    List<Paciente> listarTodos();

    List<Paciente> listarOrdenNacimientoDesc();
}
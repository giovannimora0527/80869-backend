package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Paciente;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface PacienteService {

    // Retorna todos los pacientes registrados
    List<Paciente> obtenerPacientes();

    // Busca un paciente por su número de documento
    Paciente buscarPorDoc(String nroDocumento) throws BadRequestException;
}

package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Paciente;
import java.util.List;

/**
 *
 * @author Miguel
 */
public interface PacienteService {
    List<Paciente> listarPacientes();
    Paciente buscarPorNumeroDocumento(String numeroDocumento);
    
    /**
     * Lista los pacientes del mayor al menor por fecha de nacimiento.
     * Se asume que el formato de {@code fechaNacimiento} permite el orden cronológico
     * lexicográfico (por ejemplo, yyyy-MM-dd).
     *
     * @return lista de pacientes ordenada por fecha de nacimiento ascendente
     */
    List<Paciente> listarPacientesMayorAMenorPorFechaNacimiento();
    
}

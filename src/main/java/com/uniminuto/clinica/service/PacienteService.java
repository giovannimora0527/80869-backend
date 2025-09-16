package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Paciente;
import java.util.List;
import org.apache.coyote.BadRequestException;

/**
 *
 * @author lmora
 */
public interface PacienteService {
    
    List<Paciente> listarPacientes();
    
    Paciente buscarPorDocumento (String documento) throws BadRequestException;

    List<Paciente> listarPacientesPorEdadMayor();
}

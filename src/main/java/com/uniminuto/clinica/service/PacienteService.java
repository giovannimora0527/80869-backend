package com.uniminuto.clinica.service;


import com.uniminuto.clinica.entity.Paciente;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface PacienteService {
    List<Paciente> listaPacientes();
    Paciente buscarPorDocumento(String documento) throws BadRequestException;
}

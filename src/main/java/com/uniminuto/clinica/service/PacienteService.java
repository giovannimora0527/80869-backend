package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.model.PacienteRq;
import com.uniminuto.clinica.model.RespuestaRs;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface PacienteService {

    List<Paciente> listarPacientes();

    Paciente buscarPorDocumento(String documento) throws BadRequestException;

    List<Paciente> listarPacientesOrdenadoPorFechaNacimiento();

    RespuestaRs guardarPaciente(PacienteRq pacienteRq) throws BadRequestException;

    RespuestaRs actualizarPaciente(PacienteRq pacienteRq) throws BadRequestException;
}
package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.model.CitaRq;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface CitaService {
    List<Cita> listarCitas();

    RespuestaRs guardarCita(CitaRq citaRq) throws BadRequestException;

    RespuestaRs actualizarCita(CitaRq citaRq) throws BadRequestException;
}

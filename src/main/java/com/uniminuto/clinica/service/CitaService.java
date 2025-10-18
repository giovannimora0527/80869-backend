package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.model.CitaRq;
import com.uniminuto.clinica.model.RecetaRq;
import com.uniminuto.clinica.model.RespuestaRs;
import org.apache.coyote.BadRequestException;

import java.util.List;

/**
 *
 * @author lmora
 */
public interface CitaService {
    List<Cita> obtenerTodasLasCitas();
    RespuestaRs guardarCita(CitaRq citaRq) throws BadRequestException;
}

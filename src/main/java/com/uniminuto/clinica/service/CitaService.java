package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.model.CitaRq;
import org.apache.coyote.BadRequestException;

import java.util.List;

/**
 *
 * @author lmora
 */
public interface CitaService {
    String guardarCita(CitaRq cita) throws BadRequestException;
    List<Cita> listarReciente();
    
}

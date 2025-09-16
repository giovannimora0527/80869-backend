package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Cita;
import org.apache.coyote.BadRequestException;

import java.util.List;

/**
 *
 * @author lmora
 */
public interface CitaService {
    String guardarCita(Cita cita) throws BadRequestException;
    List<Cita> listarReciente();
    
}

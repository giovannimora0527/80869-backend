package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.model.RecetaRq;
import com.uniminuto.clinica.model.RecetaRs; 
import com.uniminuto.clinica.model.RespuestaRs;
import java.util.List;
import org.apache.coyote.BadRequestException;

public interface RecetaService {

    RespuestaRs guardarReceta(RecetaRq recetaNueva) throws BadRequestException;

    List<RecetaRs> listarRecetas();
}
package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.model.RecetaRq;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface RecetaService {
    String guardarReceta(RecetaRq receta) throws BadRequestException;
    List<Receta> listarRecetas();
}

package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Receta;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface RecetaService {
    String guardarReceta(Receta receta) throws BadRequestException;
    List<Receta> listarRecetas();
}

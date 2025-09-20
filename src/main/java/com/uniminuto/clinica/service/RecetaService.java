package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.model.RecetaCrearRq;
import java.util.List;

public interface RecetaService {
  Receta crear(RecetaCrearRq rq);
  List<Receta> listar();
}

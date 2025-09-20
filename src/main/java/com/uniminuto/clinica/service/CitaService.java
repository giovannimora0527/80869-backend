package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.model.CitaCrearRq;
import java.util.List;

/** Servicio de gestión de citas. */
public interface CitaService {
  Cita crear(CitaCrearRq rq);
  List<Cita> listarRecientes();
}

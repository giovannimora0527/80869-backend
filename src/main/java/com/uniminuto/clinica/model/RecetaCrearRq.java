package com.uniminuto.clinica.model;

import lombok.Data;

/** Request para crear una receta. */
@Data
public class RecetaCrearRq {
  private Integer citaId;
  private Integer medicamentoId;
  private String dosis;
  private String indicaciones;
}

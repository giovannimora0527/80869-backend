package com.uniminuto.clinica.model;

import lombok.Data;

/** Request para crear una cita. */
@Data
public class CitaCrearRq {
  private Integer pacienteId;
  private Integer medicoId;
  private String fechaHora; // "YYYY-MM-DD HH:MM:SS"
  private String estado;    // PENDIENTE/CONFIRMADA/CANCELADA/REALIZADA
  private String motivo;
}
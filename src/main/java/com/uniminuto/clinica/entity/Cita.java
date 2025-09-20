package com.uniminuto.clinica.entity;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

/** Entidad Cita mapeada a la tabla 'cita'. */
@Data
@Entity @Table(name="cita")
public class Cita {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="paciente_id", nullable=false)
  private Integer pacienteId;

  @Column(name="medico_id", nullable=false)
  private Integer medicoId;

  @Column(name="fecha_hora", nullable=false)
  private Timestamp fechaHora;

  @Column(nullable=false, length=20)
  private String estado; // PENDIENTE/CONFIRMADA/CANCELADA/REALIZADA

  @Column(columnDefinition="TEXT")
  private String motivo;
}
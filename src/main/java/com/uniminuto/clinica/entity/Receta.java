package com.uniminuto.clinica.entity;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

/** Entidad Receta mapeada a 'receta'. */
@Data
@Entity @Table(name="receta")
public class Receta {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="cita_id", nullable=false)
  private Integer citaId;

  @Column(name="medicamento_id", nullable=false)
  private Integer medicamentoId;

  @Column(nullable=false, columnDefinition="TEXT")
  private String dosis;

  @Column(columnDefinition="TEXT")
  private String indicaciones;

  @Column(name="fecha_creacion_registro", insertable=false, updatable=false)
  private Timestamp fechaCreacionRegistro; // la pone el DEFAULT de BD
}
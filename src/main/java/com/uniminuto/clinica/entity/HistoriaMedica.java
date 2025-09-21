package com.uniminuto.clinica.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 * Entidad que representa la historia médica de un paciente.
 * Cada paciente puede tener una historia médica donde se registran
 * todas las anotaciones médicas realizadas por los doctores.
 *
 * @author Sistema Clínica
 */
@Data
@Entity
@Table(name = "historia_medica")
public class HistoriaMedica implements Serializable {
    
    /** Id serializable para la clase. */
    private static final long serialVersionUID = 1L;

    /** Identificador único de la historia médica. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Paciente al que pertenece esta historia médica. */
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    /** Fecha de creación de la historia médica. */
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}
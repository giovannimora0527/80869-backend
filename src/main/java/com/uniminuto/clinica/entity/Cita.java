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
 * Entidad que representa una cita en el sistema.
 * Vincula un {@link Medico} y un {@link Paciente} en una fecha y hora.
 *
 * @author AI
 */
@Data
@Entity
@Table(name = "cita")
public class Cita implements Serializable {
    /** Id serializable para la clase. */
    private static final long serialVersionUID = 1L;

    /** Identificador único de la cita. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Paciente asociado a la cita. */
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    /** Médico asociado a la cita. */
    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    /** Fecha y hora programada de la cita. */
    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    /** Estado de la cita (programada, confirmada, cancelada, etc.). */
    @Column(name = "estado")
    private String estado;

    /** Motivo de la cita. */
    @Column(name = "motivo")
    private String motivo;
}



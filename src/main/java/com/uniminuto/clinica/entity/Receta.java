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
 * Entidad que representa una receta asociada a una {@link Cita}.
 *
 * Requiere almacenar fecha de creación del registro.
 *
 * @author AI
 */
@Data
@Entity
@Table(name = "receta")
public class Receta implements Serializable {
    /** Dosis de la receta. */
    @Column(name = "dosis", nullable = false)
    private String dosis;

    /** Indicaciones de la receta. */
    @Column(name = "indicaciones")
    private String indicaciones;
    /** Id serializable para la clase. */
    private static final long serialVersionUID = 1L;

    /** Identificador único de la receta. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Cita asociada a la receta. */
    @ManyToOne
    @JoinColumn(name = "cita_id")
    private Cita cita;

    /** Medicamento asociado a la receta. */
    @ManyToOne
    @JoinColumn(name = "medicamento_id", nullable = false)
    private Medicamento medicamento;


    /** Fecha y hora de creación del registro. */
    @Column(name = "fecha_creacion_registro")
    private LocalDateTime fechaCreacionRegistro;
}



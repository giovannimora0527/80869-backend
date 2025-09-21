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
 * Entidad que representa las anotaciones médicas realizadas en la historia
 * clínica de un paciente. Cada anotación está asociada a una historia médica
 * específica y es realizada por un médico en una fecha determinada.
 *
 * @author Sistema Clínica
 */
@Data
@Entity
@Table(name = "anotacion_historia")
public class AnotacionHistoria implements Serializable {
    
    /** Id serializable para la clase. */
    private static final long serialVersionUID = 1L;

    /** Identificador único de la anotación. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Historia médica a la que pertenece esta anotación. */
    @ManyToOne
    @JoinColumn(name = "historia_id")
    private HistoriaMedica historia;

    /** Médico que realiza la anotación. */
    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    /** Fecha y hora cuando se realiza la anotación. */
    @Column(name = "fecha")
    private LocalDateTime fecha;

    /** Descripción o contenido de la anotación médica. */
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
}
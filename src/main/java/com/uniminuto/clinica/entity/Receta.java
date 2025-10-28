package com.uniminuto.clinica.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "receta")
@ToString
@EqualsAndHashCode
@Data
public class Receta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cita_id", nullable = false)
    private Cita cita;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "medicamento_id", nullable = false)
    private Medicamento medicamento;

    @NotNull
    @Column(name = "dosis", nullable = false, columnDefinition = "text")
    private String dosis;

    @Column(name = "indicaciones", columnDefinition = "text")
    private String indicaciones;

    @Column(name = "fecha_creacion_registro")
    private LocalDateTime fechaCreacionRegistro;

    @Column(name = "fecha_actualizacion_registro")
    private LocalDateTime fechaModificacionRegistro;
}

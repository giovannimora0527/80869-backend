package com.uniminuto.clinica.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * Entidad que representa un medicamento en el inventario de la clínica.
 */
@Data
@Entity
@Table(name = "medicamento")
public class Medicamento implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "presentacion")
    private String presentacion;

    /** Fecha de compra del medicamento. */
    @Column(name = "fecha_compra")
    private LocalDate fechaCompra;

    /** Fecha de vencimiento del medicamento. */
    @Column(name = "fecha_vence")
    private LocalDate fechaVence;

    /** Fecha de creación del registro. */
    @Column(name = "fecha_creacion_registro")
    private LocalDateTime fechaCreacionRegistro;

    /** Fecha de modificación del registro. */
    @Column(name = "fecha_modificacion_registro")
    private LocalDateTime fechaModificacionRegistro;
}

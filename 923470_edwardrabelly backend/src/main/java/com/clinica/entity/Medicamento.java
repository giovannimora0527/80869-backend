package com.clinica.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicamento")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 100)
    private String presentacion;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDate fechaCompra;

    @Column(name = "fecha_vence", nullable = false)
    private LocalDate fechaVence;

    @Column(name = "fecha_creacion_registro", nullable = false)
    private LocalDateTime fechaCreacionRegistro = LocalDateTime.now();

    @Column(name = "fecha_modificacion_registro")
    private LocalDateTime fechaModificacionRegistro;

    public Medicamento() {}

    // ===== Getters y Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public LocalDate getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDate fechaCompra) { this.fechaCompra = fechaCompra; }

    public LocalDate getFechaVence() { return fechaVence; }
    public void setFechaVence(LocalDate fechaVence) { this.fechaVence = fechaVence; }

    public LocalDateTime getFechaCreacionRegistro() { return fechaCreacionRegistro; }
    public void setFechaCreacionRegistro(LocalDateTime fechaCreacionRegistro) { this.fechaCreacionRegistro = fechaCreacionRegistro; }

    public LocalDateTime getFechaModificacionRegistro() { return fechaModificacionRegistro; }
    public void setFechaModificacionRegistro(LocalDateTime fechaModificacionRegistro) { this.fechaModificacionRegistro = fechaModificacionRegistro; }
}

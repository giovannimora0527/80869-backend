package com.clinica.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "especializacion")
public class Especializacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "codigo_especializacion", nullable = false, unique = true, length = 10)
    private String codigoEspecializacion;

    public Especializacion() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCodigoEspecializacion() { return codigoEspecializacion; }
    public void setCodigoEspecializacion(String codigoEspecializacion) { this.codigoEspecializacion = codigoEspecializacion; }
}

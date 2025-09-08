package com.uniminuto.clinica.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true)
    private String documentoIdentidad;

    private int edad;

    public Paciente() {}

    public Paciente(Long id, String nombre, String documentoIdentidad, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.documentoIdentidad = documentoIdentidad;
        this.edad = edad;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
}

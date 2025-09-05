package com.uniminuto.clinica.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "paciente")
public class Paciente implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "usuario_id")
    private int usuario;
    @Column(name = "tipo_documento")
    private String tipoDocumento;
    @Column(name = "numero_documento")
    private String numeroDocumento;
    @Column(name = "nombres")
    private String nombre;
    @Column(name = "apellidos")
    private String apellido;
    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;
    @Column(name = "genero")
    private char genero;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "direccion")
    private String direccion;
}

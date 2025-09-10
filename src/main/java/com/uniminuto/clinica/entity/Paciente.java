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
    private int pacienteId;

    @Column(name = "usuario_id")
    private int usuarioId;

    @Column(name = "tipo_documento")
    private String docTipo;

    @Column(name = "numero_documento")
    private String docNumero;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "fecha_nacimiento")
    private Date nacimiento;

    @Column(name = "genero")
    private char sexo;

    @Column(name = "telefono")
    private String celular;

    @Column(name = "direccion")
    private String domicilio;
}

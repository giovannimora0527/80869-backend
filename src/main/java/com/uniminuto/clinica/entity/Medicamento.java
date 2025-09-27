package com.uniminuto.clinica.entity;

import lombok.Data;
import org.hibernate.tuple.GeneratedValueGeneration;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "medicamento")
public class Medicamento implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    @Nullable
    private String descripcion;
    @Column(name = "presentacion")
    @Nullable
    private String presentacion;

}

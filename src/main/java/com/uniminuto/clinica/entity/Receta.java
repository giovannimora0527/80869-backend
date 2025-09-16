package com.uniminuto.clinica.entity;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "receta")
public class Receta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "dosis")
    private String dosis;
    @Column(name = "fecha_registro")
    private String fechaRegistro;
    @Column(name = "indicaciones")
    @Nullable
    private String indicaciones;
    @ManyToOne
    @JoinColumn(name = "cita_id")
    private Cita cita;
    @ManyToOne
    @JoinColumn(name = "medicamento_id")
    private Medicamento medicamento;

}

package com.uniminuto.clinica.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "cita")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    @JsonIgnoreProperties("citas")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    @JsonIgnoreProperties("citas")
    private Medico medico;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Lob
    @Column(name = "motivo")
    private String motivo;

    @OneToMany(mappedBy = "cita", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("cita")
    private List<Receta> recetas;
}
package com.uniminuto.clinica.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "auditoria_login")
public class AuditoriaLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "ip", length = 45)
    private String ip;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;
}

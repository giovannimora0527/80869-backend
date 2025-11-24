package com.uniminuto.clinica.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usuarios_bloqueados")
public class UsuarioBloqueado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "fecha_bloqueo", nullable = false)
    private LocalDateTime fechaBloqueo;

    @Column(name = "fecha_desbloqueo", nullable = false)
    private LocalDateTime fechaDesbloqueo;

    @Column(name = "motivo", length = 500)
    private String motivo;

    @Column(name = "intentos_fallidos")
    private Integer intentosFallidos;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}

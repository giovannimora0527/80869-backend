package com.uniminuto.clinica.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "intentos_fallidos_login")
@NoArgsConstructor
@NamedQuery(
        name = "IntentoFallidoLogin.countRecentAttempts",
        query = "SELECT COUNT(i) FROM IntentoFallidoLogin i WHERE i.username = :username AND i.fechaIntento > :fechaLimite"
)
public class IntentoFallidoLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private Usuario usuario;

    @Column(name = "direccion_ip", length = 45)
    private String direccionIp;

    @Column(name = "fecha_intento", nullable = false)
    private LocalDateTime fechaIntento;

    @Column(length = 500)
    private String detalles;
}


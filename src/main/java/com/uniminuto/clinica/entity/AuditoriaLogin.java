package com.uniminuto.clinica.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_login")
public class AuditoriaLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username_ingresado")
    private String usuario;
    
    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;
    
    @Column(name = "ip_address")
    private String ip;
    
    @Column(name = "resultado")
    private String resultado;
    
    @Column(name = "motivo_fallo")
    private String motivo;
    
    @Column(name = "user_agent")
    private String userAgent;

    public AuditoriaLogin() {}

    public AuditoriaLogin(String usuario, LocalDateTime fechaHora, String ip, boolean exito, String motivo) {
        this.usuario = usuario;
        this.fechaHora = fechaHora;
        this.ip = ip;
        this.resultado = exito ? "EXITOSO" : "FALLIDO";
        this.motivo = motivo;
    }
    
    @PrePersist
    protected void onCreate() {
        if (fechaHora == null) {
            fechaHora = LocalDateTime.now();
        }
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    
    public boolean isExito() {
        return "EXITOSO".equals(resultado);
    }

    public void setExito(boolean exito) {
        this.resultado = exito ? "EXITOSO" : "FALLIDO";
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}

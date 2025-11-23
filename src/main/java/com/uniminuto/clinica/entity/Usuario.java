package com.uniminuto.clinica.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * Entidad Usuario con campos de auditoría y seguridad.
 *
 * @author lmora
 */
@Data
@Entity
@Table(name="usuario")
public class Usuario implements Serializable {
    
    /**
     * Id serializable.
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "password_hash")
    private String password;
    
    @Column(name = "rol")
    private String rol;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "activo")
    private boolean activo;

    @Column(name = "email")
    private String email;
    
    // ========== CAMPOS DE SEGURIDAD Y AUDITORÍA ==========
    
    /**
     * Contador de intentos fallidos de login.
     */
    @Column(name = "intentos_fallidos")
    private Integer intentosFallidos = 0;
    
    /**
     * Fecha hasta la cual el usuario está bloqueado.
     */
    @Column(name = "bloqueado_hasta")
    private LocalDateTime bloqueadoHasta;
    
    /**
     * Fecha del último intento de login.
     */
    @Column(name = "ultimo_intento")
    private LocalDateTime ultimoIntento;
    
    /**
     * Token para recuperación de contraseña.
     */
    @Column(name = "password_reset_token")
    private String passwordResetToken;
    
    /**
     * Fecha de expiración del token de recuperación.
     */
    @Column(name = "token_expiracion")
    private LocalDateTime tokenExpiracion;
    
    /**
     * Fecha del último acceso exitoso.
     */
    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;
    
    /**
     * IP de la última conexión.
     */
    @Column(name = "ip_ultima_conexion")
    private String ipUltimaConexion;
    
    /**
     * Verifica si el usuario está bloqueado.
     * @return true si está bloqueado, false en caso contrario
     */
    public boolean estaBloqueado() {
        if (bloqueadoHasta == null) {
            return false;
        }
        return LocalDateTime.now().isBefore(bloqueadoHasta);
    }
    
    /**
     * Verifica si el token de recuperación es válido.
     * @param token Token a validar
     * @return true si es válido, false en caso contrario
     */
    public boolean esTokenValido(String token) {
        if (passwordResetToken == null || tokenExpiracion == null) {
            return false;
        }
        return passwordResetToken.equals(token) && LocalDateTime.now().isBefore(tokenExpiracion);
    }
    
}

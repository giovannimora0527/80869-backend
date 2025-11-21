package com.uniminuto.clinica.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;

/**
 * Entidad para registrar eventos de auditoría del sistema.
 *
 * @author lmora
 */
@Data
@Entity
@Table(name = "auditoria_log")
public class AuditoriaLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "tipo_evento", length = 50, nullable = false)
    private String tipoEvento;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "nivel", length = 20)
    private String nivel;

    @Column(name = "datos_adicionales", columnDefinition = "JSON")
    private String datosAdicionales;
    
    @Column(name = "modulo", length = 50)
    private String modulo;

    /**
     * Constructor vacío.
     */
    public AuditoriaLog() {
        this.fechaHora = LocalDateTime.now();
    }

    /**
     * Constructor con parámetros principales.
     */
    public AuditoriaLog(String tipoEvento, Long usuarioId, String username, 
                        String descripcion, String ipAddress, String nivel) {
        this();
        this.tipoEvento = tipoEvento;
        this.usuarioId = usuarioId;
        this.username = username;
        this.descripcion = descripcion;
        this.ipAddress = ipAddress;
        this.nivel = nivel;
    }

    /**
     * Crea un log de login exitoso.
     */
    public static AuditoriaLog loginExitoso(Usuario usuario, String ip) {
        return new AuditoriaLog(
            "LOGIN_EXITOSO",
            usuario.getId(),
            usuario.getUsername(),
            "Usuario ingresó correctamente al sistema",
            ip,
            "INFO"
        );
    }

    /**
     * Crea un log de login fallido.
     */
    public static AuditoriaLog loginFallido(String username, String ip, int intentosRestantes) {
        return new AuditoriaLog(
            "LOGIN_FALLIDO",
            null,
            username,
            "Intento de login fallido. Intentos restantes: " + intentosRestantes,
            ip,
            "WARNING"
        );
    }

    /**
     * Crea un log de usuario bloqueado.
     */
    public static AuditoriaLog usuarioBloqueado(Usuario usuario, String ip) {
        return new AuditoriaLog(
            "USUARIO_BLOQUEADO",
            usuario.getId(),
            usuario.getUsername(),
            "Usuario bloqueado por exceder intentos de login",
            ip,
            "ERROR"
        );
    }

    /**
     * Crea un log de recuperación de contraseña.
     */
    public static AuditoriaLog recuperacionPassword(Usuario usuario, String ip) {
        return new AuditoriaLog(
            "PASSWORD_RECOVERY",
            usuario.getId(),
            usuario.getUsername(),
            "Solicitud de recuperación de contraseña",
            ip,
            "INFO"
        );
    }

    /**
     * Crea un log de cambio de contraseña.
     */
    public static AuditoriaLog cambioPassword(Usuario usuario, String ip) {
        return new AuditoriaLog(
            "PASSWORD_CHANGED",
            usuario.getId(),
            usuario.getUsername(),
            "Contraseña cambiada exitosamente",
            ip,
            "INFO"
        );
    }
}

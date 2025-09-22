package com.uniminuto.clinica.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Data;

/**
 * Entidad JPA que representa una receta médica en el sistema clínico.
 * 
 * <p>Una receta está asociada a una cita médica y contiene información sobre
 * medicamentos prescritos, dosificación e indicaciones de uso. La entidad
 * incluye auto-generación de timestamp para auditoria.</p>
 * 
 * <p>Relaciones:</p>
 * <ul>
 *   <li>ManyToOne con {@link Cita} - La cita donde se prescribió la receta</li>
 *   <li>ManyToOne con {@link Medicamento} - El medicamento prescrito</li>
 * </ul>
 * 
 * @author Daniel Donado
 * @version 1.0
 * @since 2024-09-21
 */
@Data
@Entity
@Table(name = "receta")
public class Receta implements Serializable {
    /**
     * Número de versión para la serialización de la entidad.
     * Garantiza compatibilidad durante la deserialización.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único de la receta.
     * Se genera automáticamente usando estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Información sobre la dosificación del medicamento prescrito.
     * Campo requerido que especifica cantidad y frecuencia de administración.
     * 
     * @example "500mg cada 8 horas por 7 días"
     */
    @Column(name = "dosis", nullable = false)
    private String dosis;

    /**
     * Indicaciones adicionales para el uso del medicamento.
     * Campo opcional con instrucciones especiales para el paciente.
     * 
     * @example "Tomar con alimentos, evitar alcohol"
     */
    @Column(name = "indicaciones")
    private String indicaciones;

    /**
     * Cita médica donde se prescribió esta receta.
     * Relación Many-to-One con la entidad Cita.
     */
    @ManyToOne
    @JoinColumn(name = "cita_id")
    private Cita cita;

    /**
     * Medicamento prescrito en esta receta.
     * Relación Many-to-One obligatoria con la entidad Medicamento.
     */
    @ManyToOne
    @JoinColumn(name = "medicamento_id", nullable = false)
    private Medicamento medicamento;

    /**
     * Timestamp de creación del registro en la base de datos.
     * Se establece automáticamente cuando se persiste la entidad.
     */
    @Column(name = "fecha_creacion_registro")
    private LocalDateTime fechaCreacionRegistro;

    /**
     * Método de callback que se ejecuta automáticamente antes de persistir la entidad.
     * Establece la fecha y hora actual como timestamp de creación si no está definida.
     * 
     * <p>Este método garantiza que todas las recetas tengan un timestamp de auditoría
     * sin requerir intervención manual del desarrollador.</p>
     */
    @PrePersist
    protected void onCreate() {
        if (fechaCreacionRegistro == null) {
            fechaCreacionRegistro = LocalDateTime.now();
        }
    }
}



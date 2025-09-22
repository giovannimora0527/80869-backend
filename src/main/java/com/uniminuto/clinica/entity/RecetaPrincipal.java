package com.uniminuto.clinica.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad principal que representa una receta médica completa.
 * Una receta principal puede contener múltiples medicamentos prescritos.
 * 
 * @author AI
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "receta_principal")
public class RecetaPrincipal implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** Identificador único de la receta principal */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /** Cita asociada a la receta */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id", nullable = false)
    private Cita cita;
    
    /** Médico que prescribe la receta */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;
    
    /** Paciente para quien se prescribe */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;
    
    /** Fecha y hora de creación de la receta */
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    /** Estado de la receta (ACTIVA, EXPIRADA, CANCELADA) */
    @Column(name = "estado", nullable = false, length = 20)
    private String estado;
    
    /** Observaciones generales del médico */
    @Column(name = "observaciones", length = 1000)
    private String observaciones;
    
    /** Indicaciones generales para el paciente */
    @Column(name = "indicaciones_generales", length = 1000)
    private String indicacionesGenerales;
    
    /** Duración total del tratamiento en días */
    @Column(name = "duracion_total_tratamiento")
    private Integer duracionTotalTratamiento;
    
    /** Lista de medicamentos asociados a esta receta */
    @OneToMany(mappedBy = "recetaPrincipal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RecetaMedicamento> medicamentos;
    
    /** Usuario que creó el registro (para auditoría) */
    @Column(name = "usuario_creacion", length = 100)
    private String usuarioCreacion;
    
    /** Fecha de última modificación */
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
}
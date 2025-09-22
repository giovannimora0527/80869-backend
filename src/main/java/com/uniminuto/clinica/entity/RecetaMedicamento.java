package com.uniminuto.clinica.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entidad que representa la relación entre una receta principal y un medicamento específico.
 * Contiene la información detallada de cómo debe administrarse cada medicamento.
 * 
 * @author AI
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "receta_medicamento")
public class RecetaMedicamento implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** Identificador único de la relación receta-medicamento */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /** Receta principal a la que pertenece este medicamento */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_principal_id", nullable = false)
    private RecetaPrincipal recetaPrincipal;
    
    /** Medicamento prescrito (opcional si se usa nombre libre) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicamento_id")
    private Medicamento medicamento;
    
    /** Nombre del medicamento (libre, para casos donde no está en el catálogo) */
    @Column(name = "nombre_medicamento", nullable = false, length = 200)
    private String nombreMedicamento;
    
    /** Dosis prescrita */
    @Column(name = "dosis", nullable = false, length = 100)
    private String dosis;
    
    /** Frecuencia de administración */
    @Column(name = "frecuencia", nullable = false, length = 100)
    private String frecuencia;
    
    /** Duración del tratamiento en días */
    @Column(name = "duracion_dias", nullable = false)
    private Integer duracionDias;
    
    /** Indicaciones especiales para este medicamento */
    @Column(name = "indicaciones", length = 500)
    private String indicaciones;
    
    /** Orden de aparición en la receta */
    @Column(name = "orden")
    private Integer orden;
}
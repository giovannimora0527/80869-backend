package com.uniminuto.clinica.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 *
 * @author lmora
 */
@Data
@Entity
@Table(name = "inventario_medicamento")
public class InventarioMedicamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "fecha_ingreso", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;

    @Column(name = "fecha_vencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;

    @Column(name = "lote")
    private String lote;

    @ManyToOne
    @JoinColumn(name = "medicamento_id", nullable = false)
    private Medicamento medicamento;
}
package com.uniminuto.clinica.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *
 * @author lmora
 */
@Data
public class InventarioMedicamentoRq {

    @NotNull(message = "La cantidad es obligatoria")
    private Integer cantidad;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    private Date fechaIngreso;

    private Date fechaVencimiento;

    private String lote;

    @NotNull(message = "El medicamento es obligatorio")
    private Long medicamentoId;
}
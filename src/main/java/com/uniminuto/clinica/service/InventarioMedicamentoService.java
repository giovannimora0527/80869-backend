package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.InventarioMedicamento;
import com.uniminuto.clinica.model.InventarioMedicamentoRq;
import com.uniminuto.clinica.model.RespuestaRs;
import java.util.List;
import org.apache.coyote.BadRequestException;

/**
 *
 * @author lmora
 */
public interface InventarioMedicamentoService {

    List<InventarioMedicamento> listarInventario();

    List<InventarioMedicamento> listarPorMedicamento(Long idMedicamento) throws BadRequestException;

    RespuestaRs guardarInventario(InventarioMedicamentoRq rq) throws BadRequestException;

    RespuestaRs actualizarInventario(Long id, InventarioMedicamentoRq rq) throws BadRequestException;
}
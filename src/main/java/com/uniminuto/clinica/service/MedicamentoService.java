package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Medicamento;
import com.uniminuto.clinica.model.MedicamentoRq;
import com.uniminuto.clinica.model.RespuestaRs;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface MedicamentoService {
    List<Medicamento> listarMedicamentos();

    RespuestaRs guardarMedicamento(MedicamentoRq medicamento) throws BadRequestException;

    RespuestaRs actualizarMedicamento(MedicamentoRq medicamento) throws BadRequestException;
}

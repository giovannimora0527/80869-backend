package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.InventarioMedicamento;
import com.uniminuto.clinica.entity.Medicamento;
import com.uniminuto.clinica.model.InventarioMedicamentoRq;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.repository.InventarioMedicamentoRepository;
import com.uniminuto.clinica.repository.MedicamentoRepository;
import com.uniminuto.clinica.service.InventarioMedicamentoService;
import java.util.List;
import java.util.Optional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author lmora
 */
@Service
public class InventarioMedicamentoServiceImpl implements InventarioMedicamentoService {

    @Autowired
    private InventarioMedicamentoRepository inventarioRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Override
    public List<InventarioMedicamento> listarInventario() {
        return this.inventarioRepository.findAll();
    }

    @Override
    public List<InventarioMedicamento> listarPorMedicamento(Long idMedicamento) throws BadRequestException {
        Optional<Medicamento> optMedicamento = this.medicamentoRepository.findById(idMedicamento);
        if (optMedicamento.isEmpty()) {
            throw new BadRequestException("No existe el medicamento con el ID indicado");
        }
        return this.inventarioRepository.findByMedicamento(optMedicamento.get());
    }

    @Override
    public RespuestaRs guardarInventario(InventarioMedicamentoRq rq) throws BadRequestException {
        Optional<Medicamento> optMedicamento = this.medicamentoRepository.findById(rq.getMedicamentoId());
        if (optMedicamento.isEmpty()) {
            throw new BadRequestException("No existe el medicamento indicado");
        }

        InventarioMedicamento inv = this.convertir(rq, optMedicamento.get());
        this.inventarioRepository.save(inv);

        RespuestaRs rta = new RespuestaRs();
        rta.setMensaje("Registro de inventario guardado correctamente");
        return rta;
    }

    @Override
    public RespuestaRs actualizarInventario(Long id, InventarioMedicamentoRq rq) throws BadRequestException {
        Optional<InventarioMedicamento> optInv = this.inventarioRepository.findById(id);
        if (optInv.isEmpty()) {
            throw new BadRequestException("No existe el registro de inventario con el ID indicado");
        }

        Optional<Medicamento> optMedicamento = this.medicamentoRepository.findById(rq.getMedicamentoId());
        if (optMedicamento.isEmpty()) {
            throw new BadRequestException("No existe el medicamento indicado");
        }

        InventarioMedicamento inv = optInv.get();
        inv.setCantidad(rq.getCantidad());
        inv.setFechaIngreso(rq.getFechaIngreso());
        inv.setFechaVencimiento(rq.getFechaVencimiento());
        inv.setLote(rq.getLote());
        inv.setMedicamento(optMedicamento.get());

        this.inventarioRepository.save(inv);

        RespuestaRs rta = new RespuestaRs();
        rta.setMensaje("Registro de inventario actualizado correctamente");
        return rta;
    }

    private InventarioMedicamento convertir(InventarioMedicamentoRq rq, Medicamento medicamento) {
        InventarioMedicamento inv = new InventarioMedicamento();
        inv.setCantidad(rq.getCantidad());
        inv.setFechaIngreso(rq.getFechaIngreso());
        inv.setFechaVencimiento(rq.getFechaVencimiento());
        inv.setLote(rq.getLote());
        inv.setMedicamento(medicamento);
        return inv;
    }

}
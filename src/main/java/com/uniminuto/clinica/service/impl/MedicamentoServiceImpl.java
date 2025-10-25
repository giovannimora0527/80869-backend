package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Medicamento;
import com.uniminuto.clinica.model.MedicamentoRq;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.repository.MedicamentoRepository;
import com.uniminuto.clinica.service.MedicamentoService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MedicamentoServiceImpl implements MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Override
    public List<Medicamento> listarMedicamentos() {
        return medicamentoRepository.findAll()
                .stream()
                .sorted((m1, m2) -> m2.getFechaCompra().compareTo(m1.getFechaCompra()))
                .toList();
    }

    @Override
    public RespuestaRs guardarMedicamento(MedicamentoRq medicamentoRq) throws BadRequestException {
        validarFormulario(medicamentoRq);

        Optional<Medicamento> optMedicamento = medicamentoRepository.findByNombre(medicamentoRq.getNombre());
        if (optMedicamento.isPresent()) {
            throw new BadRequestException("El medicamento ya existe");
        }

        Medicamento nuevo = mapearAMedicamento(medicamentoRq);
        medicamentoRepository.save(nuevo);

        RespuestaRs rta = new RespuestaRs();
        rta.setMensaje("Medicamento creado exitosamente");
        rta.setStatus(200);
        return rta;
    }

    @Override
    public RespuestaRs actualizarMedicamento(MedicamentoRq medicamentoRq) throws BadRequestException {
        if (medicamentoRq.getId() == null) {
            throw new BadRequestException("El id del medicamento es obligatorio");
        }

        Optional<Medicamento> optMedicamento = medicamentoRepository.findById(medicamentoRq.getId());
        if (!optMedicamento.isPresent()) {
            throw new BadRequestException("El medicamento no existe y no se puede actualizar");
        }

        Medicamento medicamentoActual = optMedicamento.get();
        if (!medicamentoActual.getNombre().equalsIgnoreCase(medicamentoRq.getNombre())) {
            Optional<Medicamento> optMedicamentoPorNombre = medicamentoRepository.findByNombre(medicamentoRq.getNombre());
            if (optMedicamentoPorNombre.isPresent()) {
                throw new BadRequestException("El nombre del medicamento ya existe");
            }
        }

        medicamentoActual.setNombre(medicamentoRq.getNombre());
        medicamentoActual.setDescripcion(medicamentoRq.getDescripcion());
        medicamentoActual.setPresentacion(medicamentoRq.getPresentacion());
        medicamentoActual.setFechaCompra(medicamentoRq.getFechaCompra());
        medicamentoActual.setFechaVence(medicamentoRq.getFechaVence());
        medicamentoActual.setFechaModificacionRegistro(LocalDateTime.now());
        medicamentoRepository.save(medicamentoActual);

        RespuestaRs rta = new RespuestaRs();
        rta.setMensaje("Medicamento actualizado exitosamente");
        rta.setStatus(200);
        return rta;
    }

    @Override
    public RespuestaRs eliminarMedicamento(Long id) throws BadRequestException {
        Optional<Medicamento> optMedicamento = medicamentoRepository.findById(id);
        if (!optMedicamento.isPresent()) {
            throw new BadRequestException("El medicamento no existe y no se puede eliminar");
        }

        medicamentoRepository.deleteById(id);

        RespuestaRs rta = new RespuestaRs();
        rta.setMensaje("Medicamento eliminado exitosamente");
        rta.setStatus(200);
        return rta;
    }

    private Medicamento mapearAMedicamento(MedicamentoRq medicamentoRq) {
        Medicamento nuevo = new Medicamento();
        nuevo.setNombre(medicamentoRq.getNombre());
        nuevo.setDescripcion(medicamentoRq.getDescripcion());
        nuevo.setPresentacion(medicamentoRq.getPresentacion());
        nuevo.setFechaCompra(medicamentoRq.getFechaCompra());
        nuevo.setFechaVence(medicamentoRq.getFechaVence());
        nuevo.setFechaCreacionRegistro(LocalDateTime.now());
        return nuevo;
    }

    private void validarFormulario(MedicamentoRq medicamentoRq) throws BadRequestException {
        if (medicamentoRq.getNombre() == null || medicamentoRq.getNombre().isBlank()) {
            throw new BadRequestException("El nombre es obligatorio");
        }
        if (medicamentoRq.getDescripcion() == null || medicamentoRq.getDescripcion().isBlank()) {
            throw new BadRequestException("El campo descripcion es obligatorio");
        }
        if (medicamentoRq.getPresentacion() == null || medicamentoRq.getPresentacion().isBlank()) {
            throw new BadRequestException("El campo presentacion es obligatorio");
        }
        if (medicamentoRq.getFechaCompra() == null) {
            throw new BadRequestException("El campo fecha de compra es obligatorio");
        }
        if (medicamentoRq.getFechaVence() == null) {
            throw new BadRequestException("El campo fecha de vencimiento es obligatorio");
        }
    }
}
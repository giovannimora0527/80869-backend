package com.clinica.service;

import com.clinica.entity.Medicamento;
import com.clinica.repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    public List<Medicamento> listar() {
        return medicamentoRepository.findAll();
    }

    public Optional<Medicamento> obtenerPorId(Long id) {
        return medicamentoRepository.findById(id);
    }

    public Medicamento guardar(Medicamento medicamento) {
        medicamento.setFechaCreacionRegistro(LocalDateTime.now());
        return medicamentoRepository.save(medicamento);
    }

    public Medicamento actualizar(Long id, Medicamento medicamentoActualizado) {
        return medicamentoRepository.findById(id).map(medicamento -> {
            medicamento.setNombre(medicamentoActualizado.getNombre());
            medicamento.setDescripcion(medicamentoActualizado.getDescripcion());
            medicamento.setPresentacion(medicamentoActualizado.getPresentacion());
            medicamento.setFechaCompra(medicamentoActualizado.getFechaCompra());
            medicamento.setFechaVence(medicamentoActualizado.getFechaVence());
            medicamento.setFechaModificacionRegistro(LocalDateTime.now());
            return medicamentoRepository.save(medicamento);
        }).orElseThrow(() -> new RuntimeException("Medicamento no encontrado"));
    }

    public void eliminar(Long id) {
        medicamentoRepository.deleteById(id);
    }
}

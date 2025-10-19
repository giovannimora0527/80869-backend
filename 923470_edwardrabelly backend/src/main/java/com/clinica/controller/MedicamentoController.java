package com.clinica.controller;

import com.clinica.entity.Medicamento;
import com.clinica.service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicamento")
@CrossOrigin(origins = "http://localhost:4200")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    @GetMapping("/listar")
    public List<Medicamento> listar() {
        return medicamentoService.listar();
    }

    @GetMapping("/{id}")
    public Optional<Medicamento> obtenerPorId(@PathVariable Long id) {
        return medicamentoService.obtenerPorId(id);
    }

    @PostMapping("/crear")
    public Medicamento crear(@RequestBody Medicamento medicamento) {
        return medicamentoService.guardar(medicamento);
    }

    @PutMapping("/actualizar/{id}")
    public Medicamento actualizar(@PathVariable Long id, @RequestBody Medicamento medicamento) {
        return medicamentoService.actualizar(id, medicamento);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        medicamentoService.eliminar(id);
    }
}

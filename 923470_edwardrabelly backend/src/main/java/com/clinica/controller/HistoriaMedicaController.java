package com.clinica.controller;

import com.clinica.entity.HistoriaMedica;
import com.clinica.service.HistoriaMedicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/historia")
@CrossOrigin(origins = "http://localhost:4200")
public class HistoriaMedicaController {

    @Autowired
    private HistoriaMedicaService historiaMedicaService;

    @GetMapping("/listar")
    public List<HistoriaMedica> listar() {
        return historiaMedicaService.listar();
    }

    @GetMapping("/{id}")
    public Optional<HistoriaMedica> obtener(@PathVariable Long id) {
        return historiaMedicaService.obtenerPorId(id);
    }

    @PostMapping("/crear")
    public HistoriaMedica crear(@RequestBody HistoriaMedica historiaMedica) {
        return historiaMedicaService.guardar(historiaMedica);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        historiaMedicaService.eliminar(id);
    }
}

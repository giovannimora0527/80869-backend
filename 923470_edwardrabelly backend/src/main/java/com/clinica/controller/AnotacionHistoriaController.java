package com.clinica.controller;

import com.clinica.entity.AnotacionHistoria;
import com.clinica.service.AnotacionHistoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/anotacion")
@CrossOrigin(origins = "http://localhost:4200")
public class AnotacionHistoriaController {

    @Autowired
    private AnotacionHistoriaService anotacionHistoriaService;

    @GetMapping("/listar")
    public List<AnotacionHistoria> listar() {
        return anotacionHistoriaService.listar();
    }

    @GetMapping("/{id}")
    public Optional<AnotacionHistoria> obtener(@PathVariable Long id) {
        return anotacionHistoriaService.obtenerPorId(id);
    }

    @PostMapping("/crear")
    public AnotacionHistoria crear(@RequestBody AnotacionHistoria anotacionHistoria) {
        return anotacionHistoriaService.guardar(anotacionHistoria);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        anotacionHistoriaService.eliminar(id);
    }
}

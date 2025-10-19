package com.clinica.controller;

import com.clinica.entity.Receta;
import com.clinica.service.RecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/receta")
@CrossOrigin(origins = "http://localhost:4200")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    @GetMapping("/listar")
    public List<Receta> listar() {
        return recetaService.listar();
    }

    @GetMapping("/{id}")
    public Optional<Receta> obtenerPorId(@PathVariable Long id) {
        return recetaService.obtenerPorId(id);
    }

    @PostMapping("/crear")
    public Receta crear(@RequestBody Receta receta) {
        return recetaService.guardar(receta);
    }

    @PutMapping("/actualizar/{id}")
    public Receta actualizar(@PathVariable Long id, @RequestBody Receta receta) {
        return recetaService.actualizar(id, receta);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        recetaService.eliminar(id);
    }
}

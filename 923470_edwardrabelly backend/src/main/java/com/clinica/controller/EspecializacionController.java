package com.clinica.controller;

import com.clinica.entity.Especializacion;
import com.clinica.service.EspecializacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/especializacion")
@CrossOrigin(origins = "http://localhost:4200")
public class EspecializacionController {

    @Autowired
    private EspecializacionService especializacionService;

    @GetMapping("/listar")
    public List<Especializacion> listar() {
        return especializacionService.listar();
    }

    @GetMapping("/{id}")
    public Optional<Especializacion> obtenerPorId(@PathVariable Long id) {
        return especializacionService.obtenerPorId(id);
    }

    @PostMapping("/crear")
    public Especializacion crear(@RequestBody Especializacion especializacion) {
        return especializacionService.guardar(especializacion);
    }

    @PutMapping("/actualizar/{id}")
    public Especializacion actualizar(@PathVariable Long id, @RequestBody Especializacion especializacion) {
        return especializacionService.actualizar(id, especializacion);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        especializacionService.eliminar(id);
    }
}

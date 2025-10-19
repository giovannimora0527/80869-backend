package com.clinica.controller;

import com.clinica.entity.Medico;
import com.clinica.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medico")
@CrossOrigin(origins = "http://localhost:4200")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping("/listar")
    public List<Medico> listar() {
        return medicoService.listar();
    }

    @GetMapping("/{id}")
    public Optional<Medico> obtenerPorId(@PathVariable Long id) {
        return medicoService.obtenerPorId(id);
    }

    @PostMapping("/crear")
    public Medico crear(@RequestBody Medico medico) {
        return medicoService.guardar(medico);
    }

    @PutMapping("/actualizar/{id}")
    public Medico actualizar(@PathVariable Long id, @RequestBody Medico medico) {
        return medicoService.actualizar(id, medico);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        medicoService.eliminar(id);
    }
}

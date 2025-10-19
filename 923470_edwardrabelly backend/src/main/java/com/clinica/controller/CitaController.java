package com.clinica.controller;

import com.clinica.entity.Cita;
import com.clinica.service.CitaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping
    public Cita crearCita(@RequestBody Cita cita) {
        return citaService.guardar(cita);
    }

    @GetMapping
    public List<Cita> listarCitas() {
        return citaService.listarTodas();
    }
    @GetMapping("/recientes")
    public List<Cita> listarCitasRecientes() {
        return citaService.listarTodasRecientes();
    }

    @GetMapping("/{id}")
    public Cita obtenerCita(@PathVariable Long id) {
        return citaService.buscarPorId(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void eliminarCita(@PathVariable Long id) {
        citaService.eliminar(id);
    }

}


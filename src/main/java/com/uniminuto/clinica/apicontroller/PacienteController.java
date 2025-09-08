package com.uniminuto.clinica.apicontroller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import main.java.com.uniminuto.clinica.entity.Paciente;
import main.java.com.uniminuto.clinica.service.PacienteService;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    // Servicio 1: listar todos los pacientes
    @GetMapping
    public List<Paciente> listarPacientes() {
        return service.listarPacientes();
    }

    // Servicio 2: buscar paciente por documento
    @GetMapping("/{documento}")
    public Paciente buscarPorDocumento(@PathVariable String documento) {
        return service.buscarPorDocumento(documento)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }
}

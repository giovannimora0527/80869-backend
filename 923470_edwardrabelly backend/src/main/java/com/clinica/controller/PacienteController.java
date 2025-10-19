package com.clinica.controller;

import com.clinica.entity.Paciente;
import com.clinica.service.PacienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public List<Paciente> listarTodos() {
        return pacienteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPorId(@PathVariable Long id) {
        Optional<Paciente> paciente = pacienteService.buscarPorId(id);
        return paciente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Paciente crear(@RequestBody Paciente paciente) {
        return pacienteService.guardar(paciente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizar(@PathVariable Long id, @RequestBody Paciente pacienteDetalles) {
        return pacienteService.buscarPorId(id)
                .map(paciente -> {
                    paciente.setNombres(pacienteDetalles.getNombres());
                    paciente.setApellidos(pacienteDetalles.getApellidos());
                    paciente.setTelefono(pacienteDetalles.getTelefono());
                    paciente.setDireccion(pacienteDetalles.getDireccion());
                    paciente.setGenero(pacienteDetalles.getGenero());
                    return ResponseEntity.ok(pacienteService.guardar(paciente));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (pacienteService.buscarPorId(id).isPresent()) {
            pacienteService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/ordenados")
    public List<Paciente> listarOrdenados() {
        return pacienteService.listarPorFechaNacimientoDesc();
        }
}

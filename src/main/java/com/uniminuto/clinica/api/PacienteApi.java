package com.uniminuto.clinica.api;

import com.uniminuto.clinica.model.PacienteRq;
import com.uniminuto.clinica.model.PacienteRs;
import org.springframework.web.bind.annotation.*;
import org.apache.coyote.BadRequestException;
import java.util.List;
import java.util.Optional;

@RequestMapping("/paciente") // ✅ CAMBIO: Solo "/paciente", no toda la ruta
public interface PacienteApi {

    @GetMapping("/listar") // ✅ CAMBIO: Agregado "/listar"
    List<PacienteRs> obtenerTodos();

    @GetMapping("/{documento}")
    Optional<PacienteRs> buscarPorDocumento(@PathVariable String documento);

    @PostMapping
    PacienteRs guardar(@RequestBody PacienteRq paciente) throws BadRequestException;

    @PutMapping("/{id}") // ✅ NUEVO: Para actualizar
    PacienteRs actualizar(@PathVariable Long id, @RequestBody PacienteRq paciente) throws BadRequestException;

    @DeleteMapping("/{id}")
    void eliminar(@PathVariable Long id);

    @GetMapping("/ordenados-por-nacimiento")
    List<PacienteRs> obtenerPacientesOrdenadosPorFechaNacimiento();
}
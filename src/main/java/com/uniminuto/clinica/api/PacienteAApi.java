package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Paciente;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/api/pacientes")
public interface PacienteApi {
    @GetMapping("/listar")
    ResponseEntity<List<Paciente>> listarTodos();

    @GetMapping("/buscar")
    ResponseEntity<Paciente> buscarPorDocumento(
            @RequestParam(name = "documento") String documento
    ) throws BadRequestException;
}

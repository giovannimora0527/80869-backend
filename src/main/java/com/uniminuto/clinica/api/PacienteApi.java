package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Paciente;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/paciente")
public interface PacienteApi {
    @GetMapping("/listar")
    ResponseEntity<List<Paciente>>listarPacientes();
    @GetMapping("/buscar-x-documento")
    ResponseEntity<Paciente> buscarPorDocumento(@RequestParam String documento) throws BadRequestException;
}

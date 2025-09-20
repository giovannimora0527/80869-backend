package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Paciente;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/paciente")
public interface PacienteApi {

    @GetMapping(value = "/listar", produces = "application/json")
    ResponseEntity<List<Paciente>> listarPacientes();
}
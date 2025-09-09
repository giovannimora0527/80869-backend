package com.uniminuto.clinica.apicontroller;

import java.util.List;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.service.impl.PacienteServiceImpl;


@RestController
@RequestMapping("/Pacientes")
public class PacienteApiController {

    private final PacienteServiceImpl pacienteService;

    public PacienteApiController(PacienteServiceImpl pacienteService){
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public ResponseEntity<List<Paciente>> getAllPacientes(Paciente paciente){
        return ResponseEntity.ok(pacienteService.findAllPacientes());
    }

    @PostMapping
    public ResponseEntity<Paciente> createPaciente(@RequestBody Paciente paciente){
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.savePaciente(paciente));
    }

    @GetMapping("/pacientes/{documento}")
    public ResponseEntity<Paciente> buscarPorDocumento(@PathVariable String documento) {
        return pacienteService.buscarPorDocumento(documento)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

}

package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Paciente;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/buspacientes")
public interface BusPacienteApi {
    
    @GetMapping("/documento/{documento}")
    ResponseEntity<Paciente> buscarPorDocumento(@PathVariable String documento);
}
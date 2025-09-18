package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.PacienteApi;
import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.service.PacienteService;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class PacienteController implements PacienteApi {

    @Autowired
    private PacienteService pacienteService;

    @Override
    public ResponseEntity<List<Paciente>> listarPacienteFecha() {
        List<Paciente> paciente = pacienteService.encontrarPacienteFecha();
        return ResponseEntity.ok(paciente);
    }    
    
    @Override
    public ResponseEntity<List<Paciente>> listarPaciente() {
        List<Paciente> pacientes = pacienteService.encontrarTodosLosPacientes();
        return ResponseEntity.ok(pacientes);
    }

    @Override
    public ResponseEntity<List<Paciente>> listarPorGenero(@RequestParam String genero) {
        List<Paciente> pacientes = pacienteService.buscarPorGenero(genero);
        return ResponseEntity.ok(pacientes);
    }

    @Override
    public ResponseEntity<List<Paciente>> buscarPorNombre(@RequestParam String nombre) {
    try {
        List<Paciente> pacientes = pacienteService.buscarPorNombre(nombre);
        return ResponseEntity.ok(pacientes);
    } catch (BadRequestException e) {
        return ResponseEntity.badRequest().build();
    }
}

}

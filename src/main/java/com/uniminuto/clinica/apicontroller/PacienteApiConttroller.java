package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.PacienteApi;
import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.service.PacienteService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController implements PacienteApi {

    @Autowired
    private PacienteService servicioPaciente;

    @Override
    public ResponseEntity<List<Paciente>> listarTodos() {
        return ResponseEntity.ok(servicioPaciente.listaPacientes());
    }

    @Override
    public ResponseEntity<Paciente> buscarPorDocumento(String nroDocumento) throws BadRequestException {
        return ResponseEntity.ok(servicioPaciente.buscarPorDocumento(nroDocumento));
    }
}

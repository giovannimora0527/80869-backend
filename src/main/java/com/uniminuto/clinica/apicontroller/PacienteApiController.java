package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.PacienteApi;
import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.model.PacienteRq;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.service.PacienteService;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PacienteApiController implements PacienteApi {

    @Autowired
    private PacienteService pacienteService;

    @Override
    public ResponseEntity<List<Paciente>> listarPacientes() {
        return ResponseEntity.ok(pacienteService.listarPacientes());
    }

    @Override
    public ResponseEntity<Paciente> buscarPacientePorDoc(String numeroDocumento) throws BadRequestException {
        return ResponseEntity.ok(pacienteService.buscarPorDocumento(numeroDocumento));
    }

    @Override
    public ResponseEntity<List<Paciente>> listarPacientesOrdenado() throws BadRequestException {
        return ResponseEntity.ok(pacienteService.listarPacientesOrdenadoPorFechaNacimiento());
    }

    // Métodos agregados para guardar y actualizar
    @Override
    public ResponseEntity<RespuestaRs> guardarPaciente(PacienteRq pacienteRq) throws BadRequestException {
        return ResponseEntity.ok(pacienteService.guardarPaciente(pacienteRq));
    }

    @Override
    public ResponseEntity<RespuestaRs> actualizarPaciente(PacienteRq pacienteRq) throws BadRequestException {
        return ResponseEntity.ok(pacienteService.actualizarPaciente(pacienteRq));
    }
}

package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.model.PacienteRq;
import com.uniminuto.clinica.model.RespuestaRs;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*")
@RequestMapping("/paciente")
public interface PacienteApi {

    @RequestMapping(value = "/listar",
            produces = "application/json",
            method = RequestMethod.GET)
    ResponseEntity<List<Paciente>> listarPacientes() throws BadRequestException;

    @RequestMapping(value = "/buscar-x-documento",
            produces = "application/json",
            method = RequestMethod.GET)
    ResponseEntity<Paciente> buscarPacientePorDoc(
            @RequestParam String numeroDocumento) throws BadRequestException;

    @RequestMapping(value = "/listar-ordenado-nacimiento",
            produces = "application/json",
            method = RequestMethod.GET)
    ResponseEntity<List<Paciente>> listarPacientesOrdenado() throws BadRequestException;

    @RequestMapping(value = "/guardar",
            produces = "application/json",
            consumes = "application/json",
            method = RequestMethod.POST)
    ResponseEntity<RespuestaRs> guardarPaciente(
            @RequestBody PacienteRq pacienteRq) throws BadRequestException;

    @RequestMapping(value = "/actualizar",
            produces = "application/json",
            consumes = "application/json",
            method = RequestMethod.POST)
    ResponseEntity<RespuestaRs> actualizarPaciente(
            @RequestBody PacienteRq pacienteRq) throws BadRequestException;
}
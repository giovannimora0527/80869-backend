package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Paciente;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author lmora
 */
@CrossOrigin(origins = "*")
@RequestMapping("/paciente")
public interface PacienteApi {

    /**
     * Lista todos los pacientes registrados en el sistema.
     *
     * @return {@link ResponseEntity} que contiene una lista de objetos {@link Paciente}.
     * @throws BadRequestException si ocurre un error al procesar la solicitud.
     *
     */
    @RequestMapping(value = "/listar",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Paciente>> listarPacientes() throws BadRequestException;

    /**
     * Busca un paciente por su número de documento.
     *
     * @param numeroDocumento Número de documento del paciente a buscar.
     * @return {@link ResponseEntity} que contiene el objeto {@link Paciente} encontrado.
     * @throws BadRequestException si el número de documento es inválido
     *                             o si no se encuentra el paciente.
     */
    @RequestMapping(value = "/buscar-x-documento",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<Paciente> buscarPacientePorDoc(
            @RequestParam String numeroDocumento) throws BadRequestException;

    @RequestMapping(value = "/listar-ordenado-nacimiento",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Paciente>> listarPacientesOrdenado() throws BadRequestException;
}

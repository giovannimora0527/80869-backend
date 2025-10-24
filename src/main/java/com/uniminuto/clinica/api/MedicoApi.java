package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Medico;
import java.util.List;

import com.uniminuto.clinica.model.MedicoRq;
import com.uniminuto.clinica.model.RespuestaRs;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 *
 * @author lmora
 */
@CrossOrigin(origins = "*")
@RequestMapping("/medico")
public interface MedicoApi {

    @RequestMapping(value = "/listar",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Medico>> listarMedicos();


    @RequestMapping(value = "/listar-x-especialidad",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Medico>> listarMedicosPorEspecialidad(
            @RequestParam String codigoEspecializacion) throws BadRequestException;


    @RequestMapping(value = "/buscar-por-documento",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<Medico> buscarPorDocumento(
            @RequestParam String documento) throws BadRequestException;

    @RequestMapping(value = "/guardar",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<RespuestaRs> guardarMedico(
            @RequestBody @Valid MedicoRq medicoRq) throws BadRequestException ;

    @RequestMapping(value = "/actualizar",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.PUT)
    ResponseEntity<RespuestaRs> actualizarMedico(
            @RequestParam Long id,
            @RequestBody @Valid MedicoRq medicoRq) throws BadRequestException;
}
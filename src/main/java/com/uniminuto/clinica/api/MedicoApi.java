package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Medico;
import com.uniminuto.clinica.entity.Usuario;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    @RequestParam String documento) throws BadRequestException ;
}
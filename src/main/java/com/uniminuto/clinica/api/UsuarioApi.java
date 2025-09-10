package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Usuario;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.model.UsuarioRq;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author lmora
 */
@CrossOrigin(origins = "*")
@RequestMapping("/usuario")
public interface UsuarioApi {

    @RequestMapping(value = "/listar",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Usuario>> listarUsuarios();

    
    @RequestMapping(value = "/listar-por-rol",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Usuario>> listarUsuariosPorRol(
            @RequestParam String rol
    );
    
    @RequestMapping(value = "/buscar-username",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<Usuario> buscarPorUsername(
            @RequestParam String username
    ) throws BadRequestException;
    
    
    @RequestMapping(value = "/buscar-por-estado",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Usuario>> buscarPorEstado(
            @RequestParam Integer activo
    ) throws BadRequestException;
    
    
    @RequestMapping(value = "/guardar",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<RespuestaRs> guardarUsuarioNuevo(
            @RequestBody UsuarioRq usuario
    ) throws BadRequestException;
}
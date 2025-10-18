package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Especializacion;
import com.uniminuto.clinica.entity.Medico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/especializacion")
public interface EspecializacionApi {

    @RequestMapping(value = "/listar",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Especializacion>> listarEspecializaciones();
}

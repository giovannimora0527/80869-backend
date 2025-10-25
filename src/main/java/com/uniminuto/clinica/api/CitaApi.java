package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.model.CitaRq;
import com.uniminuto.clinica.model.CitaRs; 
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RequestMapping("/cita")
public interface CitaApi {

    @RequestMapping(value = "/guardar",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<RespuestaRs> guardarCita(
            @RequestBody CitaRq citaNueva
    ) throws BadRequestException;
    
    // Cambia el tipo de retorno a List<CitaRs> para una respuesta limpia
    @RequestMapping(value = "/listar-recientes",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<CitaRs>> listarCitasRecientes(); 
}
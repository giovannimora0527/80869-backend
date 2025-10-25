package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.CitaApi;
import com.uniminuto.clinica.model.CitaRq;
import com.uniminuto.clinica.model.CitaRs; 
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.service.CitaService;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CitaApiController implements CitaApi {

    @Autowired
    private CitaService citaService;

    @Override
    public ResponseEntity<RespuestaRs> guardarCita(CitaRq citaNueva) throws BadRequestException {
        return ResponseEntity.ok(this.citaService.guardarCita(citaNueva));
    }
    
    // El tipo de retorno se cambia a List<CitaRs> para una respuesta limpia y optimizada
    @Override
    public ResponseEntity<List<CitaRs>> listarCitasRecientes() {
        return ResponseEntity.ok(this.citaService.listarCitasRecientes());
    }
}
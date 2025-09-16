package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.CitaApi;
import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.service.CitaService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CitaApiController implements CitaApi {
    @Autowired
    CitaService citaService;
    @Override
    public ResponseEntity<String> guardarCita(Cita cita) throws BadRequestException {
        return ResponseEntity.ok(this.citaService.guardarCita(cita));
    }

    @Override
    public ResponseEntity<List<Cita>> listarRecientes() {
        return ResponseEntity.ok(this.citaService.listarReciente());
    }
}

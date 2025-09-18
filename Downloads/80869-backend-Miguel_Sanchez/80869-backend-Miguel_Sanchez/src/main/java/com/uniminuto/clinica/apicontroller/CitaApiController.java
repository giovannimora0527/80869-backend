package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.CitaApi;
import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.service.CitaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para {@link Cita}.
 */
@RestController
public class CitaApiController implements CitaApi {

    @Autowired
    private CitaService citaService;

    @Override
    public ResponseEntity<Cita> crearCita(Cita cita) {
        return ResponseEntity.ok(citaService.crearCita(cita));
    }

    @Override
    public ResponseEntity<List<Cita>> listarCitasRecientes() {
        return ResponseEntity.ok(citaService.listarCitasRecientes());
    }
}



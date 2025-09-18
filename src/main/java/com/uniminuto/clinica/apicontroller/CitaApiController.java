
package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.CitaApi;
import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.service.CitaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CitaApiController implements CitaApi {
    
    
    @Autowired
    private CitaService citaService;

    @Override
    public ResponseEntity<String> guardarCita(Cita cita) {
        citaService.guardarCita(cita);
        return ResponseEntity.ok("Cita guardada con exito");
    }

    @Override
    public ResponseEntity<List<Cita>> listarCitas() {
        return ResponseEntity.ok (this.citaService.listarCitas());
    }

    @Override
    public ResponseEntity<List<Cita>> listarCitasFechaReciente() {
        return ResponseEntity.ok(this.citaService.listarCitasFechaReciente());
    }
    
}

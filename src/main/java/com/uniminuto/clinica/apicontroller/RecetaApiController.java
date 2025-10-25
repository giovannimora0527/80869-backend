package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.RecetaApi;
import com.uniminuto.clinica.model.RecetaRq;
import com.uniminuto.clinica.model.RecetaRs; 
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.service.RecetaService;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
public class RecetaApiController implements RecetaApi {

    @Autowired
    private RecetaService recetaService;

    @Override
    public ResponseEntity<List<RecetaRs>> listarRecetas() { 
        // El controlador llama al servicio que ya devuelve una lista de DTOs.
        return ResponseEntity.ok(this.recetaService.listarRecetas());
    }

    @Override
    public ResponseEntity<RespuestaRs> guardarReceta(@Valid RecetaRq recetaNueva) throws BadRequestException {
        return ResponseEntity.ok(this.recetaService.guardarReceta(recetaNueva));
    }
}
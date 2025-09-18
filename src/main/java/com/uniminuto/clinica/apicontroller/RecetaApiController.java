package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.RecetaApi;
import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.service.RecetaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecetaApiController implements RecetaApi {
    
    @Autowired
    private RecetaService recetaService;
            
    @Override
    public ResponseEntity<String> guardarReceta(Receta receta) {
        recetaService.guardarReceta(receta);
        return ResponseEntity.ok("Cita Guardada con Exito");
        
    }

    @Override
    public ResponseEntity<List<Receta>> listarReceta() {
        return ResponseEntity.ok(this.recetaService.listarRecetas());
    }

    @Override
    public ResponseEntity<List<Receta>> listarRecetasFechaCreacion() {
        return ResponseEntity.ok(this.recetaService.listarRecetasFechaCreacion());
    }


}
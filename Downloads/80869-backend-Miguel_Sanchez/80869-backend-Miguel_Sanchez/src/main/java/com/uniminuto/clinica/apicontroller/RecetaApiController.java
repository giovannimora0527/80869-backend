package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.RecetaApi;
import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.service.RecetaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para {@link Receta}.
 */
@RestController
public class RecetaApiController implements RecetaApi {

    @Autowired
    private RecetaService recetaService;

    @Override
    public ResponseEntity<Receta> crearReceta(Receta receta) {
        return ResponseEntity.ok(recetaService.crearReceta(receta));
    }

    @Override
    public ResponseEntity<List<Receta>> listarRecetas() {
        return ResponseEntity.ok(recetaService.listarRecetas());
    }
}



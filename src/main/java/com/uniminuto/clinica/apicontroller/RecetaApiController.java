package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.RecetaApi;
import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.model.RecetaCrearRq;
import com.uniminuto.clinica.service.RecetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/** Controlador REST para Recetas. */
@RestController
public class RecetaApiController implements RecetaApi {

  private final RecetaService svc;

  public RecetaApiController(RecetaService svc) { this.svc = svc; }

  @Override
  public ResponseEntity<Receta> crear(RecetaCrearRq rq) {
    return ResponseEntity.ok(svc.crear(rq));
  }

  @Override
  public ResponseEntity<List<Receta>> listar() {
    return ResponseEntity.ok(svc.listar());
  }
}
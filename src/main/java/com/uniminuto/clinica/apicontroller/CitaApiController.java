package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.CitaApi;
import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.model.CitaCrearRq;
import com.uniminuto.clinica.service.CitaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/** Controlador REST para Citas. */
@RestController
public class CitaApiController implements CitaApi {

  private final CitaService svc;

  public CitaApiController(CitaService svc) { this.svc = svc; }

  @Override
  public ResponseEntity<Cita> crear(CitaCrearRq rq) {
    return ResponseEntity.ok(svc.crear(rq));
  }

  @Override
  public ResponseEntity<List<Cita>> listar() {
    return ResponseEntity.ok(svc.listarRecientes());
  }
}
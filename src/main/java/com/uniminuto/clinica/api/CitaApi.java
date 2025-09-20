package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.model.CitaCrearRq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*")
@RequestMapping("/cita")
public interface CitaApi {

  @PostMapping(value="/crear", consumes="application/json", produces="application/json")
  ResponseEntity<Cita> crear(@RequestBody CitaCrearRq rq);

  @GetMapping(value="/listar", produces="application/json")
  ResponseEntity<List<Cita>> listar();
}
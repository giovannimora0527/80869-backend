package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.model.RecetaCrearRq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*")
@RequestMapping("/receta")
public interface RecetaApi {

  @PostMapping(value="/crear", consumes="application/json", produces="application/json")
  ResponseEntity<Receta> crear(@RequestBody RecetaCrearRq rq);

  @GetMapping(value="/listar", produces="application/json")
  ResponseEntity<List<Receta>> listar();
}
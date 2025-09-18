
package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Cita;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/cita")
public interface CitaApi {

    @PostMapping("/guardar")
    ResponseEntity<String> guardarCita(@RequestBody Cita cita);

    @GetMapping("/listar")
    ResponseEntity<List<Cita>> listarCitas();

    @GetMapping("/listarFechaReciente")
    ResponseEntity<List<Cita>> listarCitasFechaReciente();
}


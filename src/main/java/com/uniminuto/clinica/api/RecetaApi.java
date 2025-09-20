
package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.model.RecetaRq;
import com.uniminuto.clinica.model.RespuestaRs;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/receta")
public interface RecetaApi {

    @PostMapping("/guardar")
    ResponseEntity<RespuestaRs> guardarReceta(@RequestBody RecetaRq recetaRq);

    @GetMapping("/listar")
    ResponseEntity<List<Receta>> listarReceta();

    @GetMapping("/listarFechaCreacion")
    ResponseEntity<List<Receta>> listarRecetasFechaCreacion();
}

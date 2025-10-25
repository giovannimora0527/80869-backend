package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Especializacion;
import com.uniminuto.clinica.model.RespuestaRs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RequestMapping("/especializacion")
public interface EspecializacionApi {

    @GetMapping("/listar")
    ResponseEntity<List<Especializacion>> listarEspecializaciones();

    @GetMapping("/buscar/{codigo}")
    ResponseEntity<Especializacion> buscarPorCodigo(@PathVariable String codigo);

    @PostMapping("/guardar")
    ResponseEntity<RespuestaRs> guardarEspecializacion(@Valid @RequestBody Especializacion request);

    @PutMapping("/actualizar/{id}")
    ResponseEntity<RespuestaRs> actualizarEspecializacion(@PathVariable Long id, @Valid @RequestBody Especializacion request);
}
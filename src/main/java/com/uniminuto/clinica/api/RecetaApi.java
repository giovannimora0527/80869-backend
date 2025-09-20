package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.model.RecetaRq;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST para la gestión de recetas médicas.
 * Proporciona endpoints para guardar recetas y listar todas las registradas.
 */
@CrossOrigin(origins = "*")
@RequestMapping("/receta")
public interface RecetaApi {
    /**
     * Guarda una nueva receta médica en el sistema.
     *
     * @param receta Objeto {@link Receta} que contiene los datos de la receta a registrar.
     * @return {@link ResponseEntity} con un mensaje indicando el resultado de la operación.
     * @throws BadRequestException si los datos de entrada no son válidos.
     *
     */
    @PostMapping(value = "/guardarReceta",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<String> guardarReceta(@RequestBody RecetaRq receta) throws BadRequestException;

    /**
     * Lista todas las recetas médicas registradas en el sistema.
     *
     * @return {@link ResponseEntity} con una lista de objetos {@link Receta}.
     *
     */
    @GetMapping(value = "/listar",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<List<Receta>> listarRecetas();
}

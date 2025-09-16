package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Cita;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST para la gestión de citas médicas.
 * Proporciona endpoints para guardar nuevas citas y listar citas recientes.
 */
@CrossOrigin(origins = "*")
@RequestMapping(value = "/cita")
public interface CitaApi {

    /**
     * Guarda una nueva cita médica en el sistema.
     *
     * @param cita Objeto {@link Cita} que contiene la información de la cita a guardar.
     * @return {@link ResponseEntity} con un mensaje de confirmación o error.
     * @throws BadRequestException si la solicitud contiene datos inválidos.
     *
     */
    @PostMapping(value = "/guardarCita",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<String> guardarCita(@RequestBody Cita cita) throws BadRequestException;

    /**
     * Lista las citas más recientes registradas en el sistema.
     *
     * @return {@link ResponseEntity} que contiene una lista de objetos {@link Cita}.
     *
     */
    @GetMapping(value = "/listarRecientes",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<List<Cita>> listarRecientes();

}

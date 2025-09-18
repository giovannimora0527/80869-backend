package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Cita;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * API para operaciones de {@link Cita}.
 */
@CrossOrigin(origins = "*")
@RequestMapping("/cita")
public interface CitaApi {

    /**
     * Crea una nueva cita relacionando paciente y médico.
     * @param cita cuerpo de la solicitud
     * @return cita creada
     */
    @RequestMapping(value = "/crear",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<Cita> crearCita(@RequestBody Cita cita);

    /**
     * Lista las citas por fecha y hora descendente.
     * @return listado de citas
     */
    @RequestMapping(value = "/listar-recientes",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Cita>> listarCitasRecientes();
}



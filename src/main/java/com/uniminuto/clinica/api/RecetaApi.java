package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Receta;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * API para operaciones de {@link Receta}.
 */
@CrossOrigin(origins = "*")
@RequestMapping("/receta")
public interface RecetaApi {

    /**
     * Crea una receta asociada a una cita existente.
     * @param receta cuerpo de la solicitud
     * @return receta creada
     */
    @RequestMapping(value = "/crear",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<Receta> crearReceta(@RequestBody Receta receta);

    /**
     * Lista todas las recetas (más recientes primero por fecha de creación).
     * @return listado de recetas
     */
    @RequestMapping(value = "/listar",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Receta>> listarRecetas();
}



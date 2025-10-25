package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.model.RecetaRq;
import com.uniminuto.clinica.model.RecetaRs;
import com.uniminuto.clinica.model.RespuestaRs;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Interfaz que define los endpoints (API REST) para la gestión de recetas en la clínica.
 * Permite realizar operaciones como guardar y listar recetas médicas.
 */
@CrossOrigin(origins = "*")
@RequestMapping("/receta")
public interface RecetaApi {

    /**
     * Endpoint para guardar una nueva receta en el sistema.
     * Recibe los datos de la receta a través de una solicitud POST.
     * @param recetaNueva El objeto RecetaRq que contiene la información de la nueva receta.
     * @return Un objeto ResponseEntity que envuelve una RespuestaRs con el resultado de la operación.
     * @throws BadRequestException Si la solicitud no es válida o faltan datos requeridos.
     */
    @PostMapping("/guardar")
    ResponseEntity<RespuestaRs> guardarReceta(@Valid @RequestBody RecetaRq recetaNueva) throws BadRequestException;
    
    
    /**
     * Endpoint para obtener una lista de todas las recetas existentes.
     * @return Un objeto ResponseEntity que contiene una lista de objetos RecetaRs.
     * Esta respuesta utiliza un DTO para evitar la serialización excesiva.
     */
    @GetMapping("/listar")
    ResponseEntity<List<RecetaRs>> listarRecetas();
}
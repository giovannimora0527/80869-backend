package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Receta;
import java.util.List;

/**
 * Servicio para gestionar operaciones de {@link Receta}.
 */
public interface RecetaService {
    /**
     * Crea una nueva receta asociada a una cita.
     * @param receta entidad a persistir
     * @return receta creada
     */
    Receta crearReceta(Receta receta);

    /**
     * Lista todas las recetas.
     * @return listado general de recetas
     */
    List<Receta> listarRecetas();
}



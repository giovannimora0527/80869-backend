package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Cita;
import java.util.List;

/**
 * Servicio para gestionar operaciones de {@link Cita}.
 */
public interface CitaService {
    /**
     * Crea una nueva cita.
     * @param cita entidad a persistir
     * @return cita creada
     */
    Cita crearCita(Cita cita);

    /**
     * Lista las citas en orden descendente por fecha y hora.
     * @return citas más recientes primero
     */
    List<Cita> listarCitasRecientes();
}



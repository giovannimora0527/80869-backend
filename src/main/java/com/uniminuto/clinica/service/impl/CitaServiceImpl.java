package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.repository.CitaRepository;
import com.uniminuto.clinica.service.CitaService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación de la interfaz {@link CitaService}.
 * Contiene la lógica de negocio para el manejo de citas médicas,
 * incluyendo el almacenamiento y la consulta de citas recientes.
 */
@Service
public class CitaServiceImpl implements CitaService {
    @Autowired
    CitaRepository citaRepository;

    /**
     * Guarda una nueva cita médica en la base de datos.
     *
     * @param cita Objeto {@link Cita} que contiene los datos de la cita a guardar.
     * @return Mensaje indicando si la operación fue exitosa.
     * @throws BadRequestException si ocurre un error al guardar la cita.
     */
    @Override
    public String guardarCita(Cita cita) throws BadRequestException {
        try{
            this.citaRepository.save(cita);
        }catch (Exception bex){
            throw new BadRequestException("No se guardo la cita correctamente.");
        }
        return "Se guardó la cita correctamente.";
    }

    /**
     * Retorna una lista de citas ordenadas por fecha y hora en orden ascendente.
     * Este método puede usarse para mostrar las citas más recientes primero.
     *
     * @return Lista de objetos {@link Cita} ordenados por fecha y hora.
     */
    @Override
    public List<Cita> listarReciente() {
        return this.citaRepository.findAllByOrderByFechaHoraAsc();
    }
}

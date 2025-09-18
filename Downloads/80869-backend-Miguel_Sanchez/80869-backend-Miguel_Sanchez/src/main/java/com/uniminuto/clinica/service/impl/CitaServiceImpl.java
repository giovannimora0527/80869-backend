package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.repository.CitaRepository;
import com.uniminuto.clinica.service.CitaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio {@link CitaService}.
 */
@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private CitaRepository citaRepository;

    /** {@inheritDoc} */
    @Override
    public Cita crearCita(Cita cita) {
        return citaRepository.save(cita);
    }

    /** {@inheritDoc} */
    @Override
    public List<Cita> listarCitasRecientes() {
        return citaRepository.findAllByOrderByFechaHoraDesc();
    }
}



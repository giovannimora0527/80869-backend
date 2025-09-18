package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.repository.RecetaRepository;
import com.uniminuto.clinica.service.RecetaService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio {@link RecetaService}.
 */
@Service
public class RecetaServiceImpl implements RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    /** {@inheritDoc} */
    @Override
    public Receta crearReceta(Receta receta) {
        receta.setFechaCreacionRegistro(LocalDateTime.now());
        return recetaRepository.save(receta);
    }

    /** {@inheritDoc} */
    @Override
    public List<Receta> listarRecetas() {
        return recetaRepository.findAllByOrderByFechaCreacionRegistroDesc();
    }
}



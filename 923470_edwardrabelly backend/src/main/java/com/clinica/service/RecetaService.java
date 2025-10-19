package com.clinica.service;

import com.clinica.entity.Receta;
import com.clinica.repository.RecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    public List<Receta> listar() {
        return recetaRepository.findAll();
    }

    public Optional<Receta> obtenerPorId(Long id) {
        return recetaRepository.findById(id);
    }

    public Receta guardar(Receta receta) {
        return recetaRepository.save(receta);
    }

    public Receta actualizar(Long id, Receta recetaActualizada) {
        return recetaRepository.findById(id).map(receta -> {
            receta.setPaciente(recetaActualizada.getPaciente());
            receta.setMedico(recetaActualizada.getMedico());
            receta.setMedicamento(recetaActualizada.getMedicamento());
            receta.setIndicaciones(recetaActualizada.getIndicaciones());
            receta.setFechaEmision(recetaActualizada.getFechaEmision());
            return recetaRepository.save(receta);
        }).orElseThrow(() -> new RuntimeException("Receta no encontrada"));
    }

    public void eliminar(Long id) {
        recetaRepository.deleteById(id);
    }
}

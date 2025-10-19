package com.clinica.service;

import com.clinica.entity.HistoriaMedica;
import com.clinica.repository.HistoriaMedicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoriaMedicaService {

    @Autowired
    private HistoriaMedicaRepository historiaMedicaRepository;

    public List<HistoriaMedica> listar() {
        return historiaMedicaRepository.findAll();
    }

    public Optional<HistoriaMedica> obtenerPorId(Long id) {
        return historiaMedicaRepository.findById(id);
    }

    public HistoriaMedica guardar(HistoriaMedica historiaMedica) {
        return historiaMedicaRepository.save(historiaMedica);
    }

    public void eliminar(Long id) {
        historiaMedicaRepository.deleteById(id);
    }
}

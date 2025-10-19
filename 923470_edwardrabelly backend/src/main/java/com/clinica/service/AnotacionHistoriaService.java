package com.clinica.service;

import com.clinica.entity.AnotacionHistoria;
import com.clinica.repository.AnotacionHistoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnotacionHistoriaService {

    @Autowired
    private AnotacionHistoriaRepository anotacionHistoriaRepository;

    public List<AnotacionHistoria> listar() {
        return anotacionHistoriaRepository.findAll();
    }

    public Optional<AnotacionHistoria> obtenerPorId(Long id) {
        return anotacionHistoriaRepository.findById(id);
    }

    public AnotacionHistoria guardar(AnotacionHistoria anotacionHistoria) {
        return anotacionHistoriaRepository.save(anotacionHistoria);
    }

    public void eliminar(Long id) {
        anotacionHistoriaRepository.deleteById(id);
    }
}

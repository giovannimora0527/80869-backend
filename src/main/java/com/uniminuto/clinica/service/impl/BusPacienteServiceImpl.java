package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.repository.BusPacienteRepository;
import com.uniminuto.clinica.service.BusPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class BusPacienteServiceImpl implements BusPacienteService {

    private final BusPacienteRepository busPacienteRepository;

    @Autowired
    public BusPacienteServiceImpl(BusPacienteRepository busPacienteRepository) {
        this.busPacienteRepository = busPacienteRepository;
    }

    @Override
    public Optional<Paciente> buscarPorDocumento(String documento) {
        return busPacienteRepository.findByNumeroDocumento(documento);
    }
}
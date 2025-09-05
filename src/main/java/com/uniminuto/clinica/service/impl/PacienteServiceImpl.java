package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.repository.PacienteRepository;
import com.uniminuto.clinica.service.PacienteService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    PacienteRepository pacienteRepository;

    @Override
    public List<Paciente> listaPacientes() {
        return pacienteRepository.findAll();
    }

    @Override
    public Paciente buscarPorDocumento(String documento) throws BadRequestException {
        Optional<Paciente> pacienteOpt = pacienteRepository.findByNumeroDocumento(documento);
        if(!pacienteOpt.isPresent()){
            throw new BadRequestException("No existe paciente con Numero de Documento: "+documento);
        }
        return pacienteOpt.get() ;
    }
}

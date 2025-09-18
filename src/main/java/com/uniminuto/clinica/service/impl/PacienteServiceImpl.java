package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.repository.PacienteRepository;
import com.uniminuto.clinica.service.PacienteService;
import java.util.List;
import java.util.Optional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Override
    public List<Paciente> encontrarPacienteFecha() {
        return pacienteRepository.findAllByOrderByFechanacimiento();
    }
    
    @Override
    public List<Paciente> encontrarTodosLosPacientes() {
        return pacienteRepository.findAll();
    }

    @Override
    public List<Paciente> buscarPorGenero(String genero) {
        return pacienteRepository.findByGenero(genero);
    }

    @Override
    public List<Paciente> buscarPorNombre(String nombre) throws BadRequestException {
    List<Paciente> pacientes = pacienteRepository.findByNombre(nombre);
    if (pacientes.isEmpty()) {
        throw new BadRequestException("No se encontraron pacientes con el nombre: " + nombre);
    }
    return pacientes;
    
    }
    
    }

    

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.repository.PacienteRepository;
import com.uniminuto.clinica.service.PacienteService;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jsarmientol
 */
@Service
public class PacienteServiceImpl implements PacienteService{
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
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
    
    @Override
    public List<Paciente> listarPacientesPorFechaNacimiento() {
    return pacienteRepository.findAllByOrderByFechanacimientoAsc();
}
}
   
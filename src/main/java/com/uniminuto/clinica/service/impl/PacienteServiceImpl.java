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
    private PacienteRepository repoPaciente;

    @Override
    public List<Paciente> obtenerPacientes() {
        return repoPaciente.findAll();
    }

    @Override
    public Paciente buscarPorDoc(String nroDocumento) throws BadRequestException {
        Optional<Paciente> encontrado = repoPaciente.findByDocNumero(nroDocumento);
        if (!encontrado.isPresent()) {
            throw new BadRequestException("No se encontró paciente con documento: " + nroDocumento);
        }
        return encontrado.get();
    }
}

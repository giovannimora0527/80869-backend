package com.clinica.service;

import com.clinica.entity.Medico;
import com.clinica.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    public List<Medico> listar() {
        return medicoRepository.findAll();
    }

    public Optional<Medico> obtenerPorId(Long id) {
        return medicoRepository.findById(id);
    }

    public Medico guardar(Medico medico) {
        return medicoRepository.save(medico);
    }

    public Medico actualizar(Long id, Medico medicoActualizado) {
        return medicoRepository.findById(id).map(medico -> {
            medico.setTipoDocumento(medicoActualizado.getTipoDocumento());
            medico.setNumeroDocumento(medicoActualizado.getNumeroDocumento());
            medico.setNombres(medicoActualizado.getNombres());
            medico.setApellidos(medicoActualizado.getApellidos());
            medico.setTelefono(medicoActualizado.getTelefono());
            medico.setRegistroProfesional(medicoActualizado.getRegistroProfesional());
            medico.setEspecializacion(medicoActualizado.getEspecializacion());
            return medicoRepository.save(medico);
        }).orElseThrow(() -> new RuntimeException("Médico no encontrado"));
    }

    public void eliminar(Long id) {
        medicoRepository.deleteById(id);
    }
}

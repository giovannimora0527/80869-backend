package com.clinica.service;

import com.clinica.entity.Cita;
import com.clinica.repository.CitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    private final CitaRepository citaRepository;

    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    // Crear o actualizar una cita
    public Cita guardar(Cita cita) {
        return citaRepository.save(cita);
    }

    // Listar todas las citas
    public List<Cita> listarTodas() {
        return citaRepository.findAll();
    }

    // Buscar cita por ID
    public Optional<Cita> buscarPorId(Long id) {
        return citaRepository.findById(id);
    }

    // Eliminar cita por ID
    public void eliminar(Long id) {
        citaRepository.deleteById(id);
    }

    // Listar citas más recientes primero (por fecha y hora descendente)
    public List<Cita> listarPorFechaHoraDesc() {
        return citaRepository.findAllByOrderByFechaHoraDesc();
    }
    // 👉 Nuevo método en CitaService
    public List<Cita> listarTodasRecientes() {
        return citaRepository.findAllByOrderByFechaHoraDesc();
    }
}

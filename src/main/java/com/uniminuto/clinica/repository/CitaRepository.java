package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.entity.Medico;
import com.uniminuto.clinica.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {

    List<Cita> findAllByOrderByFechaDesc();

    List<Cita> findByMedicoAndFechaBetween(Medico medicoId, LocalDateTime start, LocalDateTime end);

    List<Cita> findByPacienteAndFechaBetween(Paciente medicoId, LocalDateTime start, LocalDateTime end);
}

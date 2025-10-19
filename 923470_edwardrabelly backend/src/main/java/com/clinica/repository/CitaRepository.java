package com.clinica.repository;

import com.clinica.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    // Método personalizado para listar citas más recientes primero
    List<Cita> findAllByOrderByFechaHoraDesc();
}

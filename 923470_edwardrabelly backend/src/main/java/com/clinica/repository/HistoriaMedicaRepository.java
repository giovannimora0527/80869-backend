package com.clinica.repository;

import com.clinica.entity.HistoriaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriaMedicaRepository extends JpaRepository<HistoriaMedica, Long> {
}

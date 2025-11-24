package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.AuditoriaRecuperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaRecuperacionRepository extends JpaRepository<AuditoriaRecuperacion, Long> {
}


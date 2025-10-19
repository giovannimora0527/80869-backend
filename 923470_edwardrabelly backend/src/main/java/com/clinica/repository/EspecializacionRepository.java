package com.clinica.repository;

import com.clinica.entity.Especializacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecializacionRepository extends JpaRepository<Especializacion, Long> {
}

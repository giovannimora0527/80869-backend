package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {

    /**
     * Obtiene todas las recetas ordenadas por fecha de creación en orden descendente.
     */
    List<Receta> findAllByOrderByFechaCreacionRegistroDesc();
}
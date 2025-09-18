package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.Receta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acceso a datos para la entidad {@link Receta}.
 */
@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
    // Métodos derivados JPA si son necesarios en el futuro
    List<Receta> findAllByOrderByFechaCreacionRegistroDesc();
}



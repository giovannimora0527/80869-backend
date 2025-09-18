package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.Cita;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acceso a datos para la entidad {@link Cita}.
 */
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    /**
     * Lista todas las citas ordenadas por fecha y hora de forma descendente.
     * @return listado de citas más recientes primero
     */
    List<Cita> findAllByOrderByFechaHoraDesc();
}



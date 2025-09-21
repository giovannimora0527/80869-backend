package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.AnotacionHistoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad {@link AnotacionHistoria}.
 * Proporciona operaciones CRUD y consultas personalizadas para anotaciones de historias médicas.
 *
 * @author Sistema Clínica
 */
@Repository
public interface AnotacionHistoriaRepository extends JpaRepository<AnotacionHistoria, Long> {
    
    /**
     * Busca todas las anotaciones de una historia médica específica
     * ordenadas por fecha descendente (más recientes primero).
     *
     * @param historiaId ID de la historia médica
     * @return Lista de anotaciones ordenadas por fecha
     */
    List<AnotacionHistoria> findByHistoriaIdOrderByFechaDesc(Long historiaId);
    
    /**
     * Busca todas las anotaciones realizadas por un médico específico
     * ordenadas por fecha descendente.
     *
     * @param medicoId ID del médico
     * @return Lista de anotaciones del médico
     */
    List<AnotacionHistoria> findByMedicoIdOrderByFechaDesc(Long medicoId);
    
    /**
     * Lista todas las anotaciones ordenadas por fecha descendente.
     *
     * @return Lista de todas las anotaciones ordenadas
     */
    List<AnotacionHistoria> findAllByOrderByFechaDesc();
    
    /**
     * Cuenta el número de anotaciones en una historia médica específica.
     *
     * @param historiaId ID de la historia médica
     * @return Número de anotaciones
     */
    long countByHistoriaId(Long historiaId);
}
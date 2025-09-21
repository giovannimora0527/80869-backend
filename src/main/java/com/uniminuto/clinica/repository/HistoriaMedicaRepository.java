package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.HistoriaMedica;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad {@link HistoriaMedica}.
 * Proporciona operaciones CRUD y consultas personalizadas para historias médicas.
 *
 * @author Sistema Clínica
 */
@Repository
public interface HistoriaMedicaRepository extends JpaRepository<HistoriaMedica, Long> {
    
    /**
     * Busca la historia médica de un paciente específico por su ID.
     *
     * @param pacienteId ID del paciente
     * @return Optional con la historia médica del paciente
     */
    Optional<HistoriaMedica> findByPacienteId(Long pacienteId);
    
    /**
     * Lista todas las historias médicas ordenadas por fecha de creación descendente.
     *
     * @return Lista de historias médicas ordenadas
     */
    List<HistoriaMedica> findAllByOrderByFechaCreacionDesc();
    
    /**
     * Verifica si existe una historia médica para un paciente específico.
     *
     * @param pacienteId ID del paciente
     * @return true si existe, false en caso contrario
     */
    boolean existsByPacienteId(Long pacienteId);
}
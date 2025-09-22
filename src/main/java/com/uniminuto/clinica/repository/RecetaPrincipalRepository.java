package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.RecetaPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para el acceso a datos de recetas principales.
 * Maneja las operaciones CRUD y consultas específicas para recetas médicas.
 * 
 * @author AI
 */
@Repository
public interface RecetaPrincipalRepository extends JpaRepository<RecetaPrincipal, Long> {
    
    /**
     * Busca todas las recetas asociadas a una cita específica.
     * 
     * @param citaId ID de la cita
     * @return Lista de recetas de la cita
     */
    @Query("SELECT r FROM RecetaPrincipal r WHERE r.cita.id = :citaId ORDER BY r.fechaCreacion DESC")
    List<RecetaPrincipal> findByCitaId(@Param("citaId") Long citaId);
    
    /**
     * Busca recetas por médico en un rango de fechas.
     * 
     * @param medicoId ID del médico
     * @return Lista de recetas del médico
     */
    @Query("SELECT r FROM RecetaPrincipal r WHERE r.medico.id = :medicoId ORDER BY r.fechaCreacion DESC")
    List<RecetaPrincipal> findByMedicoId(@Param("medicoId") Long medicoId);
    
    /**
     * Busca recetas por paciente.
     * 
     * @param pacienteId ID del paciente
     * @return Lista de recetas del paciente
     */
    @Query("SELECT r FROM RecetaPrincipal r WHERE r.paciente.id = :pacienteId ORDER BY r.fechaCreacion DESC")
    List<RecetaPrincipal> findByPacienteId(@Param("pacienteId") Long pacienteId);
    
    /**
     * Verifica si ya existe una receta activa para una cita específica.
     * 
     * @param citaId ID de la cita
     * @return true si ya existe una receta activa
     */
    @Query("SELECT COUNT(r) > 0 FROM RecetaPrincipal r WHERE r.cita.id = :citaId AND r.estado = 'ACTIVA'")
    boolean existeRecetaActivaParaCita(@Param("citaId") Long citaId);
    
    /**
     * Busca una receta con todos sus medicamentos cargados.
     * 
     * @param recetaId ID de la receta
     * @return Receta con medicamentos
     */
    @Query("SELECT r FROM RecetaPrincipal r " +
           "LEFT JOIN FETCH r.medicamentos m " +
           "LEFT JOIN FETCH m.medicamento " +
           "WHERE r.id = :recetaId")
    Optional<RecetaPrincipal> findByIdWithMedicamentos(@Param("recetaId") Long recetaId);
}
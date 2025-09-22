package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * Repositorio para el acceso a datos de medicamentos.
 * Maneja las operaciones CRUD y consultas específicas para medicamentos.
 * 
 * @author AI
 */
@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    
    /**
     * Busca un medicamento por su nombre exacto.
     * 
     * @param nombre Nombre del medicamento
     * @return Medicamento encontrado
     */
    Optional<Medicamento> findByNombre(String nombre);
    
    /**
     * Busca medicamentos cuyo nombre contenga el texto especificado.
     * 
     * @param nombre Parte del nombre a buscar
     * @return Lista de medicamentos que coinciden
     */
    @Query("SELECT m FROM Medicamento m WHERE UPPER(m.nombre) LIKE UPPER(CONCAT('%', :nombre, '%'))")
    List<Medicamento> findByNombreContaining(@Param("nombre") String nombre);
    
    /**
     * Lista todos los medicamentos activos.
     * 
     * @return Lista de medicamentos activos
     */
    @Query("SELECT m FROM Medicamento m ORDER BY m.nombre")
    List<Medicamento> findAllOrderByNombre();
}
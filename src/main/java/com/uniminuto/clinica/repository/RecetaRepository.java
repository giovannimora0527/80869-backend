package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.Receta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad {@link Receta}.
 * 
 * <p>Esta interfaz extiende JpaRepository para proporcionar operaciones CRUD básicas
 * y agrega consultas personalizadas para el listado y filtrado de recetas médicas.</p>
 * 
 * <p>Funcionalidades principales:</p>
 * <ul>
 *   <li>Consultas optimizadas con JOIN FETCH para evitar problema N+1</li>
 *   <li>Filtrado dinámico por múltiples criterios</li>
 *   <li>Soporte completo para paginación</li>
 *   <li>Búsqueda de texto en campos específicos</li>
 * </ul>
 * 
 * @author Daniel Donado
 * @version 1.0
 * @since 2024-09-21
 */
@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
    
    /**
     * Busca todas las recetas ordenadas por fecha de creación descendente.
     * 
     * <p>Este método proporciona compatibilidad con la funcionalidad anterior
     * del sistema. Se recomienda usar los métodos con paginación para mejor rendimiento.</p>
     * 
     * @return Lista completa de recetas ordenadas por fecha de creación (más recientes primero)
     * @deprecated Usar {@link #findRecetasConFiltros} con paginación para mejor rendimiento
     */
    @Deprecated
    List<Receta> findAllByOrderByFechaCreacionRegistroDesc();
    
    /**
     * Busca recetas aplicando filtros dinámicos con soporte completo de paginación.
     * 
     * <p>Esta consulta utiliza JOIN FETCH para optimizar la carga de entidades relacionadas
     * y evitar el problema N+1. Todos los parámetros de filtro son opcionales y se aplican
     * con lógica AND cuando están presentes.</p>
     * 
     * <p>Entidades cargadas eagerly:</p>
     * <ul>
     *   <li>Cita asociada y su paciente y médico</li>
     *   <li>Medicamento prescrito</li>
     * </ul>
     * 
     * @param pacienteId Identificador del paciente (null para ignorar filtro)
     * @param medicoId Identificador del médico prescriptor (null para ignorar filtro)
     * @param medicamentoId Identificador del medicamento (null para ignorar filtro)
     * @param fechaDesde Fecha mínima de creación inclusive (null para ignorar filtro)
     * @param fechaHasta Fecha máxima de creación inclusive (null para ignorar filtro)
     * @param texto Texto a buscar en dosis o indicaciones usando LIKE (null para ignorar filtro)
     * @param pageable Configuración de paginación y ordenamiento
     * @return Página de recetas que cumplen con los criterios de filtrado
     */
    @Query("SELECT r FROM Receta r " +
           "LEFT JOIN FETCH r.cita c " +
           "LEFT JOIN FETCH c.paciente p " +
           "LEFT JOIN FETCH c.medico m " +
           "LEFT JOIN FETCH r.medicamento med " +
           "WHERE (:pacienteId IS NULL OR p.id = :pacienteId) " +
           "AND (:medicoId IS NULL OR m.id = :medicoId) " +
           "AND (:medicamentoId IS NULL OR med.id = :medicamentoId) " +
           "AND (:fechaDesde IS NULL OR r.fechaCreacionRegistro >= :fechaDesde) " +
           "AND (:fechaHasta IS NULL OR r.fechaCreacionRegistro <= :fechaHasta) " +
           "AND (:texto IS NULL OR " +
           "     LOWER(r.dosis) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "     LOWER(r.indicaciones) LIKE LOWER(CONCAT('%', :texto, '%')))")
    Page<Receta> findRecetasConFiltros(
            @Param("pacienteId") Long pacienteId,
            @Param("medicoId") Long medicoId,
            @Param("medicamentoId") Long medicamentoId,
            @Param("fechaDesde") LocalDateTime fechaDesde,
            @Param("fechaHasta") LocalDateTime fechaHasta,
            @Param("texto") String texto,
            Pageable pageable);
    
    /**
     * Cuenta el total de recetas que cumplen con los criterios de filtrado especificados.
     * 
     * <p>Esta consulta complementa al método de búsqueda paginada, proporcionando
     * el conteo total necesario para la implementación correcta de la paginación.
     * Utiliza los mismos filtros que la consulta principal sin JOIN FETCH para optimizar rendimiento.</p>
     * 
     * @param pacienteId Identificador del paciente (null para ignorar filtro)
     * @param medicoId Identificador del médico prescriptor (null para ignorar filtro)
     * @param medicamentoId Identificador del medicamento (null para ignorar filtro)
     * @param fechaDesde Fecha mínima de creación inclusive (null para ignorar filtro)
     * @param fechaHasta Fecha máxima de creación inclusive (null para ignorar filtro)
     * @param texto Texto a buscar en dosis o indicaciones usando LIKE (null para ignorar filtro)
     * @return Cantidad total de recetas que cumplen con todos los criterios especificados
     */
    @Query("SELECT COUNT(r) FROM Receta r " +
           "LEFT JOIN r.cita c " +
           "LEFT JOIN c.paciente p " +
           "LEFT JOIN c.medico m " +
           "LEFT JOIN r.medicamento med " +
           "WHERE (:pacienteId IS NULL OR p.id = :pacienteId) " +
           "AND (:medicoId IS NULL OR m.id = :medicoId) " +
           "AND (:medicamentoId IS NULL OR med.id = :medicamentoId) " +
           "AND (:fechaDesde IS NULL OR r.fechaCreacionRegistro >= :fechaDesde) " +
           "AND (:fechaHasta IS NULL OR r.fechaCreacionRegistro <= :fechaHasta) " +
           "AND (:texto IS NULL OR " +
           "     LOWER(r.dosis) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "     LOWER(r.indicaciones) LIKE LOWER(CONCAT('%', :texto, '%')))")
    Long countRecetasConFiltros(
            @Param("pacienteId") Long pacienteId,
            @Param("medicoId") Long medicoId,
            @Param("medicamentoId") Long medicamentoId,
            @Param("fechaDesde") LocalDateTime fechaDesde,
            @Param("fechaHasta") LocalDateTime fechaHasta,
            @Param("texto") String texto);
    
    /**
     * Busca todas las recetas prescritas para un paciente específico.
     * 
     * <p>Consulta optimizada para obtener el historial médico completo de un paciente,
     * incluyendo todas las entidades relacionadas mediante JOIN FETCH.</p>
     * 
     * @param pacienteId Identificador único del paciente
     * @param pageable Configuración de paginación y ordenamiento
     * @return Página de recetas prescritas al paciente especificado
     */
    @Query("SELECT r FROM Receta r " +
           "LEFT JOIN FETCH r.cita c " +
           "LEFT JOIN FETCH c.paciente p " +
           "LEFT JOIN FETCH c.medico m " +
           "LEFT JOIN FETCH r.medicamento med " +
           "WHERE p.id = :pacienteId")
    Page<Receta> findByPacienteId(@Param("pacienteId") Long pacienteId, Pageable pageable);
    
    /**
     * Busca todas las recetas prescritas por un médico específico.
     * 
     * <p>Consulta optimizada para obtener el historial de prescripciones de un médico,
     * útil para reportes y seguimiento de patrones de prescripción.</p>
     * 
     * @param medicoId Identificador único del médico prescriptor
     * @param pageable Configuración de paginación y ordenamiento
     * @return Página de recetas prescritas por el médico especificado
     */
    @Query("SELECT r FROM Receta r " +
           "LEFT JOIN FETCH r.cita c " +
           "LEFT JOIN FETCH c.paciente p " +
           "LEFT JOIN FETCH c.medico m " +
           "LEFT JOIN FETCH r.medicamento med " +
           "WHERE m.id = :medicoId")
    Page<Receta> findByMedicoId(@Param("medicoId") Long medicoId, Pageable pageable);
}



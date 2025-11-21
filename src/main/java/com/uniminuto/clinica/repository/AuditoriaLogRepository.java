package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.AuditoriaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para AuditoriaLog.
 *
 * @author lmora
 */
@Repository
public interface AuditoriaLogRepository extends JpaRepository<AuditoriaLog, Long> {

    /**
     * Busca logs por tipo de evento.
     */
    List<AuditoriaLog> findByTipoEvento(String tipoEvento);

    /**
     * Busca logs por usuario.
     */
    List<AuditoriaLog> findByUsuarioId(Long usuarioId);

    /**
     * Busca logs por username.
     */
    List<AuditoriaLog> findByUsername(String username);

    /**
     * Busca logs por nivel.
     */
    List<AuditoriaLog> findByNivel(String nivel);

    /**
     * Busca logs en un rango de fechas.
     */
    List<AuditoriaLog> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Busca logs con múltiples filtros.
     */
    @Query("SELECT a FROM AuditoriaLog a WHERE " +
           "(:tipoEvento IS NULL OR a.tipoEvento = :tipoEvento) AND " +
           "(:username IS NULL OR a.username LIKE %:username%) AND " +
           "(:nivel IS NULL OR a.nivel = :nivel) AND " +
           "(:fechaInicio IS NULL OR a.fechaHora >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR a.fechaHora <= :fechaFin) " +
           "ORDER BY a.fechaHora DESC")
    List<AuditoriaLog> buscarConFiltros(
        @Param("tipoEvento") String tipoEvento,
        @Param("username") String username,
        @Param("nivel") String nivel,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin
    );

    /**
     * Cuenta eventos por tipo.
     */
    @Query("SELECT a.tipoEvento, COUNT(a) FROM AuditoriaLog a GROUP BY a.tipoEvento")
    List<Object[]> contarPorTipoEvento();

    /**
     * Cuenta eventos por nivel.
     */
    @Query("SELECT a.nivel, COUNT(a) FROM AuditoriaLog a GROUP BY a.nivel")
    List<Object[]> contarPorNivel();

    /**
     * Obtiene los últimos N logs.
     */
    List<AuditoriaLog> findTop100ByOrderByFechaHoraDesc();
}

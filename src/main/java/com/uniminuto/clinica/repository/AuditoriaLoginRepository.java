package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.AuditoriaLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AuditoriaLoginRepository extends JpaRepository<AuditoriaLogin, Long> {

    @Query("""
        SELECT a FROM AuditoriaLogin a
        WHERE (:username IS NULL OR a.username LIKE %:username%)
          AND (:fechaDesde IS NULL OR a.fecha >= :fechaDesde)
          AND (:fechaHasta IS NULL OR a.fecha <= :fechaHasta)
        """)
    Page<AuditoriaLogin> buscarConFiltros(
            @Param("username") String username,
            @Param("fechaDesde") LocalDateTime fechaDesde,
            @Param("fechaHasta") LocalDateTime fechaHasta,
            Pageable pageable
    );
}



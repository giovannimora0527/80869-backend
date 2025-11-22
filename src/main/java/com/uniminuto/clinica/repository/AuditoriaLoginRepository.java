package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.AuditoriaLogin;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaLoginRepository extends JpaRepository<AuditoriaLogin, Long> {

    @Query("SELECT a FROM AuditoriaLogin a WHERE (:usuario IS NULL OR a.usuario LIKE %:usuario%) "
	    + "AND (:resultado IS NULL OR a.resultado = :resultado) "
	    + "AND (:start IS NULL OR a.fechaHora >= :start) "
	    + "AND (:end IS NULL OR a.fechaHora <= :end)")
    Page<AuditoriaLogin> search(
	    @Param("usuario") String usuario,
	    @Param("resultado") String resultado,
	    @Param("start") LocalDateTime start,
	    @Param("end") LocalDateTime end,
	    Pageable pageable);
}

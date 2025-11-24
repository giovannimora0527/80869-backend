package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.IntentoFallidoLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IntentoFallidoLoginRepository extends JpaRepository<IntentoFallidoLogin, Long> {

    @Query("SELECT COUNT(i) FROM IntentoFallidoLogin i WHERE i.usuario.id = :usuarioId AND i.fechaIntento > :fechaLimite")
    Long countRecentAttemptsByUsuarioId(@Param("usuarioId") Long usuarioId, @Param("fechaLimite") LocalDateTime fechaLimite);

    List<IntentoFallidoLogin> findByUsuarioIdAndFechaIntentoAfter(Long usuarioId, LocalDateTime fechaLimite);

    void deleteByUsuarioId(Long usuarioId);

    @Query("SELECT i FROM IntentoFallidoLogin i WHERE i.usuario.username = :username AND i.fechaIntento > :fechaLimite")
    List<IntentoFallidoLogin> findByUsernameAndFechaIntentoAfter(@Param("username") String username, @Param("fechaLimite") LocalDateTime fechaLimite);
}
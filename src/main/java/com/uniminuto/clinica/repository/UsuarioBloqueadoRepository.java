package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.UsuarioBloqueado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioBloqueadoRepository extends JpaRepository<UsuarioBloqueado, Long> {

    Optional<UsuarioBloqueado> findByUsuarioIdAndActivoTrue(Long usuarioId);

    @Query("SELECT ub FROM UsuarioBloqueado ub WHERE ub.usuario.username = :username AND ub.activo = true")
    Optional<UsuarioBloqueado> findByUsernameAndActivoTrue(@Param("username") String username);

    List<UsuarioBloqueado> findByActivoTrueAndFechaDesbloqueoBeforeOrderByFechaDesbloqueoAsc(LocalDateTime fecha);

    void deleteByUsuarioId(Long usuarioId);
}

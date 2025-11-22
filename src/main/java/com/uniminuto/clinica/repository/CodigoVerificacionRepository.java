package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.CodigoVerificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CodigoVerificacionRepository extends JpaRepository<CodigoVerificacion, Long> {
    
    Optional<CodigoVerificacion> findByUsernameAndCodigoAndUsadoFalseAndFechaExpiracionAfter(
        String username, String codigo, LocalDateTime now);
    
    List<CodigoVerificacion> findByUsernameAndUsadoFalse(String username);
    
    void deleteByFechaExpiracionBefore(LocalDateTime fecha);
}

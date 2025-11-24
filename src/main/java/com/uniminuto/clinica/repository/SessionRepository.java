package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Session.
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    void deleteByUserId(Integer userId);
    Optional<Session> findByToken(String token);
}

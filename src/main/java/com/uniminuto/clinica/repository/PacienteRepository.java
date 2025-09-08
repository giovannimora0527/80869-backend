package com.uniminuto.clinica.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import main.java.com.uniminuto.clinica.entity.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByDocumentoIdentidad(String documentoIdentidad);
}

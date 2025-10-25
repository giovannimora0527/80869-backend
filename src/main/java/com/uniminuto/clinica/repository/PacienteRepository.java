package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // Método para buscar un paciente por su número de documento.
    Optional<Paciente> findByNumeroDocumento(String numeroDocumento);

    // Método para obtener todos los pacientes ordenados por fecha de nacimiento de forma descendente.
    List<Paciente> findAllByOrderByFechaNacimientoDesc();
}
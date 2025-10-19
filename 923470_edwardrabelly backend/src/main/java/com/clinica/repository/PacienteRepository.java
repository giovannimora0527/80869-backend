package com.clinica.repository;

import com.clinica.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByNumeroDocumento(String numeroDocumento);

    // Nuevo método para listar ordenados por fecha de nacimiento (descendente)
    List<Paciente> findAllByOrderByFechaNacimientoDesc();
}




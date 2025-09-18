package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.Paciente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Miguel
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>{
   Optional<Paciente> findByNumeroDocumento(String numeroDocumento);
   
   /**
    * Encuentra todos los pacientes ordenados por fecha de nacimiento
    * de manera ascendente (del mayor al menor).
    *
    * @return lista de pacientes ordenada por fecha de nacimiento ascendente
    */
   List<Paciente> findAllByOrderByFechaNacimientoAsc();
}

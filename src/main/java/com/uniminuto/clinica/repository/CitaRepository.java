package com.uniminuto.clinica.repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import com.uniminuto.clinica.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    /**
     * Obtiene todas las citas ordenadas por fecha y hora en orden descendente.
     */
    List<Cita> findAllByOrderByFechaHoraDesc();

    /**
     * Busca citas por ID de médico y fecha/hora específica.
     */
    List<Cita> findByMedicoIdAndFechaHora(Long medicoId, LocalDateTime fechaHora);

    /**
     * Busca citas por ID de médico excluyendo una cita específica (para evitar duplicados en actualizaciones).
     */
    List<Cita> findByMedicoIdAndFechaHoraAndIdNot(Long medicoId, LocalDateTime fechaHora, Long id);
}
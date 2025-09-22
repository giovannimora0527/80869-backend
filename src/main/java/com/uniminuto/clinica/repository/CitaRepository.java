package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.entity.Medico;
import com.uniminuto.clinica.entity.Paciente;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acceso a datos para la entidad {@link Cita}.
 */
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long>, CitaRepositoryCustom {
    
    /**
     * Lista todas las citas ordenadas por fecha y hora de forma descendente.
     * @return listado de citas más recientes primero
     */
    List<Cita> findAllByOrderByFechaHoraDesc();
    
    /**
     * Busca citas que se solapan con el horario propuesto para un médico específico.
     * Considera un rango de tiempo alrededor de la fecha propuesta para evitar conflictos.
     * 
     * @param medico Médico para el cual se buscan conflictos
     * @param fechaInicio Fecha y hora de inicio del rango a verificar
     * @param fechaFin Fecha y hora de fin del rango a verificar
     * @return Lista de citas que generan conflicto
     */
    @Query("SELECT c FROM Cita c WHERE c.medico = :medico AND " +
           "c.fechaHora BETWEEN :fechaInicio AND :fechaFin")
    List<Cita> findConflictingAppointments(@Param("medico") Medico medico, 
                                         @Param("fechaInicio") LocalDateTime fechaInicio, 
                                         @Param("fechaFin") LocalDateTime fechaFin);
    
    /**
     * Busca citas existentes para un paciente en un rango de fechas específico.
     * Utilizado para evitar múltiples citas del mismo paciente en el mismo día.
     * 
     * @param paciente Paciente para el cual se buscan citas existentes
     * @param fechaInicio Fecha y hora de inicio del rango
     * @param fechaFin Fecha y hora de fin del rango
     * @return Lista de citas del paciente en el rango especificado
     */
    @Query("SELECT c FROM Cita c WHERE c.paciente = :paciente AND " +
           "c.fechaHora BETWEEN :fechaInicio AND :fechaFin")
    List<Cita> findPatientAppointmentsInRange(@Param("paciente") Paciente paciente, 
                                             @Param("fechaInicio") LocalDateTime fechaInicio, 
                                             @Param("fechaFin") LocalDateTime fechaFin);
    
    /**
     * Cuenta el número de citas que tiene un médico en una fecha específica.
     * 
     * @param medico Médico para el cual se cuentan las citas
     * @param fechaInicio Inicio del día a consultar
     * @param fechaFin Fin del día a consultar
     * @return Número de citas del médico en la fecha
     */
    @Query("SELECT COUNT(c) FROM Cita c WHERE c.medico = :medico AND " +
           "c.fechaHora BETWEEN :fechaInicio AND :fechaFin")
    Long countDoctorAppointmentsInDay(@Param("medico") Medico medico, 
                                     @Param("fechaInicio") LocalDateTime fechaInicio, 
                                     @Param("fechaFin") LocalDateTime fechaFin);
}



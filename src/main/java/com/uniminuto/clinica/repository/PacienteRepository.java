/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.Paciente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dbaez
 */

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
    List<Paciente> findAllByOrderByFechanacimiento();
    
    List<Paciente> findByNumerodocumento(String numerodocumento);
    
    List<Paciente> findByGenero(String genero);
    
    List<Paciente> findByTipodocumento(String tipodocumento);
    
    List<Paciente> findByNombre(String nombre);
}

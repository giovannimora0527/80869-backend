/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.uniminuto.clinica.service;
import com.uniminuto.clinica.entity.Paciente;
import java.util.List;
import org.apache.coyote.BadRequestException;

/**
 *
 * @author dbaez
 */
public interface PacienteService {
    
List<Paciente> encontrarPacienteFecha();
    
List<Paciente> encontrarTodosLosPacientes();
    
List<Paciente> buscarPorGenero(String genero);

List<Paciente> buscarPorNombre(String nombre) throws BadRequestException;
    
}

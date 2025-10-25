package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.model.PacienteRq;
import com.uniminuto.clinica.model.PacienteRs; 
import java.util.List;
import java.util.Optional;
import org.apache.coyote.BadRequestException;

public interface PacienteService {
    
   
    List<PacienteRs> obtenerTodos();
    
   
    Optional<PacienteRs> buscarPorDocumento(String documento);
    
  
    PacienteRs guardar(PacienteRq paciente) throws BadRequestException;
    
    void eliminar(Long id);

    PacienteRs actualizar(Long id, PacienteRq paciente) throws BadRequestException;
    List<PacienteRs> obtenerPacientesOrdenadosPorFechaNacimiento();


}
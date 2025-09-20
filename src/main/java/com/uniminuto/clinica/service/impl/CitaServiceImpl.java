package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.entity.Medico;
import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.model.CitaRq;
import com.uniminuto.clinica.repository.CitaRepository;
import com.uniminuto.clinica.repository.MedicoRepository;
import com.uniminuto.clinica.repository.PacienteRepository;
import com.uniminuto.clinica.service.CitaService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz {@link CitaService}.
 * Contiene la lógica de negocio para el manejo de citas médicas,
 * incluyendo el almacenamiento y la consulta de citas recientes.
 */
@Service
public class CitaServiceImpl implements CitaService {
    @Autowired
    CitaRepository citaRepository;
    @Autowired
    MedicoRepository medicoRepository;
    @Autowired
    PacienteRepository pacienteRepository;

    /**
     * Guarda una nueva cita médica en la base de datos.
     *
     * @param citaRq Objeto {@link CitaRq} que contiene los datos de la cita a guardar.
     * @return Mensaje indicando si la operación fue exitosa.
     * @throws BadRequestException si ocurre un error al guardar la cita.
     */
    @Override
    public String guardarCita(CitaRq citaRq) throws BadRequestException {
        Cita cita = new Cita();
        if(citaRq.getMedicoId() == null){
            throw new BadRequestException("Error al guardar Cita: debe relacionar el Id del médico.");
        }
        if(citaRq.getPacienteId() == null){
            throw new BadRequestException("Error al guardar Cita: debe relacionar el Id del paciente.");
        }
        Optional<Medico> medicoOpt = this.medicoRepository.findById(citaRq.getMedicoId());
        if(!medicoOpt.isPresent()){
            throw new BadRequestException("Error al guardar Cita: No existe el médico relacionado.");
        }else{
            cita.setMedico(medicoOpt.get());
        }

        Optional<Paciente> pacienteOpt = this.pacienteRepository.findById(citaRq.getPacienteId());
        if(!pacienteOpt.isPresent()){
            throw new BadRequestException("Error al guardar Cita: No existe el paciente relacionado.");
        }else{
            cita.setPaciente(pacienteOpt.get());
        }
        cita.setEstado(citaRq.getEstado());
        cita.setMotivo(citaRq.getMotivo());
        cita.setFechaHora(citaRq.getFechaHora());
        this.citaRepository.save(cita);
        return "Se guardó la cita correctamente.";
    }

    /**
     * Retorna una lista de citas ordenadas por fecha y hora en orden ascendente.
     * Este método puede usarse para mostrar las citas más recientes primero.
     *
     * @return Lista de objetos {@link Cita} ordenados por fecha y hora.
     */
    @Override
    public List<Cita> listarReciente() {
        return this.citaRepository.findAllByOrderByFechaHoraAsc();
    }
}

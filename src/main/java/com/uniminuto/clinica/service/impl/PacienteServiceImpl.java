package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.repository.PacienteRepository;
import com.uniminuto.clinica.service.PacienteService;
import java.util.List;
import java.util.Optional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *Implementación del servicio {@link PacienteService} para el manejo
 * de la lógica de negocio relacionada con los pacientes.
 *
 * Proporciona operaciones como listar pacientes, buscar por documento
 * y listar por edad (ascendente).
 * @author lmora
 */
@Service
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     * Retorna una lista de todos los pacientes registrados en la base de datos.
     *
     * @return Lista de objetos {@link Paciente}.
     */
    @Override
    public List<Paciente> listarPacientes() {
        return this.pacienteRepository.findAll();
    }

    /**
     * Busca un paciente a partir de su número de documento.
     *
     * @param documento Número de documento del paciente.
     * @return Objeto {@link Paciente} correspondiente al documento.
     * @throws BadRequestException si no se encuentra un paciente con ese documento.
     */
    @Override
    public Paciente buscarPorDocumento(String documento) throws BadRequestException {
        Optional<Paciente> optPaciente = this.pacienteRepository
                .findByNumeroDocumento(documento);
        if (!optPaciente.isPresent()) {
            throw new BadRequestException("No se encontro el documento del paciente");
        }
        return optPaciente.get();
    }

    @Override
    public List<Paciente> listarPacientesOrdenadoPorFechaNacimiento() {
        return this.pacienteRepository.findAllByOrderByFechaNacimientoAsc();
    }

}

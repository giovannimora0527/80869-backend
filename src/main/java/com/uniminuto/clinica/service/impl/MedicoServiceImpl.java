package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Especializacion;
import com.uniminuto.clinica.entity.Medico;
import com.uniminuto.clinica.model.MedicoRq;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.repository.EspecialidadRepository;
import com.uniminuto.clinica.repository.MedicoRepository;
import com.uniminuto.clinica.service.MedicoService;
import java.util.List;
import java.util.Optional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author lmora
 */
@Service
public class MedicoServiceImpl implements MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Override
    public List<Medico> listarMedicos() {
        return this.medicoRepository.findAll();
    }

    @Override
    public List<Medico> listarMedicosPorEspecialidad(String codEspecialidad)
            throws BadRequestException {
        Optional<Especializacion> optEspecializacion = this.especialidadRepository
                .findByCodigoEspecializacion(codEspecialidad);
        if (!optEspecializacion.isPresent()) {
            throw new BadRequestException("No existe el codigo de la especializacion");
        }

        Especializacion esp = optEspecializacion.get();
        return this.medicoRepository.findByEspecializacion(esp);
    }

    @Override
    public Medico buscarPorCC(String documento) throws BadRequestException {
        Optional<Medico> optMedico = this.medicoRepository
                .findByNumeroDocumento(documento);
        if (!optMedico.isPresent()) {
            throw new BadRequestException("El medico no se encuentra.");
        }
        return optMedico.get();
    }

    @Override
    public RespuestaRs guardarMedico(MedicoRq medicoRq) throws BadRequestException {
        Optional<Medico> optMedico = this.medicoRepository
                .findByNumeroDocumento(medicoRq.getNumeroDocumento());
        if (optMedico.isPresent()) {
            throw new BadRequestException("Ya existe un medico con el mismo numero de documento");
        }
        optMedico = this.medicoRepository.findByRegistroProfesional(medicoRq.getRegistroProfesional());
        if (optMedico.isPresent()) {
            throw new BadRequestException("Ya existe un medico con el mismo numero de registro profesional");
        }

        Optional<Especializacion> optEspecializacion = this.especialidadRepository
                .findById(medicoRq.getEspecializacion());
        if (optEspecializacion.isEmpty()) {
            throw new BadRequestException("No existe la especializacion indicada");
        }

        Medico medicoGuardar = this.convertir(medicoRq, optEspecializacion.get());
        this.medicoRepository.save(medicoGuardar);
        RespuestaRs rta = new RespuestaRs();
        rta.setMensaje("Se ha guardado el medico correctamente");
        return rta;
    }

    @Override
    public RespuestaRs actualizarMedico(Long id, MedicoRq medicoRq) throws BadRequestException {
        Optional<Medico> optMedico = this.medicoRepository.findById(id);
        if (optMedico.isEmpty()) {
            throw new BadRequestException("No existe un médico con el ID indicado");
        }

        Medico medicoExistente = optMedico.get();

        if (!medicoExistente.getNumeroDocumento().equals(medicoRq.getNumeroDocumento())) {
            Optional<Medico> dupDoc = this.medicoRepository.findByNumeroDocumento(medicoRq.getNumeroDocumento());
            if (dupDoc.isPresent()) {
                throw new BadRequestException("Ya existe un médico con el mismo número de documento");
            }
        }

        if (!medicoExistente.getRegistroProfesional().equals(medicoRq.getRegistroProfesional())) {
            Optional<Medico> dupReg = this.medicoRepository.findByRegistroProfesional(medicoRq.getRegistroProfesional());
            if (dupReg.isPresent()) {
                throw new BadRequestException("Ya existe un médico con el mismo número de registro profesional");
            }
        }

        Optional<Especializacion> optEspecializacion = this.especialidadRepository
                .findById(medicoRq.getEspecializacion());
        if (optEspecializacion.isEmpty()) {
            throw new BadRequestException("No existe la especialización indicada");
        }

        medicoExistente.setTipoDocumento(medicoRq.getTipoDocumento());
        medicoExistente.setNumeroDocumento(medicoRq.getNumeroDocumento());
        medicoExistente.setNombres(medicoRq.getNombres());
        medicoExistente.setApellidos(medicoRq.getApellidos());
        medicoExistente.setTelefono(medicoRq.getTelefono());
        medicoExistente.setRegistroProfesional(medicoRq.getRegistroProfesional());
        medicoExistente.setEspecializacion(optEspecializacion.get());

        this.medicoRepository.save(medicoExistente);

        RespuestaRs rta = new RespuestaRs();
        rta.setMensaje("Médico actualizado correctamente");
        return rta;
    }

    private Medico convertir(MedicoRq medicoRq, Especializacion especializacion) {
        Medico medico = new Medico();
        medico.setNombres(medicoRq.getNombres());
        medico.setApellidos(medicoRq.getApellidos());
        medico.setTipoDocumento(medicoRq.getTipoDocumento());
        medico.setNumeroDocumento(medicoRq.getNumeroDocumento());
        medico.setRegistroProfesional(medicoRq.getRegistroProfesional());
        medico.setEspecializacion(especializacion);
        medico.setTelefono(medicoRq.getTelefono());
        return medico;
    }

}
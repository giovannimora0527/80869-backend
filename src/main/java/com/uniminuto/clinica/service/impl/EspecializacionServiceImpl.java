package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Especializacion;
import com.uniminuto.clinica.model.EspecializacionRq;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.repository.EspecialidadRepository;
import com.uniminuto.clinica.service.EspecializacionService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspecializacionServiceImpl implements EspecializacionService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Override
    public List<Especializacion> listar() {
        return this.especialidadRepository.findAll();
    }

    @Override
    public RespuestaRs guardarEspecializacion(EspecializacionRq especializacionRq) throws BadRequestException {
        Optional<Especializacion> existingEspecializacion = especialidadRepository
                .findByCodigoEspecializacion(especializacionRq.getCodigoEspecializacion());
        if (existingEspecializacion.isPresent()) {
          throw new BadRequestException("Ya existe una especialización con el código proporcionado.");
        }
        existingEspecializacion = especialidadRepository
                .findByNombre(especializacionRq.getNombre());
        if (existingEspecializacion.isPresent()) {
          throw new BadRequestException("Ya existe una especialización con el nombre proporcionado.");
        }

        Especializacion nueva = this.convertToEntity(especializacionRq);
        this.especialidadRepository.save(nueva);
        RespuestaRs rta = new RespuestaRs();
        rta.setMensaje("Especialización guardada exitosamente.");
        rta.setStatus(200);
        return rta;
    }

    private Especializacion convertToEntity(EspecializacionRq especializacionRq) {
        Especializacion especializacion = new Especializacion();
        especializacion.setCodigoEspecializacion(especializacionRq.getCodigoEspecializacion());
        especializacion.setNombre(especializacionRq.getNombre());
        especializacion.setDescripcion(especializacionRq.getDescripcion());
        return especializacion;
    }

    @Override
    public RespuestaRs actualizarEspecializacion(EspecializacionRq especializacionRq) throws BadRequestException {
        Optional<Especializacion> optEspecial = this.especialidadRepository
                .findById(especializacionRq.getId());
        if (optEspecial.isEmpty()) {
           throw new BadRequestException("No se encontró la especialización con el ID proporcionado.");
        }

        Especializacion especializacionActual = optEspecial.get();
        if (!especializacionActual.getCodigoEspecializacion().equalsIgnoreCase(especializacionRq.getCodigoEspecializacion())) {
            Optional<Especializacion> existingEspecializacion = especialidadRepository
                    .findByCodigoEspecializacion(especializacionRq.getCodigoEspecializacion());
            if (existingEspecializacion.isPresent()) {
                throw new BadRequestException("Ya existe una especialización con el código proporcionado.");
            }
            especializacionActual.setCodigoEspecializacion(especializacionRq.getCodigoEspecializacion());
        }

        if (!especializacionActual.getNombre().equalsIgnoreCase(especializacionRq.getNombre())) {
            Optional<Especializacion> existingEspecializacion = especialidadRepository
                    .findByNombre(especializacionRq.getNombre());
            if (existingEspecializacion.isPresent()) {
                throw new BadRequestException("Ya existe una especialización con el nombre proporcionado.");
            }
            especializacionActual.setNombre(especializacionRq.getNombre());
        }
        especializacionActual.setDescripcion(especializacionRq.getDescripcion());
        this.especialidadRepository.save(especializacionActual);
        RespuestaRs rta = new RespuestaRs();
        rta.setMensaje("Especialización actualizada exitosamente.");
        rta.setStatus(200);
        return rta;
    }
}

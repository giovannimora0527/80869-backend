package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Especializacion;
import com.uniminuto.clinica.model.EspecializacionRq;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.repository.EspecialidadRepository;
import com.uniminuto.clinica.service.EspecializacionService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class EspecializacionServiceImpl implements EspecializacionService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Override
    public List<Especializacion> listarEspecializaciones() {
        return this.especialidadRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Especializacion::getNombre))
                .toList();
    }

    @Override
    public RespuestaRs guardarEspecializacion(EspecializacionRq especializacionRq) throws BadRequestException {
        this.validarFormulario(especializacionRq);

        // Validar nombre único
        Optional<Especializacion> opt = this.especialidadRepository.findByNombre(especializacionRq.getNombre());
        if (opt.isPresent()) {
            throw new BadRequestException("Ya existe una especialización con ese nombre. Intente de nuevo.");
        }

        // Validar código único si viene
        if (especializacionRq.getCodigoEspecializacion() != null) {
            Optional<Especializacion> opt2 = this.especialidadRepository
                    .findByCodigoEspecializacion(especializacionRq.getCodigoEspecializacion());
            if (opt2.isPresent()) {
                throw new BadRequestException("El código de especialización ya existe. Intente de nuevo.");
            }
        }

        Especializacion nuevo = convertirAEspecializacion(especializacionRq);
        this.especialidadRepository.save(nuevo);

        RespuestaRs rta = new RespuestaRs();
        rta.setStatus(200);
        rta.setMensaje("Especialización creada exitosamente");
        return rta;
    }

    @Override
    public RespuestaRs actualizarEspecializacion(EspecializacionRq especializacionRq) throws BadRequestException {
        if (especializacionRq.getId() == null) {
            throw new BadRequestException("El id de la especialización es obligatorio");
        }

        Optional<Especializacion> opt = this.especialidadRepository.findById(especializacionRq.getId());
        if (!opt.isPresent()) {
            throw new BadRequestException("No se puede actualizar la especialización porque no existe.");
        }

        Especializacion actual = opt.get();

        if (especializacionRq.getNombre() != null &&
                !actual.getNombre().equalsIgnoreCase(especializacionRq.getNombre())) {
            Optional<Especializacion> optNombre = this.especialidadRepository.findByNombre(especializacionRq.getNombre());
            if (optNombre.isPresent()) {
                throw new BadRequestException("Ya existe una especialización con ese nombre. Intente de nuevo.");
            }
        }

        if (especializacionRq.getCodigoEspecializacion() != null &&
                (actual.getCodigoEspecializacion() == null ||
                        !actual.getCodigoEspecializacion().equals(especializacionRq.getCodigoEspecializacion()))) {
            Optional<Especializacion> optCodigo = this.especialidadRepository
                    .findByCodigoEspecializacion(especializacionRq.getCodigoEspecializacion());
            if (optCodigo.isPresent()) {
                throw new BadRequestException("El código de especialización ya existe. Intente de nuevo.");
            }
        }

        actual.setNombre(especializacionRq.getNombre() == null ? actual.getNombre() : especializacionRq.getNombre());
        actual.setDescripcion(especializacionRq.getDescripcion() == null ? actual.getDescripcion() : especializacionRq.getDescripcion());
        actual.setCodigoEspecializacion(especializacionRq.getCodigoEspecializacion() == null ? actual.getCodigoEspecializacion() : especializacionRq.getCodigoEspecializacion());

        this.especialidadRepository.save(actual);

        RespuestaRs rta = new RespuestaRs();
        rta.setMensaje("Se ha actualizado el registro satisfactoriamente");
        rta.setStatus(200);
        return rta;
    }

    // 🔹 Métodos privados auxiliares

    private Especializacion convertirAEspecializacion(EspecializacionRq rq) {
        Especializacion e = new Especializacion();
        e.setNombre(rq.getNombre());
        e.setDescripcion(rq.getDescripcion());
        e.setCodigoEspecializacion(rq.getCodigoEspecializacion());
        return e;
    }

    private void validarFormulario(EspecializacionRq rq) throws BadRequestException {
        if (rq.getNombre() == null || rq.getNombre().isEmpty()) {
            throw new BadRequestException("El nombre es obligatorio");
        }
    }
}
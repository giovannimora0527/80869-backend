package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Especializacion;
import com.uniminuto.clinica.repository.EspecializacionRepository;
import com.uniminuto.clinica.service.EspecializacionService;
import java.util.List;
import java.util.Optional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EspecializacionServiceImpl implements EspecializacionService {

    @Autowired
    private EspecializacionRepository repo;

    @Override
    public List<Especializacion> listarTodo() {
        return this.repo.findAll();
    }

    @Override
    public Especializacion buscarEspecializacionPorCod(String codigo) throws BadRequestException {
        Optional<Especializacion> optEspc = this.repo.findByCodigoEspecializacion(codigo);
        if (!optEspc.isPresent()) {
            throw new BadRequestException("No se encuentra la especialización");
        }
        return optEspc.get();
    }

    @Override
    public Especializacion guardarEspecializacion(Especializacion especializacion) throws BadRequestException {
        // Validar que no exista el código
        if (especializacion.getId() == null &&
                this.repo.existsByCodigoEspecializacion(especializacion.getCodigoEspecializacion())) {
            throw new BadRequestException("Ya existe una especialización con ese código");
        }

        // Validar que no exista el nombre
        if (especializacion.getId() == null &&
                this.repo.existsByNombreIgnoreCase(especializacion.getNombre())) {
            throw new BadRequestException("Ya existe una especialización con ese nombre");
        }

        return this.repo.save(especializacion);
    }

    @Override
    public Especializacion actualizarEspecializacion(Long id, Especializacion especializacion) throws BadRequestException {
        Optional<Especializacion> optExistente = this.repo.findById(id);
        if (!optExistente.isPresent()) {
            throw new BadRequestException("No se encuentra la especialización a actualizar");
        }

        Especializacion existente = optExistente.get();
        existente.setNombre(especializacion.getNombre());
        existente.setDescripcion(especializacion.getDescripcion());
        existente.setCodigoEspecializacion(especializacion.getCodigoEspecializacion());

        return this.repo.save(existente);
    }
}
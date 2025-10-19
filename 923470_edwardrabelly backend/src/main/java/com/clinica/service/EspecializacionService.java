package com.clinica.service;

import com.clinica.entity.Especializacion;
import com.clinica.repository.EspecializacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspecializacionService {

    @Autowired
    private EspecializacionRepository especializacionRepository;

    public List<Especializacion> listar() {
        return especializacionRepository.findAll();
    }

    public Optional<Especializacion> obtenerPorId(Long id) {
        return especializacionRepository.findById(id);
    }

    public Especializacion guardar(Especializacion especializacion) {
        return especializacionRepository.save(especializacion);
    }

    public Especializacion actualizar(Long id, Especializacion datos) {
        return especializacionRepository.findById(id).map(e -> {
            e.setNombre(datos.getNombre());
            e.setDescripcion(datos.getDescripcion());
            e.setCodigoEspecializacion(datos.getCodigoEspecializacion());
            return especializacionRepository.save(e);
        }).orElseThrow(() -> new RuntimeException("Especialización no encontrada"));
    }

    public void eliminar(Long id) {
        especializacionRepository.deleteById(id);
    }
}

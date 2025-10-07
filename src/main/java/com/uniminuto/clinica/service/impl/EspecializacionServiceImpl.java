package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Especializacion;
import com.uniminuto.clinica.repository.EspecialidadRepository;
import com.uniminuto.clinica.service.EspecializacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecializacionServiceImpl implements EspecializacionService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Override
    public List<Especializacion> listar() {
        return this.especialidadRepository.findAll();
    }
}

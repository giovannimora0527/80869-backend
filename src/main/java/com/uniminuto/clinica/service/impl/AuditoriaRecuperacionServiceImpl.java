package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.AuditoriaRecuperacion;
import com.uniminuto.clinica.repository.AuditoriaRecuperacionRepository;
import com.uniminuto.clinica.service.AuditoriaRecuperacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaRecuperacionServiceImpl implements AuditoriaRecuperacionService {
    @Autowired
    private AuditoriaRecuperacionRepository auditoriaRepo;

    @Override
    public void registrarIntento(String username, String descripcion) {
        AuditoriaRecuperacion auditoria = new AuditoriaRecuperacion();
        auditoria.setUsername(username);
        auditoria.setDescripcion(descripcion);
        auditoriaRepo.save(auditoria);
    }
}

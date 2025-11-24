package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.AuditoriaLogin;
import com.uniminuto.clinica.repository.AuditoriaLoginRepository;
import com.uniminuto.clinica.service.AuditoriaLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditoriaLoginServiceImpl implements AuditoriaLoginService {

    @Autowired
    private AuditoriaLoginRepository auditoriaLoginRepository;

    @Override
    public Page<AuditoriaLogin> buscarLogs(
            String username,
            LocalDateTime fechaDesde,
            LocalDateTime fechaHasta,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fecha").descending());
        return auditoriaLoginRepository.buscarConFiltros(username, fechaDesde, fechaHasta, pageable);
    }
}


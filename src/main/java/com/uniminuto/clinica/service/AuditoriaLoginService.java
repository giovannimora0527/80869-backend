package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.AuditoriaLogin;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface AuditoriaLoginService {

    Page<AuditoriaLogin> buscarLogs(
            String username,
            LocalDateTime fechaDesde,
            LocalDateTime fechaHasta,
            int page,
            int size
    );
}


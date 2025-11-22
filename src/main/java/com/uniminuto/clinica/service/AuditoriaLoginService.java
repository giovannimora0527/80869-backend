package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.AuditoriaLogin;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditoriaLoginService {
    void registrarIntento(AuditoriaLogin auditoriaLogin);
    Page<AuditoriaLogin> buscar(String usuario, Boolean exito, LocalDateTime start, LocalDateTime end, Pageable pageable);
}

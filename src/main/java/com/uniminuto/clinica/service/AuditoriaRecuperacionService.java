package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.AuditoriaRecuperacion;

public interface AuditoriaRecuperacionService {
    void registrarIntento(String username, String descripcion);
}

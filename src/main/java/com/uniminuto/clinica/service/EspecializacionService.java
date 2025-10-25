package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Especializacion;
import java.util.List;
import org.apache.coyote.BadRequestException;

public interface EspecializacionService {

    List<Especializacion> listarTodo();
    Especializacion buscarEspecializacionPorCod(String codigo) throws BadRequestException;
    Especializacion guardarEspecializacion(Especializacion especializacion) throws BadRequestException;
    Especializacion actualizarEspecializacion(Long id, Especializacion especializacion) throws BadRequestException;
}
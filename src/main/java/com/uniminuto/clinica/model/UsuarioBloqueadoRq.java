package com.uniminuto.clinica.model;

import lombok.Data;

@Data
public class UsuarioBloqueadoRq {
    private Long usuarioId;
    private String motivo;
    private int minutosBloqueo;
    private int intentosFallidos;
}

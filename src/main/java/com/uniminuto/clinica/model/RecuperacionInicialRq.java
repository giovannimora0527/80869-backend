package com.uniminuto.clinica.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RecuperacionInicialRq {
    /** email de usuario */
    @NotBlank(message = "El email es obligatorio.")
    private String email;

}

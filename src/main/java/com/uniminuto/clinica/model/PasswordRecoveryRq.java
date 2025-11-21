package com.uniminuto.clinica.model;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * Request para recuperación de contraseña.
 */
@Data
public class PasswordRecoveryRq {
    
    @NotBlank(message = "El nombre de usuario es requerido")
    private String username;
}

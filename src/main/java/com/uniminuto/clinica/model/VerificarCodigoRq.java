package com.uniminuto.clinica.model;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class VerificarCodigoRq implements Serializable {
    @NotBlank(message = "El username es obligatorio.")
    private String username;
    
    @NotBlank(message = "El código es obligatorio.")
    private String codigo;
    
    @NotBlank(message = "La nueva contraseña es obligatoria.")
    private String nuevaPassword;
}

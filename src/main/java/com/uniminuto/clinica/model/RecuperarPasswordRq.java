package com.uniminuto.clinica.model;

import lombok.Data;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;

@Data
public class RecuperarPasswordRq implements Serializable {
    @NotBlank(message = "El username es obligatorio.")
    private String username;
}

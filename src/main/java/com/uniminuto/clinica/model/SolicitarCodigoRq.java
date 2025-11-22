package com.uniminuto.clinica.model;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class SolicitarCodigoRq implements Serializable {
    @NotBlank(message = "El username es obligatorio.")
    private String username;
}

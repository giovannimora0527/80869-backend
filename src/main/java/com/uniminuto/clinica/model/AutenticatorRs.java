package com.uniminuto.clinica.model;

import lombok.Data;

@Data
public class AutenticatorRs {
    private String token;
    private String username;
    private String email;
    private String rol;
}

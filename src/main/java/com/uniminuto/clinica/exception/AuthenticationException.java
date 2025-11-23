package com.uniminuto.clinica.exception;

import lombok.Getter;
import java.util.Map;

/**
 * Excepción personalizada para errores de autenticación.
 * Permite enviar datos estructurados al frontend.
 */
@Getter
public class AuthenticationException extends RuntimeException {
    private final Map<String, Object> errorData;

    public AuthenticationException(String message, Map<String, Object> errorData) {
        super(message);
        this.errorData = errorData;
    }
}

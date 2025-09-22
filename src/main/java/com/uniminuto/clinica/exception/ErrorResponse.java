package com.uniminuto.clinica.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Clase para estructurar las respuestas de error de la API.
 * Proporciona información detallada sobre errores ocurridos durante el procesamiento de solicitudes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    /**
     * Timestamp cuando ocurrió el error
     */
    private LocalDateTime timestamp;
    
    /**
     * Código de estado HTTP
     */
    private int status;
    
    /**
     * Tipo o categoría del error
     */
    private String error;
    
    /**
     * Mensaje descriptivo del error
     */
    private String message;
    
    /**
     * Ruta donde ocurrió el error
     */
    private String path;
}
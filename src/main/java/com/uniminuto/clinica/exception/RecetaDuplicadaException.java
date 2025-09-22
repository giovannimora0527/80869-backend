package com.uniminuto.clinica.exception;

/**
 * Excepción lanzada cuando se intenta crear una receta duplicada.
 */
public class RecetaDuplicadaException extends RuntimeException {
    
    public RecetaDuplicadaException(String mensaje) {
        super(mensaje);
    }
    
    public RecetaDuplicadaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    public RecetaDuplicadaException(Long citaId) {
        super("Ya existe una receta activa para la cita con ID: " + citaId);
    }
}
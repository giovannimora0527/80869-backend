package com.uniminuto.clinica.exception;

/**
 * Excepción lanzada cuando se intenta acceder a una cita que no existe.
 */
public class CitaNoEncontradaException extends RuntimeException {
    
    public CitaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
    
    public CitaNoEncontradaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    public CitaNoEncontradaException(Long citaId) {
        super("No se encontró la cita con ID: " + citaId);
    }
}
package com.uniminuto.clinica.exception;

/**
 * Excepción lanzada cuando un médico no está autorizado para realizar una operación.
 */
public class MedicoNoAutorizadoException extends RuntimeException {
    
    public MedicoNoAutorizadoException(String mensaje) {
        super(mensaje);
    }
    
    public MedicoNoAutorizadoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    public MedicoNoAutorizadoException(Long medicoId, Long citaId) {
        super("El médico con ID " + medicoId + " no está autorizado para crear receta en la cita " + citaId);
    }
}
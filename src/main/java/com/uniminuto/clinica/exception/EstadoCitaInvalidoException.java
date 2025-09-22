package com.uniminuto.clinica.exception;

/**
 * Excepción lanzada cuando el estado de una cita no es válido para la operación solicitada.
 */
public class EstadoCitaInvalidoException extends RuntimeException {
    
    public EstadoCitaInvalidoException(String mensaje) {
        super(mensaje);
    }
    
    public EstadoCitaInvalidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    public EstadoCitaInvalidoException(Long citaId, String estadoActual) {
        super("La cita con ID " + citaId + " está en estado '" + estadoActual + 
              "' y no es válida para crear receta. Estados válidos: FINALIZADA, EN_CURSO");
    }
}
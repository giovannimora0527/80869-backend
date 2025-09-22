package com.uniminuto.clinica.exception;

/**
 * Excepción lanzada cuando se intenta programar una cita fuera del horario de atención.
 *
 * @author AI
 */
public class HorarioNoValidoException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado.
     *
     * @param mensaje Mensaje descriptivo del error
     */
    public HorarioNoValidoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param mensaje Mensaje descriptivo del error
     * @param causa   Causa raíz de la excepción
     */
    public HorarioNoValidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
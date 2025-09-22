package com.uniminuto.clinica.exception;

/**
 * Excepción lanzada cuando hay un conflicto de horarios en la programación de citas.
 *
 * @author AI
 */
public class ConflictoHorarioException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado.
     *
     * @param mensaje Mensaje descriptivo del error
     */
    public ConflictoHorarioException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param mensaje Mensaje descriptivo del error
     * @param causa   Causa raíz de la excepción
     */
    public ConflictoHorarioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
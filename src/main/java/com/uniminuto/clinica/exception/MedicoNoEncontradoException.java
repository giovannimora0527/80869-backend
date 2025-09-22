package com.uniminuto.clinica.exception;

/**
 * Excepción lanzada cuando no se encuentra un médico en el sistema.
 *
 * @author AI
 */
public class MedicoNoEncontradoException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado.
     *
     * @param mensaje Mensaje descriptivo del error
     */
    public MedicoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param mensaje Mensaje descriptivo del error
     * @param causa   Causa raíz de la excepción
     */
    public MedicoNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
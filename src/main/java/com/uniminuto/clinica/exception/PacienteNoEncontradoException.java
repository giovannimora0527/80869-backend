package com.uniminuto.clinica.exception;

/**
 * Excepción lanzada cuando no se encuentra un paciente en el sistema.
 *
 * @author AI
 */
public class PacienteNoEncontradoException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado.
     *
     * @param mensaje Mensaje descriptivo del error
     */
    public PacienteNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param mensaje Mensaje descriptivo del error
     * @param causa   Causa raíz de la excepción
     */
    public PacienteNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
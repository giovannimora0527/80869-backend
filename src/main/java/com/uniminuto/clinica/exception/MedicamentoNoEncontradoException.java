package com.uniminuto.clinica.exception;

/**
 * Excepción lanzada cuando se intenta acceder a un medicamento que no existe.
 */
public class MedicamentoNoEncontradoException extends RuntimeException {
    
    public MedicamentoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
    
    public MedicamentoNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    public static MedicamentoNoEncontradoException porNombre(String nombreMedicamento) {
        return new MedicamentoNoEncontradoException("No se encontró el medicamento: " + nombreMedicamento);
    }
}
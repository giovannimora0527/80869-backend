package com.uniminuto.clinica.service;

import com.uniminuto.clinica.model.RespuestaRs;
import org.apache.coyote.BadRequestException;
import javax.mail.MessagingException;

public interface CodigoVerificacionService {
    RespuestaRs solicitarCodigo(String username) throws BadRequestException, MessagingException;
    RespuestaRs verificarCodigo(String username, String codigo, String nuevaPassword) throws BadRequestException;
    void limpiarCodigosExpirados();
}

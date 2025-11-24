package com.uniminuto.clinica.service;

import com.uniminuto.clinica.model.*;
import org.apache.coyote.BadRequestException;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

public interface AutenticarService {

    AutenticatorRs autenticar(AuthenticatorRq request) throws BadRequestException;

    RespuestaRs iniciarRecuperacion(RecuperacionInicialRq request)
            throws BadRequestException, MessagingException;

    RespuestaRs restablecerPassword(RecuperacionRq request)
            throws BadRequestException;
}

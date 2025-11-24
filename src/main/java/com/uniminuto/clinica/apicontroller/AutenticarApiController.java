package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.AutenticarApi;
import com.uniminuto.clinica.model.*;
import com.uniminuto.clinica.service.AutenticarService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
public class AutenticarApiController implements AutenticarApi {

    @Autowired
    private AutenticarService autenticarService;


    @Override
    public ResponseEntity<AutenticatorRs> autenticar(AuthenticatorRq request) throws BadRequestException {
        return ResponseEntity.ok(this.autenticarService.autenticar(request));
    }

    @Override
    public ResponseEntity<RespuestaRs> solicitarRecuperacionPwd(RecuperacionInicialRq request) throws BadRequestException, MessagingException {
        return ResponseEntity.ok(this.autenticarService.iniciarRecuperacion(request));
    }

    @Override
    public ResponseEntity<RespuestaRs> restablecerPwd(RecuperacionRq request) throws BadRequestException {
        return ResponseEntity.ok(this.autenticarService.restablecerPassword(request));
    }
}


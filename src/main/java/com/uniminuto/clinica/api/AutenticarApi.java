package com.uniminuto.clinica.api;

import com.uniminuto.clinica.model.*;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RequestMapping("/auth")
public interface AutenticarApi {
    @PostMapping("/login")
    ResponseEntity<AutenticatorRs> autenticar(@Valid @RequestBody AuthenticatorRq request) throws BadRequestException;

    @PostMapping("/pwdRequest")
    ResponseEntity<RespuestaRs> solicitarRecuperacionPwd(@RequestBody RecuperacionInicialRq request) throws BadRequestException, MessagingException;

    @PostMapping("/pwdRestore")
    ResponseEntity<RespuestaRs> restablecerPwd(@RequestBody RecuperacionRq request) throws BadRequestException;
}



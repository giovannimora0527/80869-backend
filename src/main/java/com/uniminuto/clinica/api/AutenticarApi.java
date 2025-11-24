package com.uniminuto.clinica.api;

import com.uniminuto.clinica.model.AutenticatorRs;
import com.uniminuto.clinica.model.AuthenticatorRq;
import com.uniminuto.clinica.model.RecuperarContrasenaRq;
import com.uniminuto.clinica.model.RespuestaRs;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/auth")
public interface AutenticarApi {

    @PostMapping("/login")
    ResponseEntity<AutenticatorRs> autenticar(@Valid @RequestBody AuthenticatorRq request)
            throws BadRequestException;

    @PostMapping("/recuperar-contrasena")
    ResponseEntity<RespuestaRs> recuperarContrasena(
            @Valid @RequestBody RecuperarContrasenaRq request) throws BadRequestException;
}

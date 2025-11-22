package com.uniminuto.clinica.api;

import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.model.SolicitarCodigoRq;
import com.uniminuto.clinica.model.VerificarCodigoRq;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.validation.Valid;

@RequestMapping("/auth")
public interface RecuperarPasswordApi {

    @PostMapping("/solicitar-codigo")
    ResponseEntity<RespuestaRs> solicitarCodigo(@Valid @RequestBody SolicitarCodigoRq request) throws BadRequestException;

    @PostMapping("/verificar-codigo")
    ResponseEntity<RespuestaRs> verificarCodigo(@Valid @RequestBody VerificarCodigoRq request) throws BadRequestException;
}

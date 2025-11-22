package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.RecuperarPasswordApi;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.model.SolicitarCodigoRq;
import com.uniminuto.clinica.model.VerificarCodigoRq;
import com.uniminuto.clinica.service.CodigoVerificacionService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import javax.mail.MessagingException;

@RestController
public class RecuperarPasswordApiController implements RecuperarPasswordApi {

    @Autowired
    private CodigoVerificacionService codigoVerificacionService;

    @Override
    public ResponseEntity<RespuestaRs> solicitarCodigo(SolicitarCodigoRq request) throws BadRequestException {
        try {
            RespuestaRs respuesta = codigoVerificacionService.solicitarCodigo(request.getUsername());
            return ResponseEntity.ok(respuesta);
        } catch (MessagingException e) {
            RespuestaRs error = new RespuestaRs();
            error.setStatus(500);
            error.setMensaje("Error al enviar el correo electrónico");
            return ResponseEntity.status(500).body(error);
        }
    }

    @Override
    public ResponseEntity<RespuestaRs> verificarCodigo(VerificarCodigoRq request) throws BadRequestException {
        RespuestaRs respuesta = codigoVerificacionService.verificarCodigo(
            request.getUsername(), request.getCodigo(), request.getNuevaPassword());
        return ResponseEntity.ok(respuesta);
    }
}

package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.EspecializacionApi;
import com.uniminuto.clinica.entity.Especializacion;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.service.EspecializacionService;
import java.util.List;
import javax.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EspecializacionApiController implements EspecializacionApi {

    @Autowired
    private EspecializacionService servicio;

    @Override
    public ResponseEntity<List<Especializacion>> listarEspecializaciones() {
        List<Especializacion> especializaciones = this.servicio.listarTodo();
        return ResponseEntity.ok(especializaciones);
    }

    @Override
    public ResponseEntity<Especializacion> buscarPorCodigo(String codigo) {
        try {
            Especializacion especializacion = this.servicio.buscarEspecializacionPorCod(codigo);
            return ResponseEntity.ok(especializacion);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<RespuestaRs> guardarEspecializacion(@Valid Especializacion request) {
        try {
            this.servicio.guardarEspecializacion(request);
            RespuestaRs respuesta = new RespuestaRs();
            respuesta.setMensaje("Especialización guardada correctamente");
            respuesta.setStatus(200);
            return ResponseEntity.ok(respuesta);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<RespuestaRs> actualizarEspecializacion(Long id, @Valid Especializacion request) {
        try {
            this.servicio.actualizarEspecializacion(id, request);
            RespuestaRs respuesta = new RespuestaRs();
            respuesta.setMensaje("Especialización actualizada correctamente");
            respuesta.setStatus(200);
            return ResponseEntity.ok(respuesta);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
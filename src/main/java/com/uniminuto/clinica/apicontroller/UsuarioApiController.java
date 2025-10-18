package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.UsuarioApi;
import com.uniminuto.clinica.entity.Usuario;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.model.UsuarioRq;
import com.uniminuto.clinica.service.UsuarioSevice;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lmora
 */
@RestController
public class UsuarioApiController implements UsuarioApi {

    @Autowired
    private UsuarioSevice usuarioSevice;

    @Override
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(this.usuarioSevice.encontrarTodosLosUsuarios());
    }

    @Override
    public ResponseEntity<List<Usuario>> listarUsuariosPorRol(String rol) {
        return ResponseEntity.ok(this.usuarioSevice.buscarPorRol(rol));
    }

    @Override
    public ResponseEntity<Usuario> buscarPorUsername(String username)
            throws BadRequestException {
        return ResponseEntity.ok(this.usuarioSevice.buscarPorNombre(username));
    }

    @Override
    public ResponseEntity<List<Usuario>> buscarPorEstado(Integer activo) throws BadRequestException {
        boolean isActivo = activo == 1? true : false;
        return ResponseEntity.ok(this.usuarioSevice
                .encontrarPorActivo(isActivo));
    }

    @Override
    public ResponseEntity<RespuestaRs> guardarUsuarioNuevo(UsuarioRq usuario) 
            throws BadRequestException {
        return ResponseEntity.ok(this.usuarioSevice.guardarUsuario(usuario));
    }

    @Override
    public ResponseEntity<RespuestaRs> actualizarUsuario(UsuarioRq usuario) throws BadRequestException {
        return ResponseEntity.ok(this.usuarioSevice.actualizarUsuario(usuario));
    }

}

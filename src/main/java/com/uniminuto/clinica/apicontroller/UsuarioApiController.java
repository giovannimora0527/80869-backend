package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.UsuarioApi;
import com.uniminuto.clinica.entity.Usuario;
import com.uniminuto.clinica.service.UsuarioSevice;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioApiController implements UsuarioApi {

    @Autowired
    private UsuarioSevice usuarioService;

    @Override
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(this.usuarioService.encontrarTodosLosUsuarios());
    }

    @Override
    public ResponseEntity<List<Usuario>> listarUsuariosPorRol(String rol) {
        return ResponseEntity.ok(this.usuarioService.buscarPorRol(rol));
    }

    @Override
    public ResponseEntity<Usuario> buscarPorUsername(String username) throws BadRequestException {
        return ResponseEntity.ok(this.usuarioService.buscarPorNombre(username));
    }

    @Override
    public ResponseEntity<List<Usuario>> buscarPorEstado(Integer activo) throws BadRequestException {
        boolean isActivo = activo == 1;
        return ResponseEntity.ok(this.usuarioService.encontrarPorActivo(isActivo));
    }
    
    @Override
    public ResponseEntity<Usuario> buscarPorNumeroDocumento(String numeroDocumento) throws BadRequestException {
    Usuario usuario = usuarioService.buscarPorNumeroDocumento(numeroDocumento);
    return ResponseEntity.ok(usuario);
}



}

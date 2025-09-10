package com.clinica;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/por-documento/{numeroDocumento}")
    public ResponseEntity<Usuario> buscarPorDocumentoPaciente(@PathVariable String numeroDocumento) {
        return usuarioService.buscarUsuarioPorNumeroDocumentoPaciente(numeroDocumento)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

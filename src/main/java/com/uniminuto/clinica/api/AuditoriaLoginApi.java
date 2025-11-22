package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.AuditoriaLogin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;


@RequestMapping("/auth")
public interface AuditoriaLoginApi {
    @PostMapping("/auditoria-login")
    ResponseEntity<Void> registrarIntento(@RequestBody AuditoriaLogin auditoriaLogin);

    @GetMapping("/auditoria-login")
    ResponseEntity<Page<AuditoriaLogin>> buscar(
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) Boolean exito,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    );
}
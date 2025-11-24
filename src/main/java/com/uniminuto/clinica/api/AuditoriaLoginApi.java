package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.AuditoriaLogin;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin(origins = "*")
@RequestMapping("/auditoria-login")
public interface AuditoriaLoginApi {

    @RequestMapping(
            value = "/listar",
            method = RequestMethod.GET,
            produces = {"application/json"}
    )
    ResponseEntity<Page<AuditoriaLogin>> listar(
            @RequestParam(required = false) String username,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaDesde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaHasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );
}


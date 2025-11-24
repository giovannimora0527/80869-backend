package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.AuditoriaLoginApi;
import com.uniminuto.clinica.entity.AuditoriaLogin;
import com.uniminuto.clinica.service.AuditoriaLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class AuditoriaLoginApiController implements AuditoriaLoginApi {

    @Autowired
    private AuditoriaLoginService auditoriaLoginService;

    @Override
    public ResponseEntity<Page<AuditoriaLogin>> listar(
            String username,
            LocalDateTime fechaDesde,
            LocalDateTime fechaHasta,
            int page,
            int size
    ) {
        Page<AuditoriaLogin> result = auditoriaLoginService.buscarLogs(
                username, fechaDesde, fechaHasta, page, size
        );
        return ResponseEntity.ok(result);
    }
}


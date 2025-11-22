package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.AuditoriaLoginApi;
import com.uniminuto.clinica.entity.AuditoriaLogin;
import com.uniminuto.clinica.service.AuditoriaLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class AuditoriaLoginApiController implements AuditoriaLoginApi {
    @Autowired
    private AuditoriaLoginService auditoriaLoginService;

    /**
     * Registra un intento de autenticación en la tabla de auditoría.
     *
     * @param auditoriaLogin entidad con datos de intento (usuario, ip, exito, motivo)
    * @return {@code ResponseEntity<Void>} response 200 OK si se registra correctamente
     */
    @Override
    public ResponseEntity<Void> registrarIntento(AuditoriaLogin auditoriaLogin) {
        auditoriaLoginService.registrarIntento(auditoriaLogin);
        return ResponseEntity.ok().build();
    }

    /**
     * Busca registros de auditoría según filtros y paginación.
     *
     * @param usuario filtro por nombre de usuario (nullable)
     * @param exito filtro por resultado de autenticación (nullable)
     * @param start fecha inicial en formato ISO_LOCAL_DATE_TIME (nullable)
     * @param end fecha final en formato ISO_LOCAL_DATE_TIME (nullable)
     * @param page número de página (0-based)
     * @param size tamaño de página
    * @return {@code ResponseEntity<Page<AuditoriaLogin>>} página con resultados y metadata de paginación
     */
    @Override
    public ResponseEntity<Page<AuditoriaLogin>> buscar(String usuario, Boolean exito, String start, String end, int page, int size) {
        LocalDateTime startDt = null;
        LocalDateTime endDt = null;
        try {
            if (start != null && !start.isEmpty()) startDt = LocalDateTime.parse(start);
            if (end != null && !end.isEmpty()) endDt = LocalDateTime.parse(end);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaHora"));
        Page<AuditoriaLogin> resultados = auditoriaLoginService.buscar(usuario, exito, startDt, endDt, pageable);
        return ResponseEntity.ok(resultados);
    }
}

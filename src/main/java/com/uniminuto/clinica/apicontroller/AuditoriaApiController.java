package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.AuditoriaApi;
import com.uniminuto.clinica.entity.AuditoriaLog;
import com.uniminuto.clinica.model.AuditoriaEstadisticasRs;
import com.uniminuto.clinica.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller REST para consultar logs de auditoría del sistema.
 * Permite filtrar por tipo de evento, usuario, nivel y rango de fechas.
 * Incluye endpoint para estadísticas de seguridad.
 * 
 * @author Giovanni Mora Jaimes
 * @version 1.0
 */
@RestController
@Tag(name = "Auditoría", description = "Endpoints para consultar logs y estadísticas de auditoría")
public class AuditoriaApiController implements AuditoriaApi {

    @Autowired
    private AuditoriaService auditoriaService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Consulta logs de auditoría con filtros opcionales.
     * Permite filtrar por tipo de evento, usuario, nivel de severidad y rango de fechas.
     * 
     * @param tipoEvento Tipo de evento (LOGIN_EXITOSO, LOGIN_FALLIDO, USUARIO_BLOQUEADO, etc.)
     * @param username Nombre de usuario que generó el evento
     * @param nivel Nivel de severidad (INFO, WARNING, ERROR, CRITICAL)
     * @param fechaInicio Fecha y hora de inicio del rango (formato ISO: yyyy-MM-dd'T'HH:mm:ss)
     * @param fechaFin Fecha y hora de fin del rango (formato ISO: yyyy-MM-dd'T'HH:mm:ss)
     * @return Lista de logs que cumplen los criterios de filtrado
     */
    @Override
    @Operation(
        summary = "Consultar logs de auditoría",
        description = "Retorna logs de auditoría filtrados por tipo de evento, usuario, nivel y rango de fechas. " +
                     "Todos los filtros son opcionales. Si no se especifican, retorna todos los logs."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de logs recuperados exitosamente.",
            content = @Content(schema = @Schema(implementation = AuditoriaLog.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Formato de fecha inválido. Use formato ISO: yyyy-MM-dd'T'HH:mm:ss"
        )
    })
    public ResponseEntity<List<AuditoriaLog>> consultarLogs(
            @Parameter(description = "Filtrar por tipo de evento", example = "LOGIN_EXITOSO")
            String tipoEvento,
            @Parameter(description = "Filtrar por nombre de usuario", example = "admin")
            String username,
            @Parameter(description = "Filtrar por nivel de severidad", example = "WARNING")
            String nivel,
            @Parameter(description = "Fecha de inicio (ISO format)", example = "2025-11-21T00:00:00")
            String fechaInicio,
            @Parameter(description = "Fecha de fin (ISO format)", example = "2025-11-21T23:59:59")
            String fechaFin) {

        LocalDateTime inicio = null;
        LocalDateTime fin = null;

        try {
            if (fechaInicio != null && !fechaInicio.isEmpty()) {
                inicio = LocalDateTime.parse(fechaInicio, FORMATTER);
            }
            if (fechaFin != null && !fechaFin.isEmpty()) {
                fin = LocalDateTime.parse(fechaFin, FORMATTER);
            }
        } catch (Exception e) {
            System.err.println("Error parseando fechas: " + e.getMessage());
        }

        List<AuditoriaLog> logs = auditoriaService.consultarLogs(
            tipoEvento, username, nivel, inicio, fin
        );

        return ResponseEntity.ok(logs);
    }

    /**
     * Obtiene estadísticas generales de auditoría.
     * Incluye conteo de eventos por tipo, usuarios más activos, y métricas de seguridad.
     * 
     * @return Estadísticas de auditoría del sistema
     */
    @Override
    @Operation(
        summary = "Obtener estadísticas de auditoría",
        description = "Retorna estadísticas agregadas de los logs de auditoría: " +
                     "conteo por tipo de evento, usuarios más activos, intentos fallidos, etc."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Estadísticas recuperadas exitosamente.",
            content = @Content(schema = @Schema(implementation = AuditoriaEstadisticasRs.class))
        )
    })
    public ResponseEntity<AuditoriaEstadisticasRs> obtenerEstadisticas() {
        AuditoriaEstadisticasRs stats = auditoriaService.obtenerEstadisticas();
        return ResponseEntity.ok(stats);
    }
}

package com.uniminuto.clinica.api;

import com.uniminuto.clinica.model.PasswordRecoveryRq;
import com.uniminuto.clinica.model.RespuestaRs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * API para recuperación de contraseñas.
 * Permite a los usuarios solicitar una contraseña temporal cuando olvidan su contraseña.
 * 
 * @author Giovanni Mora Jaimes
 * @version 1.0.0
 * @since 2024
 */
@Tag(name = "Password Recovery", description = "API para recuperación de contraseñas olvidadas")
@RequestMapping("/password-recovery")
public interface PasswordRecoveryApi {

    /**
     * Solicita recuperación de contraseña para un usuario.
     * Por seguridad, siempre retorna el mismo mensaje genérico sin revelar si el usuario existe.
     * 
     * @param request Objeto con el nombre de usuario que solicita recuperación
     * @return Respuesta genérica indicando que se procesó la solicitud
     */
    @Operation(
        summary = "Solicitar recuperación de contraseña",
        description = "Envía una contraseña temporal al correo electrónico registrado del usuario. " +
                      "Por seguridad, siempre retorna el mismo mensaje sin indicar si el usuario existe o no."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Solicitud procesada exitosamente. Si el usuario existe, recibirá un email con contraseña temporal.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RespuestaRs.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de solicitud inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/request")
    ResponseEntity<RespuestaRs> solicitarRecuperacion(
        @Parameter(
            description = "Datos de solicitud de recuperación con el nombre de usuario",
            required = true,
            schema = @Schema(implementation = PasswordRecoveryRq.class)
        )
        @Valid @RequestBody PasswordRecoveryRq request
    );
}

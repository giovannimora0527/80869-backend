package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.AutenticarApi;
import com.uniminuto.clinica.model.AutenticatorRs;
import com.uniminuto.clinica.model.AuthenticatorRq;
import com.uniminuto.clinica.entity.Usuario;
import com.uniminuto.clinica.repository.UsuarioRepository;
import com.uniminuto.clinica.service.AutenticarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Controller REST para autenticación de usuarios.
 * Gestiona el login con control de intentos fallidos y bloqueo temporal.
 * 
 * @author Giovanni Mora Jaimes
 * @version 1.0
 */
@RestController
@Tag(name = "Autenticación", description = "Endpoints para autenticación y gestión de sesiones")
public class AutenticarApiController implements AutenticarApi {

    @Autowired
    private AutenticarService autenticarService;


    /**
     * Autentica un usuario en el sistema.
     * Valida credenciales, controla intentos fallidos (máximo 3) y bloquea por 5 minutos si es necesario.
     * Registra eventos de auditoría para login exitoso, fallido y bloqueo.
     * 
     * @param request Credenciales del usuario (username y password)
     * @return Token JWT si las credenciales son válidas
     * @throws BadRequestException si las credenciales son incorrectas o el usuario está bloqueado
     */
    @Override
    @Operation(
        summary = "Autenticar usuario",
        description = "Valida las credenciales del usuario y retorna un token JWT. " +
                     "Controla intentos fallidos (máximo 3) y bloquea temporalmente por 5 minutos. " +
                     "Registra todos los eventos en auditoría."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autenticación exitosa. Retorna token JWT y datos del usuario.",
            content = @Content(schema = @Schema(implementation = AutenticatorRs.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Credenciales incorrectas o usuario bloqueado temporalmente."
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Usuario no autorizado o inactivo."
        )
    })
    public ResponseEntity<AutenticatorRs> autenticar(AuthenticatorRq request) throws BadRequestException {
        return ResponseEntity.ok(this.autenticarService.autenticar(request));
    }
}


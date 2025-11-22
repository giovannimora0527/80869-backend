package com.uniminuto.clinica.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

@Component
public class JwtTokenFilter extends GenericFilterBean {

    /**
     * Log.
     */
    private static final Logger LOG = Logger.getLogger(JwtTokenFilter.class.getName());

    /**
     * Filtro JWT.
     * @param req Request.
     * @param res Response.
     * @param filterChain Filter chain.
     * @throws IOException IOException.
     * @throws ServletException ServletException.
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        // Documentación rápida: este filtro intercepta cada petición HTTP y valida
        // la presencia y validez de un JWT en el header `Authorization`.
        // Establece la autenticación en el SecurityContext cuando el token es válido.

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String authHeader = request.getHeader("Authorization");
        LOG.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        // Log diagnóstico: no mostrar token completo, solo presencia y longitud
        if (request.getRequestURI() != null && request.getRequestURI().contains("/auth/auditoria-login")) {
            boolean hasAuth = authHeader != null;
            boolean startsBearer = hasAuth && authHeader.startsWith("Bearer ");
            int tokenLen = startsBearer ? authHeader.length() - 7 : 0;
            LOG.info(String.format("Diagnostics: authHeader present=%s, startsWithBearer=%s, tokenLen=%d", hasAuth, startsBearer, tokenLen));
            // También mostrar autenticación actual en el contexto (puede ser null)
            try {
                LOG.info("SecurityContext Authentication: " + org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication());
            } catch (Exception ex) {
                LOG.warning("Unable to read SecurityContext authentication: " + ex.getMessage());
            }
        }

        String requestURI = request.getRequestURI();
        if (requestURI != null && (requestURI.contains("/auth/recuperar-contrasena")
                || requestURI.contains("/auth/login")
                || requestURI.contains("/auth/solicitar-codigo")
                || requestURI.contains("/auth/verificar-codigo"))) {
            try {
                filterChain.doFilter(req, res);
            } catch (BadRequestException e) {
                LOG.log(Level.WARNING, "Error en la petición: {0}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\": \"Bad Request\","
                        + " \"message\": \"" + e.getMessage() + "\"}");
                return;
            }
            LOG.info("Petición a /auth/login, /auth/recuperar-contrasena, /auth/solicitar-codigo o /auth/verificar-codigo, se permite sin token.");
            return;
        } else if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if (isTokenExpired(token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\":\"Token expirado. Por favor, inicie sesión nuevamente.\"}");
                    response.flushBuffer();
                    return;
                }
                // Si el token es válido, establecer autenticación en el contexto de Spring
                DecodedJWT jwt = JWT.decode(token);
                String username = jwt.getSubject();
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, Collections.singletonList(new SimpleGrantedAuthority("USER"))
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException eje) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Token expirado. Por favor, inicie sesión nuevamente.\"}");
                response.flushBuffer();
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Token inválido.\"}");
                response.flushBuffer();
                return;
            }
        }
        filterChain.doFilter(req, res);
    }

    /**
     * Valida si el token JWT está expirado.
     * @param token JWT.
     * @return true si está expirado, false si es válido.
     */
    private boolean isTokenExpired(String token) {
        DecodedJWT jwt = JWT.decode(token);
        // Extrae el claim personalizado "fecha_fin_sesion" como Date
        java.util.Date fechaFinSesion = jwt.getClaim("fecha_fin_sesion").asDate();
        if (fechaFinSesion == null) {
            return false; // Si no tiene expiración, se considera válido
        }
        return fechaFinSesion.before(new java.util.Date());
    }
}

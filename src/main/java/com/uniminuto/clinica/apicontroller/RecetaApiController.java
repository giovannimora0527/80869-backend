package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.RecetaApi;
import com.uniminuto.clinica.dto.CrearRecetaRequestDto;
import com.uniminuto.clinica.dto.CrearRecetaResponseDto;
import com.uniminuto.clinica.dto.RecetaListadoDto;
import com.uniminuto.clinica.dto.RecetaFiltroDto;
import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.service.RecetaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.util.List;

/**
 * Controlador REST para operaciones de recetas médicas
 * Maneja la creación y gestión de prescripciones médicas
 */
@RestController
@Slf4j
@Validated
public class RecetaApiController implements RecetaApi {

    @Autowired
    private RecetaService recetaService;

    @Override
    public ResponseEntity<Receta> crearReceta(Receta receta) {
        return ResponseEntity.ok(recetaService.crearReceta(receta));
    }

    @Override
    public ResponseEntity<List<Receta>> listarRecetas() {
        return ResponseEntity.ok(recetaService.listarRecetas());
    }

    /**
     * Crear una nueva receta médica completa
     * Solo médicos autenticados pueden crear recetas
     */
    @Override
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<CrearRecetaResponseDto> crearRecetaCompleta(
            @Valid CrearRecetaRequestDto request) {
        
        try {
            log.info("Iniciando creación de receta para cita: {}", request.getCitaId());
            
            // Por ahora usamos un médico ID de ejemplo - en producción se obtendría del contexto de seguridad
            Long medicoId = 1L; // TODO: Obtener del contexto de seguridad
            
            // Crear la receta usando el servicio
            CrearRecetaResponseDto response = recetaService.crearRecetaCompleta(request, medicoId);
            
            log.info("Receta creada exitosamente con ID: {}", response.getRecetaId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear receta: {}", e.getMessage());
            throw e;
            
        } catch (IllegalStateException e) {
            log.warn("Error de estado al crear receta: {}", e.getMessage());
            throw e;
            
        } catch (SecurityException e) {
            log.warn("Error de autorización al crear receta: {}", e.getMessage());
            throw e;
            
        } catch (Exception e) {
            log.error("Error inesperado al crear receta", e);
            throw new RuntimeException("Error interno del servidor al crear la receta", e);
        }
    }
    
    /**
     * Validar si una cita está en estado apropiado para crear receta
     */
    @Override
    public ResponseEntity<Boolean> validarCitaParaReceta(@PathVariable("citaId") Long citaId) {
        try {
            log.info("Validando cita {} para creación de receta", citaId);
            
            boolean esValida = recetaService.validarEstadoCitaParaReceta(citaId);
            
            log.info("Resultado de validación para cita {}: {}", citaId, esValida);
            
            return ResponseEntity.ok(esValida);
            
        } catch (Exception e) {
            log.error("Error al validar cita para receta", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
    
    /**
     * Listar recetas con filtros y paginación
     */
    @Override
    public ResponseEntity<org.springframework.data.domain.Page<com.uniminuto.clinica.dto.RecetaListadoDto>> listarRecetasConFiltros(
            Integer pagina, Integer tamano, String ordenarPor, String direccion,
            Long pacienteId, Long medicoId, Long medicamentoId,
            String fechaDesde, String fechaHasta, String texto) {
        
        try {
            log.info("Listando recetas con filtros - página: {}, tamaño: {}", pagina, tamano);
            
            // Crear DTO de filtros
            RecetaFiltroDto filtros = RecetaFiltroDto.builder()
                    .pagina(pagina)
                    .tamano(tamano)
                    .ordenarPor(ordenarPor)
                    .direccion(direccion)
                    .pacienteId(pacienteId)
                    .medicoId(medicoId)
                    .medicamentoId(medicamentoId)
                    .texto(texto)
                    .build();
            
            // Parsear fechas si están presentes
            if (fechaDesde != null && !fechaDesde.trim().isEmpty()) {
                filtros.setFechaDesde(java.time.LocalDateTime.parse(fechaDesde));
            }
            if (fechaHasta != null && !fechaHasta.trim().isEmpty()) {
                filtros.setFechaHasta(java.time.LocalDateTime.parse(fechaHasta));
            }
            
            org.springframework.data.domain.Page<com.uniminuto.clinica.dto.RecetaListadoDto> resultado = 
                recetaService.listarRecetasConFiltros(filtros);
            
            log.info("Encontradas {} recetas en {} páginas", 
                    resultado.getTotalElements(), resultado.getTotalPages());
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            log.error("Error al listar recetas con filtros", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    /**
     * Listar recetas de un paciente específico
     */
    @Override
    public ResponseEntity<org.springframework.data.domain.Page<com.uniminuto.clinica.dto.RecetaListadoDto>> listarRecetasPorPaciente(
            Long pacienteId, Integer pagina, Integer tamano) {
        
        try {
            log.info("Listando recetas del paciente {} - página: {}, tamaño: {}", 
                    pacienteId, pagina, tamano);
            
            org.springframework.data.domain.Page<com.uniminuto.clinica.dto.RecetaListadoDto> resultado = 
                recetaService.listarRecetasPorPaciente(pacienteId, pagina, tamano);
            
            log.info("Encontradas {} recetas para el paciente {}", 
                    resultado.getTotalElements(), pacienteId);
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            log.error("Error al listar recetas del paciente {}", pacienteId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    /**
     * Listar recetas de un médico específico
     */
    @Override
    public ResponseEntity<org.springframework.data.domain.Page<com.uniminuto.clinica.dto.RecetaListadoDto>> listarRecetasPorMedico(
            Long medicoId, Integer pagina, Integer tamano) {
        
        try {
            log.info("Listando recetas del médico {} - página: {}, tamaño: {}", 
                    medicoId, pagina, tamano);
            
            org.springframework.data.domain.Page<com.uniminuto.clinica.dto.RecetaListadoDto> resultado = 
                recetaService.listarRecetasPorMedico(medicoId, pagina, tamano);
            
            log.info("Encontradas {} recetas para el médico {}", 
                    resultado.getTotalElements(), medicoId);
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            log.error("Error al listar recetas del médico {}", medicoId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}



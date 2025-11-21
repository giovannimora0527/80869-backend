package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.AuditoriaLog;
import com.uniminuto.clinica.model.AuditoriaEstadisticasRs;
import com.uniminuto.clinica.repository.AuditoriaLogRepository;
import com.uniminuto.clinica.service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación del servicio de auditoría del sistema.
 * 
 * <p>Este servicio proporciona funcionalidades para:</p>
 * <ul>
 *   <li>Consultar logs de auditoría con múltiples filtros</li>
 *   <li>Obtener estadísticas agregadas de eventos de seguridad</li>
 *   <li>Registrar nuevos eventos de auditoría</li>
 * </ul>
 * 
 * <p>Los logs de auditoría incluyen información sobre:</p>
 * <ul>
 *   <li>Intentos de login exitosos y fallidos</li>
 *   <li>Bloqueos temporales de usuarios</li>
 *   <li>Recuperaciones de contraseña</li>
 *   <li>Cambios de contraseña</li>
 *   <li>Operaciones críticas del sistema</li>
 * </ul>
 * 
 * @author Giovanni Mora Jaimes
 * @version 1.0
 * @since 2025-11-21
 * @see AuditoriaService
 * @see AuditoriaLog
 */
@Service
public class AuditoriaServiceImpl implements AuditoriaService {

    @Autowired
    private AuditoriaLogRepository auditoriaLogRepository;

    /**
     * Consulta logs de auditoría aplicando filtros opcionales.
     * 
     * <p>Permite filtrar por múltiples criterios simultáneamente:</p>
     * <ul>
     *   <li>Tipo de evento (LOGIN_EXITOSO, LOGIN_FALLIDO, USUARIO_BLOQUEADO, etc.)</li>
     *   <li>Nombre de usuario que generó el evento</li>
     *   <li>Nivel de severidad (INFO, WARNING, ERROR, CRITICAL)</li>
     *   <li>Rango de fechas (inicio y fin)</li>
     * </ul>
     * 
     * <p>Todos los filtros son opcionales. Si se omiten, retorna todos los logs.</p>
     * 
     * @param tipoEvento Tipo de evento a filtrar (puede ser null)
     * @param username Usuario que generó el evento (puede ser null)
     * @param nivel Nivel de severidad del evento (puede ser null)
     * @param fechaInicio Fecha y hora de inicio del rango (puede ser null)
     * @param fechaFin Fecha y hora de fin del rango (puede ser null)
     * @return Lista de logs que cumplen los criterios especificados
     */
    @Override
    public List<AuditoriaLog> consultarLogs(String tipoEvento, String username, String nivel,
                                            LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return auditoriaLogRepository.buscarConFiltros(
            tipoEvento, username, nivel, fechaInicio, fechaFin
        );
    }

    /**
     * Obtiene estadísticas agregadas de los logs de auditoría.
     * 
     * <p>Calcula y retorna las siguientes métricas:</p>
     * <ul>
     *   <li>Total de eventos registrados en el sistema</li>
     *   <li>Conteo de eventos por tipo (LOGIN_EXITOSO, LOGIN_FALLIDO, etc.)</li>
     *   <li>Conteo de eventos por nivel de severidad (INFO, WARNING, ERROR, CRITICAL)</li>
     *   <li>Número de logins exitosos</li>
     *   <li>Número de logins fallidos</li>
     *   <li>Número de usuarios bloqueados</li>
     * </ul>
     * 
     * <p>Estas estadísticas son útiles para:</p>
     * <ul>
     *   <li>Monitoreo de seguridad del sistema</li>
     *   <li>Detección de patrones de ataque</li>
     *   <li>Análisis de comportamiento de usuarios</li>
     *   <li>Reportes de cumplimiento y auditoría</li>
     * </ul>
     * 
     * @return Objeto AuditoriaEstadisticasRs con todas las métricas calculadas
     */
    @Override
    public AuditoriaEstadisticasRs obtenerEstadisticas() {
        AuditoriaEstadisticasRs stats = new AuditoriaEstadisticasRs();
        
        // Total de eventos
        stats.setTotalEventos(auditoriaLogRepository.count());
        
        // Eventos por tipo
        Map<String, Long> eventosPorTipo = new HashMap<>();
        List<Object[]> tipoEventos = auditoriaLogRepository.contarPorTipoEvento();
        for (Object[] row : tipoEventos) {
            eventosPorTipo.put((String) row[0], (Long) row[1]);
        }
        stats.setEventosPorTipo(eventosPorTipo);
        
        // Eventos por nivel
        Map<String, Long> eventosPorNivel = new HashMap<>();
        List<Object[]> niveles = auditoriaLogRepository.contarPorNivel();
        for (Object[] row : niveles) {
            eventosPorNivel.put((String) row[0], (Long) row[1]);
        }
        stats.setEventosPorNivel(eventosPorNivel);
        
        // Estadísticas específicas
        stats.setLoginExitosos(eventosPorTipo.getOrDefault("LOGIN_EXITOSO", 0L));
        stats.setLoginFallidos(eventosPorTipo.getOrDefault("LOGIN_FALLIDO", 0L));
        stats.setUsuariosBloqueados(eventosPorTipo.getOrDefault("USUARIO_BLOQUEADO", 0L));
        
        return stats;
    }

    /**
     * Registra un nuevo log de auditoría en la base de datos.
     * 
     * <p>Este método persiste eventos de auditoría que incluyen:</p>
     * <ul>
     *   <li>Fecha y hora del evento</li>
     *   <li>Tipo de evento y nivel de severidad</li>
     *   <li>Usuario relacionado (ID y username)</li>
     *   <li>Dirección IP del cliente</li>
     *   <li>Descripción y datos adicionales en formato JSON</li>
     * </ul>
     * 
     * @param log Objeto AuditoriaLog con toda la información del evento a registrar
     */
    @Override
    public void registrarLog(AuditoriaLog log) {
        auditoriaLogRepository.save(log);
    }
}

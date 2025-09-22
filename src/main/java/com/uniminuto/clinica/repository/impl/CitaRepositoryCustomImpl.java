package com.uniminuto.clinica.repository.impl;

import com.uniminuto.clinica.dto.ListarCitasRequestDto;
import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.repository.CitaRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del repositorio personalizado para consultas complejas de citas.
 * Utiliza Criteria API para construir consultas dinámicas con filtros múltiples.
 *
 * @author AI
 */
@Repository
public class CitaRepositoryCustomImpl implements CitaRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Cita> findCitasConFiltros(ListarCitasRequestDto filtros, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
        // Consulta para obtener los datos
        CriteriaQuery<Cita> dataQuery = cb.createQuery(Cita.class);
        Root<Cita> citaRoot = dataQuery.from(Cita.class);
        
        // Hacer fetch joins para evitar N+1 queries
        citaRoot.fetch("paciente", JoinType.LEFT);
        citaRoot.fetch("medico", JoinType.LEFT);
        
        // Construir predicados (condiciones WHERE)
        List<Predicate> predicates = construirPredicados(cb, citaRoot, filtros, null, null);
        
        if (!predicates.isEmpty()) {
            dataQuery.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        
        // Aplicar ordenamiento
        List<Order> orders = construirOrdenamiento(cb, citaRoot, filtros);
        dataQuery.orderBy(orders);
        
        // Crear consulta tipada
        TypedQuery<Cita> typedQuery = entityManager.createQuery(dataQuery);
        
        // Aplicar paginación
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        
        List<Cita> resultados = typedQuery.getResultList();
        
        // Consulta para obtener el total de elementos
        long total = contarTotalElementos(filtros, null, null);
        
        return new PageImpl<>(resultados, pageable, total);
    }

    @Override
    public Page<Cita> findCitasConFiltrosYAutorizacion(
            ListarCitasRequestDto filtros, 
            Pageable pageable,
            Long usuarioId,
            String tipoUsuario) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
        // Consulta para obtener los datos
        CriteriaQuery<Cita> dataQuery = cb.createQuery(Cita.class);
        Root<Cita> citaRoot = dataQuery.from(Cita.class);
        
        // Hacer fetch joins para evitar N+1 queries
        citaRoot.fetch("paciente", JoinType.LEFT);
        citaRoot.fetch("medico", JoinType.LEFT);
        
        // Construir predicados incluyendo autorización
        List<Predicate> predicates = construirPredicados(cb, citaRoot, filtros, usuarioId, tipoUsuario);
        
        if (!predicates.isEmpty()) {
            dataQuery.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        
        // Aplicar ordenamiento
        List<Order> orders = construirOrdenamiento(cb, citaRoot, filtros);
        dataQuery.orderBy(orders);
        
        // Crear consulta tipada
        TypedQuery<Cita> typedQuery = entityManager.createQuery(dataQuery);
        
        // Aplicar paginación
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        
        List<Cita> resultados = typedQuery.getResultList();
        
        // Consulta para obtener el total de elementos
        long total = contarTotalElementos(filtros, usuarioId, tipoUsuario);
        
        return new PageImpl<>(resultados, pageable, total);
    }

    /**
     * Construye los predicados (condiciones WHERE) para la consulta.
     */
    private List<Predicate> construirPredicados(
            CriteriaBuilder cb, 
            Root<Cita> citaRoot, 
            ListarCitasRequestDto filtros,
            Long usuarioId,
            String tipoUsuario) {
        
        List<Predicate> predicates = new ArrayList<>();
        
        // Filtro por médico
        if (filtros.getMedicoId() != null) {
            predicates.add(cb.equal(citaRoot.get("medico").get("id"), filtros.getMedicoId()));
        }
        
        // Filtro por paciente
        if (filtros.getPacienteId() != null) {
            predicates.add(cb.equal(citaRoot.get("paciente").get("id"), filtros.getPacienteId()));
        }
        
        // Filtro por estado
        if (StringUtils.hasText(filtros.getEstado())) {
            predicates.add(cb.equal(citaRoot.get("estado"), filtros.getEstado()));
        }
        
        // Filtro por rango de fechas
        if (filtros.getDesde() != null) {
            predicates.add(cb.greaterThanOrEqualTo(citaRoot.get("fechaHora"), filtros.getDesde()));
        }
        
        if (filtros.getHasta() != null) {
            predicates.add(cb.lessThanOrEqualTo(citaRoot.get("fechaHora"), filtros.getHasta()));
        }
        
        // Aplicar filtros de autorización según el tipo de usuario
        if (usuarioId != null && tipoUsuario != null) {
            switch (tipoUsuario.toUpperCase()) {
                case "PACIENTE":
                    // Los pacientes solo pueden ver sus propias citas
                    predicates.add(cb.equal(citaRoot.get("paciente").get("usuarioId"), usuarioId));
                    break;
                case "MEDICO":
                    // Los médicos solo pueden ver sus propias citas
                    // Asumiendo que existe una relación entre médico y usuario
                    predicates.add(cb.equal(citaRoot.get("medico").get("id"), usuarioId));
                    break;
                case "ADMIN":
                    // Los administradores pueden ver todas las citas (no se agrega filtro)
                    break;
            }
        }
        
        return predicates;
    }

    /**
     * Construye el ordenamiento para la consulta.
     */
    private List<Order> construirOrdenamiento(
            CriteriaBuilder cb, 
            Root<Cita> citaRoot, 
            ListarCitasRequestDto filtros) {
        
        List<Order> orders = new ArrayList<>();
        
        String sortBy = filtros.getSortBy();
        String sortDir = filtros.getSortDir();
        
        // Ordenamiento principal
        if ("fechaHora".equals(sortBy)) {
            if ("asc".equalsIgnoreCase(sortDir)) {
                orders.add(cb.asc(citaRoot.get("fechaHora")));
            } else {
                orders.add(cb.desc(citaRoot.get("fechaHora")));
            }
        } else if ("id".equals(sortBy)) {
            if ("asc".equalsIgnoreCase(sortDir)) {
                orders.add(cb.asc(citaRoot.get("id")));
            } else {
                orders.add(cb.desc(citaRoot.get("id")));
            }
        } else if ("estado".equals(sortBy)) {
            if ("asc".equalsIgnoreCase(sortDir)) {
                orders.add(cb.asc(citaRoot.get("estado")));
            } else {
                orders.add(cb.desc(citaRoot.get("estado")));
            }
        }
        
        // Tie-breaker: siempre ordenar por id desc como segundo criterio
        if (!"id".equals(sortBy)) {
            orders.add(cb.desc(citaRoot.get("id")));
        }
        
        return orders;
    }

    /**
     * Cuenta el total de elementos que coinciden con los filtros.
     */
    private long contarTotalElementos(
            ListarCitasRequestDto filtros,
            Long usuarioId,
            String tipoUsuario) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Cita> citaRoot = countQuery.from(Cita.class);
        
        countQuery.select(cb.count(citaRoot));
        
        // Aplicar los mismos predicados que en la consulta de datos
        List<Predicate> predicates = construirPredicados(cb, citaRoot, filtros, usuarioId, tipoUsuario);
        
        if (!predicates.isEmpty()) {
            countQuery.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
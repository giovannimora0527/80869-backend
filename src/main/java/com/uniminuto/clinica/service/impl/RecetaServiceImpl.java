package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.model.RecetaCrearRq;
import com.uniminuto.clinica.repository.CitaRepository;
import com.uniminuto.clinica.repository.RecetaRepository;
import com.uniminuto.clinica.service.RecetaService;
import org.springframework.stereotype.Service;
import java.util.List;

/** Implementación de servicio para Recetas. */
@Service
public class RecetaServiceImpl implements RecetaService {

  private final RecetaRepository repo;
  private final CitaRepository citaRepo;

  public RecetaServiceImpl(RecetaRepository repo, CitaRepository citaRepo) {
    this.repo = repo; this.citaRepo = citaRepo;
  }

  @Override
  public Receta crear(RecetaCrearRq rq) {
    if (rq.getCitaId()==null || rq.getMedicamentoId()==null || rq.getDosis()==null) {
      throw new IllegalArgumentException("Datos obligatorios: citaId, medicamentoId, dosis");
    }
    // Validar que la cita exista
    citaRepo.findById(Long.valueOf(rq.getCitaId()))
            .orElseThrow(() -> new IllegalArgumentException("La cita no existe"));
    Receta r = new Receta();
    r.setCitaId(rq.getCitaId());
    r.setMedicamentoId(rq.getMedicamentoId());
    r.setDosis(rq.getDosis());
    r.setIndicaciones(rq.getIndicaciones());
    return repo.save(r);
  }

  @Override
  public List<Receta> listar() {
    return repo.findAllByOrderByFechaCreacionRegistroDesc();
  }
}
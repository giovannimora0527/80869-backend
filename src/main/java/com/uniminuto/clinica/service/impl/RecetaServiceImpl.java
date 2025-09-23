package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.entity.Medicamento;
import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.model.RecetaRq;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.repository.CitaRepository;
import com.uniminuto.clinica.repository.MedicamentoRepository;
import com.uniminuto.clinica.repository.RecetaRepository;
import com.uniminuto.clinica.service.RecetaService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecetaServiceImpl implements RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Override
    public List<Receta> obtenerTodasLasRecetas() {
        return this.recetaRepository.findAllByOrderByFechaCreacionRegistroDesc();
    }

    @Override
    public RespuestaRs guardarReceta(RecetaRq recetaRq) throws BadRequestException {
        Optional<Cita> citaOpt = this.citaRepository.findById(recetaRq.getCitaId());
        if (!citaOpt.isPresent()) {
            throw new BadRequestException("No existe la cita asociada");
        }
        Optional<Medicamento> medicamentoOpt = medicamentoRepository
                .findById(recetaRq.getMedicamentoId());
        if (!medicamentoOpt.isPresent()) {
            throw new BadRequestException("No existe el medicamento asociado");
        }

        List<Receta> recetasExistentes = this.recetaRepository
                .findByCitaAndMedicamento(citaOpt.get(), medicamentoOpt.get());
        if (!recetasExistentes.isEmpty()) {
            throw new BadRequestException("Ya existe una receta para la cita y medicamento indicados");
        }

        Receta recetaAGuardar = convertCitaRqToReceta(recetaRq);
        recetaAGuardar.setCita(citaOpt.get());
        recetaAGuardar.setMedicamento(medicamentoOpt.get());
        this.recetaRepository.save(recetaAGuardar);
        RespuestaRs rta = new RespuestaRs();
        rta.setStatus(200);
        rta.setMensaje("Receta guardada exitosamente");
        return rta;
    }

    private Receta convertCitaRqToReceta(RecetaRq recetaRq) {
        Receta receta = new Receta();
        receta.setDosis(recetaRq.getDosis());
        receta.setIndicaciones(recetaRq.getIndicaciones());
        receta.setFechaCreacionRegistro(LocalDateTime.now());
        return receta;
    }


}

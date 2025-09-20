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
    public RespuestaRs guardarReceta(RecetaRq recetaRq) {
        RespuestaRs respuesta = new RespuestaRs();

        // Validación campos obligatorios
        if (recetaRq.getCitaId() == null) {
            respuesta.setMensaje(" El campo citaId es obligatorio");
            respuesta.setEstaFuncionando(false);
            respuesta.setStatus(400);
            return respuesta;
        }
        if (recetaRq.getMedicamentoId() == null) {
            respuesta.setMensaje(" El campo medicamentoId es obligatorio");
            respuesta.setEstaFuncionando(false);
            respuesta.setStatus(400);
            return respuesta;
        }

        // Validar cita
        Optional<Cita> citaOpt = citaRepository.findById(recetaRq.getCitaId());
        if (!citaOpt.isPresent()) {
            respuesta.setMensaje(" No existe la cita con id " + recetaRq.getCitaId());
            respuesta.setEstaFuncionando(false);
            respuesta.setStatus(404);
            return respuesta;
        }

        // Validar medicamento
        Optional<Medicamento> medicamentoOpt = medicamentoRepository.findById(recetaRq.getMedicamentoId());
        if (!medicamentoOpt.isPresent()) {
            respuesta.setMensaje(" No existe el medicamento con id " + recetaRq.getMedicamentoId());
            respuesta.setEstaFuncionando(false);
            respuesta.setStatus(404);
            return respuesta;
        }

        // Crear receta
        Receta receta = new Receta();
        receta.setCita(citaOpt.get());
        receta.setMedicamento(medicamentoOpt.get());
        receta.setDosis(recetaRq.getDosis());
        receta.setIndicaciones(recetaRq.getIndicaciones());
        receta.setFechaCreacionRegistro(LocalDateTime.now());
        recetaRepository.save(receta);

        // Respuesta final
        respuesta.setMensaje(" Receta guardada con éxito");
        respuesta.setEstaFuncionando(true);
        respuesta.setStatus(201);
        return respuesta;
    }

    @Override
    public List<Receta> listarRecetas() {
        return recetaRepository.findAll();
    }

    @Override
    public List<Receta> listarRecetasFechaCreacion() {
        return recetaRepository.findAllByOrderByFechaCreacionRegistroDesc();
    }
}

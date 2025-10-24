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
    public List<Receta> listarRecetas() {
        return this.recetaRepository.findAllByOrderByFechaCreacionRegistroDesc();
    }

    @Override
    public RespuestaRs guardarReceta(RecetaRq recetaRq) throws BadRequestException {
        validarCampos(recetaRq);

        Optional<Cita> citaOpt = this.citaRepository.findById(recetaRq.getCitaId());
        if (!citaOpt.isPresent()) {
            throw new BadRequestException("No existe la cita asociada");
        }

        Optional<Medicamento> medicamentoOpt = this.medicamentoRepository.findById(recetaRq.getMedicamentoId());
        if (!medicamentoOpt.isPresent()) {
            throw new BadRequestException("No existe el medicamento asociado");
        }

        List<Receta> recetasExistentes =
                this.recetaRepository.findByCitaAndMedicamento(citaOpt.get(), medicamentoOpt.get());
        if (!recetasExistentes.isEmpty()) {
            throw new BadRequestException("Ya existe una receta para la cita y medicamento indicados");
        }

        Receta recetaAGuardar = convertirAReceta(recetaRq);
        recetaAGuardar.setCita(citaOpt.get());
        recetaAGuardar.setMedicamento(medicamentoOpt.get());
        recetaRepository.save(recetaAGuardar);

        RespuestaRs rta = new RespuestaRs();
        rta.setStatus(200);
        rta.setMensaje("Receta guardada exitosamente");
        return rta;
    }

    @Override
    public RespuestaRs actualizarReceta(RecetaRq recetaRq) throws BadRequestException {
        if (recetaRq.getId() == null) {
            throw new BadRequestException("El id de la receta es obligatorio para actualizar");
        }

        Optional<Receta> recetaOpt = this.recetaRepository.findById(recetaRq.getId());
        if (!recetaOpt.isPresent()) {
            throw new BadRequestException("No se puede actualizar la receta porque no existe");
        }

        Receta recetaActual = recetaOpt.get();

        // Validar relaciones
        Optional<Cita> citaOpt = this.citaRepository.findById(recetaRq.getCitaId());
        if (!citaOpt.isPresent()) {
            throw new BadRequestException("No existe la cita asociada");
        }

        Optional<Medicamento> medicamentoOpt = this.medicamentoRepository.findById(recetaRq.getMedicamentoId());
        if (!medicamentoOpt.isPresent()) {
            throw new BadRequestException("No existe el medicamento asociado");
        }

        // Evitar duplicados de receta con la misma cita y medicamento
        List<Receta> recetasExistentes =
                this.recetaRepository.findByCitaAndMedicamento(citaOpt.get(), medicamentoOpt.get());
        if (!recetasExistentes.isEmpty()
                && !recetasExistentes.get(0).getId().equals(recetaActual.getId())) {
            throw new BadRequestException("Ya existe una receta con la misma cita y medicamento");
        }

        // Actualizar campos
        recetaActual.setCita(citaOpt.get());
        recetaActual.setMedicamento(medicamentoOpt.get());
        recetaActual.setDosis(recetaRq.getDosis() != null ? recetaRq.getDosis() : recetaActual.getDosis());
        recetaActual.setIndicaciones(recetaRq.getIndicaciones() != null ? recetaRq.getIndicaciones() : recetaActual.getIndicaciones());
        recetaActual.setFechaCreacionRegistro(LocalDateTime.now());

        this.recetaRepository.save(recetaActual);

        RespuestaRs rta = new RespuestaRs();
        rta.setStatus(200);
        rta.setMensaje("Receta actualizada exitosamente");
        return rta;
    }

    private Receta convertirAReceta(RecetaRq recetaRq) {
        Receta receta = new Receta();
        receta.setDosis(recetaRq.getDosis());
        receta.setIndicaciones(recetaRq.getIndicaciones());
        receta.setFechaCreacionRegistro(LocalDateTime.now());
        return receta;
    }

    private void validarCampos(RecetaRq recetaRq) throws BadRequestException {
        if (recetaRq.getCitaId() == null) {
            throw new BadRequestException("El campo citaId es obligatorio");
        }
        if (recetaRq.getMedicamentoId() == null) {
            throw new BadRequestException("El campo medicamentoId es obligatorio");
        }
        if (recetaRq.getDosis() == null || recetaRq.getDosis().isEmpty()) {
            throw new BadRequestException("El campo dosis es obligatorio");
        }
        if (recetaRq.getIndicaciones() == null || recetaRq.getIndicaciones().isEmpty()) {
            throw new BadRequestException("El campo indicaciones es obligatorio");
        }
    }
}

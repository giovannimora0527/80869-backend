package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Medicamento;
import com.uniminuto.clinica.model.MedicamentoRq;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.repository.MedicamentoRepository;
import com.uniminuto.clinica.service.MedicamentoService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar la entidad Medicamento.
 * Ajustado para trabajar con la entidad Medicamento simplificada (sin campos de fecha ni auditoría).
 */
@Service
public class MedicamentoServiceImpl implements MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Override
    public List<Medicamento> listarMedicamentos() {
        // Lista todos los medicamentos sin ordenamiento por fecha (campo eliminado)
        return medicamentoRepository.findAll();
    }

    @Override
    public RespuestaRs guardarMedicamento(MedicamentoRq medicamento)
            throws BadRequestException {
        // Paso 1. Validar mi objeto de entrada
        this.validarFormulario(medicamento);

        // Paso 2. Validar que no exista un medicamento con el mismo nombre (clave única)
        Optional<Medicamento> optMedicamento = this.medicamentoRepository
                .findByNombre(medicamento.getNombre());

        if (optMedicamento.isPresent()) {
            throw new BadRequestException("Ya existe un medicamento registrado con este nombre. Intente de nuevo");
        }

        // Paso 3. Crear el objeto Medicamento y guardarlo
        Medicamento nuevo = this.convertirAMedicamento(medicamento);
        this.medicamentoRepository.save(nuevo);

        // Paso 4. Retornar mensaje de éxito
        RespuestaRs rta = new RespuestaRs();
        rta.setStatus(200);
        rta.setMensaje("Medicamento creado exitosamente");
        return rta;
    }

    @Override
    public RespuestaRs actualizarMedicamento(MedicamentoRq medicamento) throws BadRequestException {
        // Paso 1. Valido si el id llega y busco el registro
        if (medicamento.getId() == null) {
            throw new BadRequestException("El id del medicamento es obligatorio");
        }

        Optional<Medicamento> optMedicamento = this.medicamentoRepository
                .findById(medicamento.getId());

        if (!optMedicamento.isPresent()) {
            throw new BadRequestException("No se puede actualizar el medicamento porque no existe.");
        }

        // Paso 2. Si existe, modifico los campos a actualizar
        Medicamento medicamentoActual = optMedicamento.get();

        // Validar si el nombre cambió y si el nuevo nombre ya existe
        if (medicamento.getNombre() != null && !medicamentoActual.getNombre().toLowerCase()
                .equals(medicamento.getNombre().toLowerCase())) {

            Optional<Medicamento> optBuscar = this.medicamentoRepository
                    .findByNombre(medicamento.getNombre());

            if (optBuscar.isPresent()) {
                throw new BadRequestException("Ya existe un medicamento registrado con el nombre '" + medicamento.getNombre() + "'.");
            }
        }

        // Actualización de campos (solo los presentes en la entidad simplificada)
        medicamentoActual.setNombre(medicamento.getNombre() == null ? medicamentoActual.getNombre() : medicamento.getNombre());
        medicamentoActual.setPresentacion(medicamento.getPresentacion() == null ? medicamentoActual.getPresentacion() : medicamento.getPresentacion());
        medicamentoActual.setDescripcion(medicamento.getDescripcion() == null ? medicamentoActual.getDescripcion() : medicamento.getDescripcion());
        // Eliminada referencia a fechaModificacionRegistro

        this.medicamentoRepository.save(medicamentoActual);

        // Paso 3. Retorno mensaje de éxito
        RespuestaRs rta = new RespuestaRs();
        rta.setMensaje("Se ha actualizado el registro satisfactoriamente");
        rta.setStatus(200);
        return rta;
    }

    private Medicamento convertirAMedicamento(MedicamentoRq medicamento) {
        Medicamento nuevo = new Medicamento();
        nuevo.setNombre(medicamento.getNombre());
        nuevo.setPresentacion(medicamento.getPresentacion());
        nuevo.setDescripcion(medicamento.getDescripcion());
        // Eliminada referencia a fechaCreacionRegistro, fechaCompra y fechaVence
        return nuevo;
    }

    private void validarFormulario(MedicamentoRq medicamento) throws BadRequestException {
        if (medicamento.getNombre() == null || medicamento.getNombre().isEmpty()) {
            throw new BadRequestException("El nombre es obligatorio");
        }
        if (medicamento.getDescripcion() == null || medicamento.getDescripcion().isEmpty()) {
            throw new BadRequestException("El campo descripción es obligatorio");
        }
        if (medicamento.getPresentacion() == null || medicamento.getPresentacion().isEmpty()) {
            throw new BadRequestException("El campo presentación es obligatorio");
        }
        // Eliminadas validaciones de fechaCompra y fechaVence
    }
}

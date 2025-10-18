package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Medicamento;
import com.uniminuto.clinica.model.MedicamentoRq;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.repository.MedicamentoRepository;
import com.uniminuto.clinica.service.MedicamentoService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MedicamentoServiceImpl implements MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Override
    public List<Medicamento> listarMedicamentos() {
        return medicamentoRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Medicamento::getFechaCompra).reversed())
                .toList();
    }

    @Override
    public RespuestaRs guardarMedicamento(MedicamentoRq medicamento)
            throws BadRequestException {
        // Paso 1. Validar mi objeto de entrada que tengan todos los campos diligenciados
        this.validarFormulario(medicamento);

        // Paso 2. Validar que no exista un medicamento con el mismo nombre
        Optional<Medicamento> optMedicamento = this.medicamentoRepository
                .findByNombre(medicamento.getNombre());
        // Paso 3. Si existe un medicamento con el mismo nombre, retornar un mensaje de error
        if (optMedicamento.isPresent()) {
            throw new BadRequestException("Existe el medicamento. Intente de nuevo");
        }
        // Paso 4. Si no existe, crear el objeto Medicamento y guardarlo en la base de datos
        Medicamento nuevo = this.convertirAMedicamento(medicamento);
        this.medicamentoRepository.save(nuevo);

        // Paso 5. Retornar un mensaje de éxito
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

        // Si llega consultar el registro.
        Optional<Medicamento> optMedicamento = this.medicamentoRepository
                .findById(medicamento.getId());

        // Si el registro no existe, retorno error
        if (!optMedicamento.isPresent()) {
            throw new BadRequestException("No se puede actualizar el medicamento porque no existe.");
        }

        // Si existe modifico los campos a actualizar y guardo los cambios.
        Medicamento medicamentoActual = optMedicamento.get();
        // Validar si el nombre cambio con referencia al objeto en BD
        if (!medicamentoActual.getNombre().toLowerCase()
                .equals(medicamento.getNombre().toLowerCase())) {
            Optional<Medicamento> optBuscar = this.medicamentoRepository
                    .findByNombre(medicamento.getNombre());
            // Paso 3. Si existe un medicamento con el mismo nombre, retornar un mensaje de error
            if (optBuscar.isPresent()) {
                throw new BadRequestException("Existe el medicamento. Intente de nuevo");
            }
        }

        medicamentoActual.setNombre(medicamento.getNombre() == null? medicamentoActual.getNombre() : medicamento.getNombre());
        medicamentoActual.setPresentacion(medicamento.getPresentacion() == null? medicamentoActual.getPresentacion() : medicamento.getPresentacion());
        medicamentoActual.setDescripcion(medicamento.getDescripcion() == null? medicamentoActual.getDescripcion() : medicamento.getDescripcion());
        medicamentoActual.setFechaCompra(medicamento.getFechaCompra() == null? medicamentoActual.getFechaCompra() : medicamento.getFechaCompra());
        medicamentoActual.setFechaVence(medicamento.getFechaVence()== null? medicamentoActual.getFechaVence() : medicamento.getFechaVence());
        medicamentoActual.setFechaModificacionRegistro(LocalDateTime.now());

        this.medicamentoRepository.save(medicamentoActual);

        // Paso N. Retorno mensaje de éxito
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
        nuevo.setFechaCompra(medicamento.getFechaCompra());
        nuevo.setFechaVence(medicamento.getFechaVence());
        nuevo.setFechaCreacionRegistro(LocalDateTime.now());
        return nuevo;
    }

    private void validarFormulario(MedicamentoRq medicamento) throws BadRequestException {
        if (medicamento.getNombre() == null || medicamento.getNombre().isEmpty()) {
            throw new BadRequestException("El nombre es obligatorio");
        }
        if (medicamento.getDescripcion() == null || medicamento.getDescripcion().isEmpty()) {
            throw new BadRequestException("El campo descripcion es obligatorio");
        }
        if (medicamento.getPresentacion() == null || medicamento.getPresentacion().isEmpty()) {
            throw new BadRequestException("El campo presentacion es obligatorio");
        }
        if (medicamento.getFechaCompra() == null) {
            throw new BadRequestException("El campo fecha de compra es obligatorio");
        }
        if (medicamento.getFechaVence() == null) {
            throw new BadRequestException("El campo fecha de vencimiento es obligatorio");
        }
    }
}

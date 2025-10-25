package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.PacienteApi;
import com.uniminuto.clinica.model.PacienteRq;
import com.uniminuto.clinica.model.PacienteRs;
import com.uniminuto.clinica.service.PacienteService;
import java.util.List;
import java.util.Optional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PacienteApiController implements PacienteApi {

    @Autowired
    private PacienteService servicio;

    @Override
    public List<PacienteRs> obtenerTodos() {
        return this.servicio.obtenerTodos();
    }

    @Override
    public Optional<PacienteRs> buscarPorDocumento(String documento) {
        return this.servicio.buscarPorDocumento(documento);
    }

    @Override
    public PacienteRs guardar(PacienteRq paciente) throws BadRequestException {
        return this.servicio.guardar(paciente);
    }

    @Override
    public PacienteRs actualizar(Long id, PacienteRq paciente) throws BadRequestException {
        return this.servicio.actualizar(id, paciente);
    }

    @Override
    public void eliminar(Long id) {
        this.servicio.eliminar(id);
    }

    @Override
    public List<PacienteRs> obtenerPacientesOrdenadosPorFechaNacimiento() {
        return this.servicio.obtenerPacientesOrdenadosPorFechaNacimiento();
    }
}
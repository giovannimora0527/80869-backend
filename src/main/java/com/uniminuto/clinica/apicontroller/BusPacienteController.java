package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.BusPacienteApi;
import com.uniminuto.clinica.entity.Paciente;
import com.uniminuto.clinica.service.BusPacienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
public class BusPacienteController implements BusPacienteApi {

    private final BusPacienteService busPacienteService;

    public BusPacienteController(BusPacienteService busPacienteService) {
        this.busPacienteService = busPacienteService;
    }

    @Override
    public ResponseEntity<Paciente> buscarPorDocumento(String documento) {
        // Validación 1: El documento está vacío o es nulo
        if (documento == null || documento.trim().isEmpty()) {
            return new ResponseEntity("Debe ingresar un numero de documento valido.", HttpStatus.BAD_REQUEST);
        }

        // Validación 2: El documento contiene caracteres no numéricos
        if (!documento.matches("\\d+")) {
            return new ResponseEntity("El numero de documento no puede contener caracteres especiales o letras.", HttpStatus.BAD_REQUEST);
        }

        // Se llama al servicio que retorna el Optional<Paciente>
        Optional<Paciente> paciente = busPacienteService.buscarPorDocumento(documento);

        // Validación 3: El paciente no se encuentra en la base de datos
        if (paciente.isEmpty()) {
            String mensaje = "El paciente con numero " + documento + " no se encuentra en la base de datos.";
            return new ResponseEntity(mensaje, HttpStatus.NOT_FOUND);
        }

        // Si todo es valido y el paciente se encuentra
        return new ResponseEntity<>(paciente.get(), HttpStatus.OK);
    }
}
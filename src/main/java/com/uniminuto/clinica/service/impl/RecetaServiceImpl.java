package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Cita;
import com.uniminuto.clinica.entity.Medicamento;
import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.model.RecetaRq;
import com.uniminuto.clinica.repository.CitaRepository;
import com.uniminuto.clinica.repository.MedicamentoRepository;
import com.uniminuto.clinica.repository.MedicoRepository;
import com.uniminuto.clinica.repository.RecetaRepository;
import com.uniminuto.clinica.service.RecetaService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz {@link RecetaService}.
 * Proporciona la lógica de negocio para el manejo de recetas médicas,
 * incluyendo el almacenamiento y la consulta de todas las recetas registradas.
 */
@Service
public class RecetaServiceImpl implements RecetaService {
    @Autowired
    RecetaRepository recetaRepository;
    @Autowired
    CitaRepository citaRepository;
    @Autowired
    MedicamentoRepository medicamentoRepository;

    /**
     * Guarda una nueva receta médica en la base de datos.
     *
     * @param recetaRq Objeto {@link RecetaRq} que contiene los datos a almacenar.
     * @return Mensaje indicando si la operación fue exitosa.
     * @throws BadRequestException si ocurre un error al guardar la receta.
     */
    @Override
    public String guardarReceta(RecetaRq recetaRq) throws BadRequestException {
        Receta receta = new Receta();
        if(recetaRq.getCitaId() == null){
            throw new BadRequestException("No se pudo guardar la receta correctamente: debe ingresar una cita. ");
        }if(recetaRq.getMedicamentoId() == null){
            throw new BadRequestException("No se pudo guardar la receta correctamente: debe ingresar un medicamento. ");
        }
        Optional<Cita> citaOpt = this.citaRepository.findById(recetaRq.getCitaId());
        if(!citaOpt.isPresent()){
            throw new BadRequestException("No se pudo guardar la receta correctamente: La cita relacionada no existe.");
        }else{
            receta.setCita(citaOpt.get());
        }
        Optional<Medicamento> medicamentoOpt = this.medicamentoRepository.findById(recetaRq.getMedicamentoId());
        if(!medicamentoOpt.isPresent()){
            throw new BadRequestException("No se pudo guardar la receta correctamente: El medicamento relacionado no existe.");
        }else{
            receta.setMedicamento(medicamentoOpt.get());
        }
        receta.setDosis(recetaRq.getDosis());
        receta.setIndicaciones(recetaRq.getIndicaciones());
        receta.setFechaRegistro(recetaRq.getFechaRegistro());
        this.recetaRepository.save(receta);

        return "Se guardó la receta correctamente";
    }

    /**
     * Retorna una lista con todas las recetas médicas registradas en la base de datos.
     *
     * @return Lista de objetos {@link Receta}.
     */
    @Override
    public List<Receta> listarRecetas() {
        return this.recetaRepository.findAll();
    }
}

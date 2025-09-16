package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Receta;
import com.uniminuto.clinica.repository.RecetaRepository;
import com.uniminuto.clinica.service.RecetaService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación de la interfaz {@link RecetaService}.
 * Proporciona la lógica de negocio para el manejo de recetas médicas,
 * incluyendo el almacenamiento y la consulta de todas las recetas registradas.
 */
@Service
public class RecetaServiceImpl implements RecetaService {
    @Autowired
    RecetaRepository recetaRepository;

    /**
     * Guarda una nueva receta médica en la base de datos.
     *
     * @param receta Objeto {@link Receta} que contiene los datos a almacenar.
     * @return Mensaje indicando si la operación fue exitosa.
     * @throws BadRequestException si ocurre un error al guardar la receta.
     */
    @Override
    public String guardarReceta(Receta receta) throws BadRequestException {
        try{
            this.recetaRepository.save(receta);
        }catch (Exception e){
            throw new BadRequestException("No se pudo guardar la receta correctamente");
        }
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

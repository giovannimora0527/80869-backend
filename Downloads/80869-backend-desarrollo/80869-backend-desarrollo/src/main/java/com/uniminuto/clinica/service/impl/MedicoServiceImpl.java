package com.uniminuto.clinica.service.impl;

import com.uniminuto.clinica.entity.Especializacion;
import com.uniminuto.clinica.entity.Medico;
import com.uniminuto.clinica.repository.EspecialidadRepository;
import com.uniminuto.clinica.repository.MedicoRepository;
import com.uniminuto.clinica.service.MedicoService;
import java.util.List;
import java.util.Optional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author lmora
 */
@Service
public class MedicoServiceImpl implements MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Override
    public List<Medico> listarMedicos() {
        return this.medicoRepository.findAll();
    }

    @Override
    public List<Medico> listarMedicosPorEspecialidad(String codEspecialidad)
            throws BadRequestException {
        // 1. Consulto si la especialidad existe por codigo
        Optional<Especializacion> optEspecializacion = this.especialidadRepository
                .findByCodigoEspecializacion(codEspecialidad);
        // SI no esta o no existe la especializacion Lanzo Error por codigo
        // No existe
        if (!optEspecializacion.isPresent()) {
           throw new BadRequestException("No existe el codigo de la especializacion");
        }
        
        // Obtengo el objeto especializacion con el codigo ingresado
        Especializacion esp = optEspecializacion.get();  
        // Devuelvo la lista de resultados con la especializacion.
        return this.medicoRepository.findByEspecializacion(esp);
    }

    @Override
    public Medico buscarPorCC(String documento) throws BadRequestException {
        // Paso 1. Crear un metodo en el repo q busque un medico por cc CHECK
        // Paso 2. Verificar que el medico existe por documento
        // Paso 3. Sino esta arrojar error.
        // Paso 4. Esta el medico ... devolverlo :)
        
        // PASO 2.
        Optional<Medico> optMedico = this.medicoRepository
                .findByNumeroDocumento(documento);
        // Paso 3. 
        if (!optMedico.isPresent()) {
            throw new BadRequestException("El medico no se encuentra.");
        }
        
        // Paso 4. Devolver el resultado
        return optMedico.get();
    }

}

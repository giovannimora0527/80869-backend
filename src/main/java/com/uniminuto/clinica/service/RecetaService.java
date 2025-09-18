
package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Receta;
import java.util.List;


public interface RecetaService {
    
   Receta guardarReceta (Receta receta);
   List<Receta> listarRecetas();
   List<Receta> listarRecetasFechaCreacion ();
   
}

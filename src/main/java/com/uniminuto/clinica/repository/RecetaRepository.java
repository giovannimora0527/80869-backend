
package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.Receta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecetaRepository extends JpaRepository<Receta, Long>{
    
    // Aqui utilizamos un metodo publico el cual nos listara
    //las recetas por orden de creacion en este caso Descendente
    
    public List<Receta> findAllByOrderByFechaCreacionRegistroDesc();

}
    
    
    

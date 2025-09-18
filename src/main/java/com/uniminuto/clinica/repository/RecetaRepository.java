
package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.Receta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecetaRepository extends JpaRepository<Receta, Long>{
    
    
    public List<Receta> findAllByOrderByFechaCreacionRegistroDesc();

}
    
    
    

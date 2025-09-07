package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lmora
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    List<Usuario> findByRol(String rol);
    
    Optional<Usuario> findByUsername(String username);
    
    Optional<Usuario> findByNumerodocumento(String numerodocumento);
    
    List<Usuario> findByActivo(boolean activo);
    
}

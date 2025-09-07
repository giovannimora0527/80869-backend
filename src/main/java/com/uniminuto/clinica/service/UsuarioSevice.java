package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Usuario;
import java.util.List;
import org.apache.coyote.BadRequestException;

/**
 *
 * @author lmora
 */
public interface UsuarioSevice {
    List<Usuario> encontrarTodosLosUsuarios();
    
    List<Usuario> buscarPorRol(String rol);
    
    Usuario buscarPorNombre(String username) throws BadRequestException;
    
    Usuario buscarPorNumeroDocumento(String numeroDocumento) throws BadRequestException;

    List<Usuario> encontrarPorActivo(boolean activo);
    
}

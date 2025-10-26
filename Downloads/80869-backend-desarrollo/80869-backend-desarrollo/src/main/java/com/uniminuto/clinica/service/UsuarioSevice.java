package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Usuario;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.model.UsuarioRq;
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
    
    List<Usuario> encontrarPorActivo(boolean activo);
    
    RespuestaRs guardarUsuario(UsuarioRq usuarioNuevo) throws BadRequestException;
    
    
    
}

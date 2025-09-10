package com.clinica;

import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PacienteRepository pacienteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
    }

    /**
     * Busca un usuario asociado al paciente que tenga el numero de documento dado.
     * Lógica: busca paciente por numero_documento y si existe y tiene usuario_id, trae el usuario.
     */
    public Optional<Usuario> buscarUsuarioPorNumeroDocumentoPaciente(String numeroDocumento) {
        return pacienteRepository.findByNumeroDocumento(numeroDocumento)
                .flatMap(p -> {
                    Integer usuarioId = p.getUsuarioId();
                    if (usuarioId == null) return Optional.empty();
                    return usuarioRepository.findById(usuarioId.longValue());
                });
    }
}

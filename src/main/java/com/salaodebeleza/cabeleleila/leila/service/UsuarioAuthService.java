package com.salaodebeleza.cabeleleila.leila.service;

import com.salaodebeleza.cabeleleila.leila.model.Perfil;
import com.salaodebeleza.cabeleleila.leila.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioAuthService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioAuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean isAdmin(String usuarioId) {
        if (usuarioId == null || usuarioId.isBlank()) {
            return false;
        }
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> usuario.getPerfil() == Perfil.ADMIN)
                .orElse(false);
    }
}

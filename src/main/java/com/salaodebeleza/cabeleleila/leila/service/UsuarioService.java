package com.salaodebeleza.cabeleleila.leila.service;

import com.salaodebeleza.cabeleleila.leila.model.Perfil;
import com.salaodebeleza.cabeleleila.leila.model.Usuario;
import com.salaodebeleza.cabeleleila.leila.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioAuthService usuarioAuthService;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioAuthService usuarioAuthService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioAuthService = usuarioAuthService;
    }

    public Usuario cadastrar(Usuario usuario, String requisitanteId) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado: " + usuario.getEmail());
        }
        if (!usuarioAuthService.isAdmin(requisitanteId)) {
            usuario.setPerfil(Perfil.CLIENTE);
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(String id) {
        return buscarOuLancarExcecao(id);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario atualizar(String id, Usuario dadosAtualizados, String requisitanteId) {
        Optional<Usuario> usuarioComEmail = usuarioRepository.findByEmail(dadosAtualizados.getEmail());
        if (usuarioComEmail.isPresent() && !usuarioComEmail.get().getId().equals(id)) {
            throw new IllegalArgumentException("Email já cadastrado: " + dadosAtualizados.getEmail());
        }
        Usuario usuarioExistente = buscarOuLancarExcecao(id);
        usuarioExistente.setNome(dadosAtualizados.getNome());
        usuarioExistente.setEmail(dadosAtualizados.getEmail());
        usuarioExistente.setSenha(dadosAtualizados.getSenha());
        usuarioExistente.setTelefone(dadosAtualizados.getTelefone());
        if (usuarioAuthService.isAdmin(requisitanteId)) {
            usuarioExistente.setPerfil(dadosAtualizados.getPerfil());
        }
        return usuarioRepository.save(usuarioExistente);
    }

    public void deletar(String id) {
        Usuario usuario = buscarOuLancarExcecao(id);
        usuarioRepository.delete(usuario);
    }

    private Usuario buscarOuLancarExcecao(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
    }
}

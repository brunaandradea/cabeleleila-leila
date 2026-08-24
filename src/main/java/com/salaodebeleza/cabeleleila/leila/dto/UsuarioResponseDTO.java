package com.salaodebeleza.cabeleleila.leila.dto;

import com.salaodebeleza.cabeleleila.leila.model.Perfil;
import com.salaodebeleza.cabeleleila.leila.model.Usuario;
import lombok.Getter;

@Getter
public class UsuarioResponseDTO {
    private final String id;
    private final String nome;
    private final String email;
    private final String telefone;
    private final Perfil perfil;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.telefone = usuario.getTelefone();
        this.perfil = usuario.getPerfil();
    }
}
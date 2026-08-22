package com.salaodebeleza.cabeleleila.leila.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    private String senha;
    private String telefone;

    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    public Usuario() {
    }
}

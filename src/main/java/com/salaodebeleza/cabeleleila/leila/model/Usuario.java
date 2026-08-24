package com.salaodebeleza.cabeleleila.leila.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String nome;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    private String senha;
    private String telefone;

    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    public Usuario() {
    }
}

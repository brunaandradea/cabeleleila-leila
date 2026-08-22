package com.salaodebeleza.cabeleleila.leila.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "servico")
@Getter
@Setter
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nome;
    private String descricao;
    private BigDecimal precoAtual;

    private int duracaoMinutos;
    private boolean ativo;

    public Servico() {

    }
}

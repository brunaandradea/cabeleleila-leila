package com.salaodebeleza.cabeleleila.leila.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "item_agendamento")
@Getter
@Setter
public class ItemAgendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @Column(name = "preco_praticado", nullable = false)
    private BigDecimal precoPraticado;

    @Enumerated(EnumType.STRING)
    private StatusItem status;
}

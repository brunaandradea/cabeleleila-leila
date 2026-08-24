package com.salaodebeleza.cabeleleila.leila.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class AgendamentoRequestDTO {
    private String usuarioId;
    private LocalDateTime dataHora;
    private List<String> servicoIds;
    private String observacao;
}
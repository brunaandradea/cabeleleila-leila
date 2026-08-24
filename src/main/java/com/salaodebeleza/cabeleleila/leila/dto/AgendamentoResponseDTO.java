package com.salaodebeleza.cabeleleila.leila.dto;

import com.salaodebeleza.cabeleleila.leila.model.Agendamento;
import com.salaodebeleza.cabeleleila.leila.model.StatusAgendamento;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AgendamentoResponseDTO {
    private final String id;
    private final String usuarioId;
    private final LocalDateTime dataHora;
    private final StatusAgendamento status;
    private final String observacao;

    public AgendamentoResponseDTO(Agendamento agendamento) {
        this.id = agendamento.getId();
        this.usuarioId = agendamento.getUsuario().getId();
        this.dataHora = agendamento.getDataHora();
        this.status = agendamento.getStatus();
        this.observacao = agendamento.getObservacao();

    }
}

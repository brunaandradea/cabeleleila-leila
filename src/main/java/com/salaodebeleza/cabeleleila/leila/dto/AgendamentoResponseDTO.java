package com.salaodebeleza.cabeleleila.leila.dto;

import com.salaodebeleza.cabeleleila.leila.model.Agendamento;
import com.salaodebeleza.cabeleleila.leila.model.StatusAgendamento;
import com.salaodebeleza.cabeleleila.leila.model.Usuario;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AgendamentoResponseDTO {
    private final String id;
    private final String usuarioId;
    private final String nomeCliente;
    private final LocalDateTime dataHora;
    private final StatusAgendamento status;
    private final String observacao;

    public AgendamentoResponseDTO(Agendamento agendamento) {
        Usuario usuario = agendamento.getUsuario();
        this.id = agendamento.getId();
        this.usuarioId = usuario != null ? usuario.getId() : null;
        this.nomeCliente = usuario != null ? usuario.getNome() : "Cliente removido";
        this.dataHora = agendamento.getDataHora();
        this.status = agendamento.getStatus();
        this.observacao = agendamento.getObservacao();
    }
}
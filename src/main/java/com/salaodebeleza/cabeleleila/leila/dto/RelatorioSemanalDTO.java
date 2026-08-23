package com.salaodebeleza.cabeleleila.leila.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class RelatorioSemanalDTO {
    private long totalAgendamentos;
    private long totalConcluidos;
    private long totalCancelados;
    private BigDecimal faturamentoTotal;

}
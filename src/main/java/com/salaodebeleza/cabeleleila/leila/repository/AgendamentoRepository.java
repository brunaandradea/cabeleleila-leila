package com.salaodebeleza.cabeleleila.leila.repository;

import com.salaodebeleza.cabeleleila.leila.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, String> {

    List<Agendamento> findByUsuario_IdAndDataHoraBetween(String usuarioId, LocalDateTime inicio, LocalDateTime fim);
    List<Agendamento> findByUsuario_IdOrderByDataHoraDesc(String usuarioId);
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
}

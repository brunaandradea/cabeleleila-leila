package com.salaodebeleza.cabeleleila.leila.repository;

import com.salaodebeleza.cabeleleila.leila.model.ItemAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemAgendamentoRepository extends JpaRepository<ItemAgendamento, String> {

    List<ItemAgendamento> findByAgendamento_Id(String agendamentoId);

}

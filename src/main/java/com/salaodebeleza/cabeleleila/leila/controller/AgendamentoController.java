package com.salaodebeleza.cabeleleila.leila.controller;

import com.salaodebeleza.cabeleleila.leila.dto.AgendamentoRequestDTO;
import com.salaodebeleza.cabeleleila.leila.dto.AgendamentoResponseDTO;
import com.salaodebeleza.cabeleleila.leila.dto.RelatorioSemanalDTO;
import com.salaodebeleza.cabeleleila.leila.model.Agendamento;
import com.salaodebeleza.cabeleleila.leila.model.ItemAgendamento;
import com.salaodebeleza.cabeleleila.leila.model.StatusAgendamento;
import com.salaodebeleza.cabeleleila.leila.service.AgendamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public List<AgendamentoResponseDTO> listar() {
        return agendamentoService.listarTodos().stream()
                .map(AgendamentoResponseDTO::new)
                .toList();
    }

    @GetMapping("/{id}")
    public AgendamentoResponseDTO buscar(@PathVariable String id) {
        return new AgendamentoResponseDTO(agendamentoService.buscarPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgendamentoResponseDTO cadastrar(@RequestBody AgendamentoRequestDTO dto) {
        Agendamento agendamento = agendamentoService.cadastrar(dto.getUsuarioId(), dto.getDataHora(), dto.getServicoIds(), dto.getObservacao());
        return new AgendamentoResponseDTO(agendamento);
    }

    @PatchMapping("/{id}/data-hora")
    public AgendamentoResponseDTO alterarDataHora(@PathVariable String id, @RequestBody AgendamentoRequestDTO dto) {
        Agendamento agendamento = agendamentoService.alterarDataHora(id, dto.getDataHora());
        return new AgendamentoResponseDTO(agendamento);
    }

    @PatchMapping("/{id}/confirmar")
    public AgendamentoResponseDTO confirmarAgendamento(@PathVariable String id) {
        Agendamento agendamento = agendamentoService.confirmarAgendamento(id);
        return new AgendamentoResponseDTO(agendamento);
    }

    @PatchMapping("/{id}/status")
    public AgendamentoResponseDTO atualizarStatus(@PathVariable String id, @RequestParam StatusAgendamento status) {
        return new AgendamentoResponseDTO(agendamentoService.atualizarStatus(id, status));
    }

    @GetMapping("/sugestao-data")
    public Optional<LocalDateTime> sugerirDataAgendamento(@RequestParam String usuarioId, @RequestParam LocalDateTime dataDesejada) {
        return agendamentoService.sugerirDataAgendamento(usuarioId, dataDesejada);
    }

    @GetMapping("/usuario/{usuarioId}/historico")
    public List<AgendamentoResponseDTO> historico(@PathVariable String usuarioId) {
        return agendamentoService.historicoPorUsuario(usuarioId).stream()
                .map(AgendamentoResponseDTO::new)
                .toList();
    }

    @GetMapping("/{id}/itens")
    public List<ItemAgendamento> detalhesDoAgendamento(@PathVariable String id) {
        return agendamentoService.detalhesDoAgendamento(id);
    }

    @PatchMapping("/itens/{itemId}/concluir")
    public ItemAgendamento concluirItem(@PathVariable String itemId) {
        return agendamentoService.concluirItem(itemId);
    }

    @PatchMapping("/itens/{itemId}/cancelar")
    public ItemAgendamento cancelarItem(@PathVariable String itemId) {
        return agendamentoService.cancelarItem(itemId);
    }

    @GetMapping("/relatorio-semanal")
    public RelatorioSemanalDTO relatorioSemanal() {
        return agendamentoService.gerarRelatorioSemanal();
    }
}

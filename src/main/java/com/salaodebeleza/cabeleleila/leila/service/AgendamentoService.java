package com.salaodebeleza.cabeleleila.leila.service;

import com.salaodebeleza.cabeleleila.leila.dto.RelatorioSemanalDTO;
import com.salaodebeleza.cabeleleila.leila.model.*;
import com.salaodebeleza.cabeleleila.leila.repository.AgendamentoRepository;
import com.salaodebeleza.cabeleleila.leila.repository.ItemAgendamentoRepository;
import com.salaodebeleza.cabeleleila.leila.repository.ServicoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioService usuarioService;
    private final ServicoRepository servicoRepository;
    private final ItemAgendamentoRepository itemAgendamentoRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository,
                              UsuarioService usuarioService,
                              ServicoRepository servicoRepository,
                              ItemAgendamentoRepository itemAgendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioService = usuarioService;
        this.servicoRepository = servicoRepository;
        this.itemAgendamentoRepository = itemAgendamentoRepository;
    }

    private static final LocalTime HORARIO_ABERTURA = LocalTime.of(8, 0);
    private static final LocalTime HORARIO_FECHAMENTO = LocalTime.of(18, 0);

    private void validarHorarioComercial(LocalDateTime dataHora) {
        DayOfWeek diaDaSemana = dataHora.getDayOfWeek();
        if (diaDaSemana == DayOfWeek.SUNDAY || diaDaSemana == DayOfWeek.MONDAY) {
            throw new IllegalArgumentException("Agendamentos só podem ser feitos de terça a sábado.");
        }

        LocalTime horario = dataHora.toLocalTime();
        if (horario.isBefore(HORARIO_ABERTURA) || !horario.isBefore(HORARIO_FECHAMENTO)) {
            throw new IllegalArgumentException("Agendamentos só podem ser feitos entre 08:00 e 18:00.");
        }
    }

    public Agendamento cadastrar(String usuarioId, LocalDateTime dataHora, List<String> servicoIds, String observacao) {
        validarHorarioComercial(dataHora);
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        List<Servico> servicosEscolhidos = new ArrayList<>();
        for (String servicoId : servicoIds) {
            Servico servico = servicoRepository.findById(servicoId)
                    .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado: " + servicoId));

            if (!servico.isAtivo()) {
                throw new IllegalArgumentException("O serviço " + servico.getNome() +
                        " não está ativo e não pode ser agendado.");
            }
            servicosEscolhidos.add(servico);
        }

        Agendamento agendamento = new Agendamento();
        agendamento.setUsuario(usuario);
        agendamento.setDataHora(dataHora);
        agendamento.setStatus(StatusAgendamento.PENDENTE);
        agendamento.setObservacao(observacao);
        agendamento = agendamentoRepository.save(agendamento);

        for (Servico servico : servicosEscolhidos) {
            ItemAgendamento item = new ItemAgendamento();
            item.setAgendamento(agendamento);
            item.setServico(servico);
            item.setPrecoPraticado(servico.getPrecoAtual());
            item.setStatus(StatusItem.PENDENTE);
            itemAgendamentoRepository.save(item);
        }

        return agendamento;
    }

    public Agendamento alterarDataHora(String agendamentoId, LocalDateTime novaDataHora) {
        validarHorarioComercial(novaDataHora);
        Agendamento agendamento = buscarOuLancarExcecao(agendamentoId);

        Duration diferenca = Duration.between(LocalDateTime.now(), agendamento.getDataHora());
        if (diferenca.toHours() < 48) {
            throw new IllegalStateException("Alterações a menos de 2 dias do agendamento devem ser feitas por telefone.");
        }

        agendamento.setDataHora(novaDataHora);
        return agendamentoRepository.save(agendamento);
    }

    public Optional<LocalDateTime> sugerirDataAgendamento(String usuarioId, LocalDateTime dataDesejada) {
        LocalDate inicioSemana = dataDesejada.toLocalDate().with(DayOfWeek.MONDAY);
        LocalDate fimSemana = inicioSemana.plusDays(6);

        LocalDateTime inicio = inicioSemana.atStartOfDay();
        LocalDateTime fim = fimSemana.atTime(LocalTime.MAX);

        List<Agendamento> agendamentos = agendamentoRepository.findByUsuario_IdAndDataHoraBetween(usuarioId, inicio, fim);

        return agendamentos.stream()
                .filter(a -> a.getStatus() != StatusAgendamento.CANCELADO)
                .map(Agendamento::getDataHora)
                .min(LocalDateTime::compareTo);
    }

    public Agendamento buscarPorId(String id) {
        return buscarOuLancarExcecao(id);
    }

    public Agendamento confirmarAgendamento(String agendamentoId) {
        Agendamento agendamento = buscarOuLancarExcecao(agendamentoId);
        if (agendamento.getStatus() != StatusAgendamento.PENDENTE) {
            throw new IllegalStateException("Apenas agendamentos pendentes podem ser confirmados.");
        }
        agendamento.setStatus(StatusAgendamento.CONFIRMADO);
        return agendamentoRepository.save(agendamento);
    }

    public Agendamento atualizarStatus(String agendamentoId, StatusAgendamento novoStatus) {
        Agendamento agendamento = buscarOuLancarExcecao(agendamentoId);

        if (novoStatus == null) {
            throw new IllegalArgumentException("Status obrigatório.");
        }

        StatusAgendamento atual = agendamento.getStatus();
        if (atual == novoStatus) {
            return agendamento;
        }

        boolean transicaoValida =
                (atual == StatusAgendamento.PENDENTE && (novoStatus == StatusAgendamento.CONFIRMADO || novoStatus == StatusAgendamento.CANCELADO))
                        || (atual == StatusAgendamento.CONFIRMADO && (novoStatus == StatusAgendamento.CONCLUIDO || novoStatus == StatusAgendamento.CANCELADO));

        if (!transicaoValida) {
            throw new IllegalStateException("Transição de status inválida: " + atual + " -> " + novoStatus);
        }

        agendamento.setStatus(novoStatus);
        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    public List<Agendamento> historicoPorUsuario(String usuarioId) {
        return agendamentoRepository.findByUsuario_IdOrderByDataHoraDesc(usuarioId);
    }

    public List<ItemAgendamento> detalhesDoAgendamento(String agendamentoId) {
        buscarOuLancarExcecao(agendamentoId);
        return itemAgendamentoRepository.findByAgendamento_Id(agendamentoId);
    }

    public RelatorioSemanalDTO gerarRelatorioSemanal() {
        LocalDate inicioSemana = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate fimSemana = inicioSemana.plusDays(6);

        List<Agendamento> agendamentos = agendamentoRepository.findByDataHoraBetween(
                inicioSemana.atStartOfDay(), fimSemana.atTime(LocalTime.MAX));

        long totalAgendamentos = agendamentos.size();
        long totalConcluidos = 0;
        long totalCancelados = 0;
        BigDecimal faturamentoTotal = BigDecimal.ZERO;

        for (Agendamento agendamento : agendamentos) {
            if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
                totalConcluidos++;
                List<ItemAgendamento> itens = itemAgendamentoRepository.findByAgendamento_Id(agendamento.getId());
                for (ItemAgendamento item : itens) {
                    if (item.getStatus() == StatusItem.CONCLUIDO) {
                        faturamentoTotal = faturamentoTotal.add(item.getPrecoPraticado());
                    }
                }
            } else if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
                totalCancelados++;
            }
        }

        return new RelatorioSemanalDTO(totalAgendamentos, totalConcluidos, totalCancelados, faturamentoTotal);
    }

    public ItemAgendamento concluirItem(String itemId) {
        ItemAgendamento item = itemAgendamentoRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item de agendamento não encontrado: " + itemId));

        if (item.getStatus() == StatusItem.CANCELADO) {
            throw new IllegalStateException("Não é possível concluir um item cancelado.");
        }
        if (item.getStatus() == StatusItem.CONCLUIDO) {
            throw new IllegalStateException("O item já está concluído.");
        }

        item.setStatus(StatusItem.CONCLUIDO);
        return itemAgendamentoRepository.save(item);
    }

    public ItemAgendamento cancelarItem(String itemId) {
        ItemAgendamento item = itemAgendamentoRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item de agendamento não encontrado: " + itemId));

        if (item.getStatus() == StatusItem.CONCLUIDO) {
            throw new IllegalStateException("Não é possível cancelar um item já concluído.");
        }
        if (item.getStatus() == StatusItem.CANCELADO) {
            throw new IllegalStateException("O item já está cancelado.");
        }

        item.setStatus(StatusItem.CANCELADO);
        return itemAgendamentoRepository.save(item);
    }

    private Agendamento buscarOuLancarExcecao(String id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado: " + id));
    }
}

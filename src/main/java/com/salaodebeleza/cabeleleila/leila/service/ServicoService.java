package com.salaodebeleza.cabeleleila.leila.service;

import com.salaodebeleza.cabeleleila.leila.model.Servico;
import com.salaodebeleza.cabeleleila.leila.repository.ServicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public Servico cadastrar(Servico servico) {
        return servicoRepository.save(servico);
    }

    public Servico buscarPorId(String id) {
        return buscarOuLancarExcecao(id);
    }

    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    public Servico atualizar(String id, Servico dadosAtualizados) {
        Servico servico = buscarOuLancarExcecao(id);
        servico.setNome(dadosAtualizados.getNome());
        servico.setDescricao(dadosAtualizados.getDescricao());
        servico.setPrecoAtual(dadosAtualizados.getPrecoAtual());
        servico.setDuracaoMinutos(dadosAtualizados.getDuracaoMinutos());
        return servicoRepository.save(servico);
    }

    public Servico ativar(String id) {
        Servico servico = buscarOuLancarExcecao(id);
        servico.setAtivo(true);
        return servicoRepository.save(servico);
    }

    public Servico desativar(String id) {
        Servico servico = buscarOuLancarExcecao(id);
        servico.setAtivo(false);
        return servicoRepository.save(servico);
    }

    private Servico buscarOuLancarExcecao(String id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado: " + id));
    }
}

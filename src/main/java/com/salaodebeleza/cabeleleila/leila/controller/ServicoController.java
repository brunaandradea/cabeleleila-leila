package com.salaodebeleza.cabeleleila.leila.controller;

import com.salaodebeleza.cabeleleila.leila.model.Servico;
import com.salaodebeleza.cabeleleila.leila.service.ServicoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @GetMapping
    public List<Servico> listar() {
        return servicoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Servico buscar(@PathVariable String id) {
        return servicoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Servico cadastrar(@RequestBody Servico servico) {
        return servicoService.cadastrar(servico);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Servico atualizar(@PathVariable String id, @RequestBody Servico servico) {
        return servicoService.atualizar(id, servico);
    }

    @PatchMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.OK)
    public Servico ativar(@PathVariable String id) {
        return servicoService.ativar(id);
    }

    @PatchMapping("/{id}/desativar")
    @ResponseStatus(HttpStatus.OK)
    public Servico desativar(@PathVariable String id) {
        return servicoService.desativar(id);
    }
}


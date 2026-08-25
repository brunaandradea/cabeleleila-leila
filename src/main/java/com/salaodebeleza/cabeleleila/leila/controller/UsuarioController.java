package com.salaodebeleza.cabeleleila.leila.controller;

import com.salaodebeleza.cabeleleila.leila.dto.UsuarioResponseDTO;
import com.salaodebeleza.cabeleleila.leila.model.Usuario;
import com.salaodebeleza.cabeleleila.leila.security.AdminAccessInterceptor;
import com.salaodebeleza.cabeleleila.leila.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return usuarioService.listarTodos().stream()
                .map(UsuarioResponseDTO::new)
                .toList();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO buscar(@PathVariable String id) {
        return new UsuarioResponseDTO(usuarioService.buscarPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO cadastrar(@Valid @RequestBody Usuario usuario,
                                        @RequestHeader(value = AdminAccessInterceptor.HEADER_USUARIO_ID, required = false) String requisitanteId) {
        return new UsuarioResponseDTO(usuarioService.cadastrar(usuario, requisitanteId));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioResponseDTO atualizar(@PathVariable String id, @Valid @RequestBody Usuario usuario,
                                        @RequestHeader(value = AdminAccessInterceptor.HEADER_USUARIO_ID, required = false) String requisitanteId) {
        return new UsuarioResponseDTO(usuarioService.atualizar(id, usuario, requisitanteId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable String id) {
        usuarioService.deletar(id);
    }
}

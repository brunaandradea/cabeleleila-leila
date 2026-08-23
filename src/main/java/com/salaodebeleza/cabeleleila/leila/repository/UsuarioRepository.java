package com.salaodebeleza.cabeleleila.leila.repository;

import com.salaodebeleza.cabeleleila.leila.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);
}

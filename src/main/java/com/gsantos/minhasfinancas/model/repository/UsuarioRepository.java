package com.gsantos.minhasfinancas.model.repository;

import com.gsantos.minhasfinancas.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    //QUERY METHODS

    boolean existsByEmail(String email);
}

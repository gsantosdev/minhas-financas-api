package com.gsantos.minhasfinancas.service.impl;

import com.gsantos.minhasfinancas.model.entity.Usuario;
import com.gsantos.minhasfinancas.model.repository.UsuarioRepository;
import com.gsantos.minhasfinancas.service.UsuarioService;

public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }


    @Override
    public Usuario autenticar(String email, String senha) {

        return null;
    }

    @Override
    public Usuario salvarUsuario(Usuario usuario) {
        return null;
    }

    @Override
    public void validarEmail(String email) {

    }
}

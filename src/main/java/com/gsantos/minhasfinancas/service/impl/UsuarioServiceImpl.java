package com.gsantos.minhasfinancas.service.impl;

import com.gsantos.minhasfinancas.exception.ErroAutenticacao;
import com.gsantos.minhasfinancas.exception.RegraNegocioException;
import com.gsantos.minhasfinancas.model.entity.Usuario;
import com.gsantos.minhasfinancas.model.repository.UsuarioRepository;
import com.gsantos.minhasfinancas.service.UsuarioService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }


    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);

        if (!usuario.isPresent()) {
            throw new ErroAutenticacao("Usuario não encontrado para o email informado.");
        }

        if (!usuario.get().getSenha().equals(senha)) {
            throw new ErroAutenticacao("Senha inválida.");
        }

        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);

    }

    @Override
    public void validarEmail(String email) {

        if (repository.existsByEmail(email)) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
        }

    }
}

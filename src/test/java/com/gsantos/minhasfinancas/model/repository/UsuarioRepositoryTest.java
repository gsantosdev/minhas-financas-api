package com.gsantos.minhasfinancas.model.repository;


import com.gsantos.minhasfinancas.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveVerificarExistenciaDeEmail() {
        //cenário
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);

        //ação / execução
        boolean result = repository.existsByEmail("usuario@email.com");

        //verificacao
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
        //ação
        boolean result = repository.existsByEmail("usuario@email.com");

        //verificacao
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void devePersistirUmUsuarioNaBaseDeDados() {
        //cenário
        Usuario usuario = Usuario
                .builder()
                .nome("usuario")
                .email("usuario@email.com")
                .senha("senha")
                .build();

        Usuario usuarioSalvo = repository.save(usuario);

        assertThat(usuarioSalvo.getId()).isNotNull();
    }

    @Test
    public void deveBuscarUmUsuarioPorEmail() {
        //cenario
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);

        //verificacao
        Optional<Usuario> result = repository.findByEmail("usuario@email.com");
        assertThat(result.isPresent()).isTrue();

    }

    @Test
    public void deveRetornarVazioAoBuscarUsuarioPorEmail() {

        //verificacao
        Optional<Usuario> result = repository.findByEmail("usuario@email.com");
        assertThat(result.isPresent()).isFalse();

    }

    private Usuario criarUsuario() {
        return Usuario
                .builder()
                .nome("usuario")
                .email("usuario@email.com")
                .senha("senha")
                .build();
    }


}

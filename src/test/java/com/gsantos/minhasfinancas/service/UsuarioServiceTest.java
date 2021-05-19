package com.gsantos.minhasfinancas.service;


import com.gsantos.minhasfinancas.exception.ErroAutenticacao;
import com.gsantos.minhasfinancas.exception.RegraNegocioException;
import com.gsantos.minhasfinancas.model.entity.Usuario;
import com.gsantos.minhasfinancas.model.repository.UsuarioRepository;
import com.gsantos.minhasfinancas.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")

public class UsuarioServiceTest {


    private static final String EMAIL = "usuario@email.com";
    private static final String USUARIO = "usuario";
    private static final String SENHA = "senha";
    private static final Long ID = 1L;

    UsuarioService service;

    @MockBean
    UsuarioRepository repository;

    @BeforeEach
    public void setUp() {
        service = new UsuarioServiceImpl(repository);
    }


    @Test
    public void deveAutenticarUmUsuarioComSucesso() {
        //cenário
        Assertions.assertDoesNotThrow(() -> {
            Usuario usuario = Usuario.builder().nome(EMAIL).senha(SENHA).id(ID).build();
            Mockito.when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));

            //acao
            Usuario result = service.autenticar(EMAIL, SENHA);

            //verificacao
            assertThat(result).isNotNull();
        });
    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {

        //cenário
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        //acao
        Throwable exception = org.assertj.core.api.Assertions.catchThrowable(() -> {
            service.autenticar(EMAIL, SENHA);
        });
        //verificacao
        assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuario não encontrado para o email informado.");

    }

    @Test
    public void deveLancarErroQuandoSenhaNaoBater() {

        //cenário
        Usuario usuario = Usuario.builder().email(EMAIL).senha(SENHA).build();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
        //acao
        Throwable exception = org.assertj.core.api.Assertions.catchThrowable(() -> {
            service.autenticar(EMAIL, "123");
        });
        assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida.");

    }

    @Test
    public void deveValidarEmail() {
        Assertions.assertDoesNotThrow(() -> {

            //cenario
            Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

            UsuarioRepository usuarioRepositoryMock = Mockito.mock(UsuarioRepository.class);
            repository.deleteAll();

            //acao
            service.validarEmail(EMAIL);
        });
    }

    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
        Assertions.assertThrows(RegraNegocioException.class, () ->
        {
            //cenario
            Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

            //acao
            service.validarEmail(EMAIL);
        });

    }


}

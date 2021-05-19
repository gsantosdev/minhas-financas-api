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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")

public class UsuarioServiceTest {


    private static final String EMAIL = "usuario@email.com";
    private static final String NOME = "usuario";
    private static final String SENHA = "senha";
    private static final Long ID = 1L;

    @SpyBean
    UsuarioServiceImpl service;

    @MockBean
    UsuarioRepository repository;

    @Test
    public void deveSalvarUmUsuario() {

        Assertions.assertDoesNotThrow(() -> {
            //cenário
            Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
            Usuario usuario = Usuario.builder()
                    .id(ID)
                    .nome(NOME)
                    .email(EMAIL)
                    .senha(SENHA)
                    .build();
            Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
            //acao
            Usuario usuarioSalvo = service.salvarUsuario(new Usuario());

            //verificacao
            assertThat(usuarioSalvo).isNotNull();
            assertThat(usuarioSalvo.getId()).isEqualTo(ID);
            assertThat(usuarioSalvo.getNome()).isEqualTo(NOME);
            assertThat(usuarioSalvo.getEmail()).isEqualTo(EMAIL);
            assertThat(usuarioSalvo.getSenha()).isEqualTo(SENHA);
        });
    }

    @Test
    public void naoDeveSalvarUmUsuarioComEmailJaCadastrado(){

        Assertions.assertThrows(RegraNegocioException.class, () ->{
            //cenário
            Usuario usuario = Usuario.builder().email(EMAIL).build();
            Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(EMAIL);

            //acao
            service.salvarUsuario(usuario);

            //verificacao
            Mockito.verify(repository, Mockito.never()).save(usuario);

        });

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

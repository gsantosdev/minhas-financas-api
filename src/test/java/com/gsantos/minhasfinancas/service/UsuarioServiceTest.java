package com.gsantos.minhasfinancas.service;


import com.gsantos.minhasfinancas.exception.RegraNegocioException;
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


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")

public class UsuarioServiceTest {


    private static final String EMAIL = "usuario@email.com";
    private static final String USUARIO = "usuario";

    UsuarioService service;

    @MockBean
    UsuarioRepository repository;

    @BeforeEach
    public void setUp() {
        service = new UsuarioServiceImpl(repository);
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

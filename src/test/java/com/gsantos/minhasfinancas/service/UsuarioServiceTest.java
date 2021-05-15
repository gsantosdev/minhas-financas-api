package com.gsantos.minhasfinancas.service;


import com.gsantos.minhasfinancas.exception.RegraNegocioException;
import com.gsantos.minhasfinancas.model.entity.Usuario;
import com.gsantos.minhasfinancas.model.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioServiceTest {

    @Autowired
    UsuarioService service;

    @Autowired
    UsuarioRepository repository;

    @Test
    public void deveValidarEmail(){
        Assertions.assertDoesNotThrow(() ->{

            //cenario
            repository.deleteAll();

            //acao
            service.validarEmail("email@gmail.com");
        });
    }

    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){
        Assertions.assertThrows(RegraNegocioException.class, ()->
        {
            //cenario
            Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
            repository.save(usuario);

            //acao
            service.validarEmail("email@email.com");
        });

    }


}

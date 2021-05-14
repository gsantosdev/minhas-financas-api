package com.gsantos.minhasfinancas.model.repository;


import com.gsantos.minhasfinancas.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Test
    public void deveVerificarExistenciaDeEmail(){
        //cenário
        Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
        repository.save(usuario);

        //ação / execução
        boolean result = repository.existsByEmail("usuario@email.com");

        //verificacao
        Assertions.assertThat(result).isTrue();

    }

}

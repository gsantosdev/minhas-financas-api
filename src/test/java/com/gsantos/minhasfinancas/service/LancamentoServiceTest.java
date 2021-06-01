package com.gsantos.minhasfinancas.service;


import com.gsantos.minhasfinancas.model.entity.Lancamento;
import com.gsantos.minhasfinancas.model.enums.StatusLancamento;
import com.gsantos.minhasfinancas.model.repository.LancamentoRepository;
import com.gsantos.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.gsantos.minhasfinancas.service.impl.LancamentoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

    @SpyBean
    LancamentoServiceImpl service;

    @MockBean
    LancamentoRepository repository;


    @Test
    public void deveSalvarUmLancamento(){
        //cenário
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doNothing().when(service).validar(lancamentoASalvar);

        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1L);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);


        //execucao
        Lancamento lancamento = service.salvar(lancamentoASalvar);

        //verificação
        assertThat( lancamento.getId() ).isEqualTo(lancamentoSalvo.getId());
        assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
    }


    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao(){

    }


}

package com.gsantos.minhasfinancas.service;


import com.gsantos.minhasfinancas.exception.RegraNegocioException;
import com.gsantos.minhasfinancas.model.entity.Lancamento;
import com.gsantos.minhasfinancas.model.enums.StatusLancamento;
import com.gsantos.minhasfinancas.model.repository.LancamentoRepository;
import com.gsantos.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.gsantos.minhasfinancas.service.impl.LancamentoServiceImpl;
import org.assertj.core.api.Assertions;
import org.hibernate.criterion.Example;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

    @SpyBean
    LancamentoServiceImpl service;

    @MockBean
    LancamentoRepository repository;


    @Test
    public void deveSalvarUmLancamento() {
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
        assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
    }


    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
        //cenário
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);

        //execucao e verificacao
        Assertions.catchThrowableOfType(() -> service.salvar(lancamentoASalvar), RegraNegocioException.class);

        Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);

    }

    @Test
    public void deveAtualizarLancamento() {
        //cenário
        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1L);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);

        Mockito.doNothing().when(service).validar(lancamentoSalvo);

        Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

        //execucao
        service.atualizar(lancamentoSalvo);

        //verificação
        Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);

    }

    @Test
    public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
        //cenário
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();

        //execucao e verificacao
        Assertions.catchThrowableOfType(() -> service.atualizar(lancamentoASalvar), NullPointerException.class);

        Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
    }


    @Test
    public void deveDeletarUmLancamento() {
        //cenário
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1L);

        //execucao
        service.deletar(lancamento);

        //verificacao
        Mockito.verify(repository).delete(lancamento);
    }


    @Test
    public void deveLancarErroAoDeletarUmLancamentoAindaNaoSalvo() {
        //cenário
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();

        //execucao
        Assertions.catchThrowableOfType(() -> service.deletar(lancamento), NullPointerException.class);

        //verificacao
        Mockito.verify(repository, Mockito.never()).delete(lancamento);

    }

    @Test
    public void deveFiltrarLancamentos() {
        //cenário
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1L);

        List<Lancamento> lista = Arrays.asList(lancamento);
        Mockito.when(repository.findAll((Pageable) Mockito.any(Example.class))).thenReturn((Page<Lancamento>) lista);

        //execucao
        List<Lancamento> resultado = service.buscar(lancamento);

        //verificacoes
        Assertions
                .assertThat(resultado)
                .isNotEmpty()
                .hasSize(1)
                .contains(lancamento);


    }

    @Test
    public void deveAtualizarOStatusDeUmLancamento(){
        //cenário
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1L);
        lancamento.setStatus(StatusLancamento.PENDENTE);

        StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
        Mockito.doReturn(lancamento).when(service).atualizar(lancamento);

        //execucao
        service.atualizarStatus(lancamento, novoStatus);

        Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
        Mockito.verify(service).atualizar(lancamento);
    }


}

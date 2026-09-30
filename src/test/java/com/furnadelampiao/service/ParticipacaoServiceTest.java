package com.furnadelampiao.service;

import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.domain.Participacao;
import com.furnadelampiao.domain.Pessoa;
import com.furnadelampiao.enums.PapelParticipante;
import com.furnadelampiao.repository.ParticipacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipacaoServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private ParticipacaoRepository repository;

    private ParticipacaoService service;

    @BeforeEach
    void setUp() {
        lenient().when(entityManager.getTransaction()).thenReturn(transaction);
        service = new ParticipacaoService(entityManager, repository);
    }

    private Participacao participacaoValida() {
        Pessoa pessoa = Pessoa.builder().nome("Fulano").build();
        pessoa.setId(1L);
        Expedicao expedicao = Expedicao.builder().codigo("EXP-001").build();
        expedicao.setId(2L);

        return Participacao.builder()
                .pessoa(pessoa)
                .expedicao(expedicao)
                .papelParticipante(PapelParticipante.PESQUISADOR)
                .dataConfirmacao(LocalDate.now())
                .valorDiaria(new BigDecimal("100.00"))
                .qtdPrevistaDias(5)
                .presencaConfirmada(false)
                .build();
    }

    // ---------- cadastrar ----------

    @Test
    void cadastrar_devePersistir_quandoValida() {
        Participacao participacao = participacaoValida();

        service.cadastrar(participacao);

        verify(transaction).begin();
        verify(repository).salvar(participacao);
        verify(transaction).commit();
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoNula() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(null));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoPessoaAusente() {
        Participacao participacao = participacaoValida();
        participacao.setPessoa(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(participacao));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoExpedicaoAusente() {
        Participacao participacao = participacaoValida();
        participacao.setExpedicao(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(participacao));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoDataConfirmacaoAusente() {
        Participacao participacao = participacaoValida();
        participacao.setDataConfirmacao(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(participacao));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoPapelParticipanteAusente() {
        Participacao participacao = participacaoValida();
        participacao.setPapelParticipante(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(participacao));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoValorDiariaNegativo() {
        Participacao participacao = participacaoValida();
        participacao.setValorDiaria(new BigDecimal("-1"));

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(participacao));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoQtdPrevistaDiasNegativa() {
        Participacao participacao = participacaoValida();
        participacao.setQtdPrevistaDias(-1);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(participacao));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveDefinirPresencaConfirmadaComoFalse_quandoNula() {
        Participacao participacao = participacaoValida();
        participacao.setPresencaConfirmada(null);

        service.cadastrar(participacao);

        assertFalse(participacao.getPresencaConfirmada());
    }

    // ---------- atualizar ----------

    @Test
    void atualizar_deveAtualizar_quandoValidaComId() {
        Participacao participacao = participacaoValida();
        participacao.setId(1L);

        service.atualizar(participacao);

        verify(repository).atualizar(participacao);
    }

    @Test
    void atualizar_deveLancarExcecao_quandoIdNulo() {
        Participacao participacao = participacaoValida();

        assertThrows(IllegalArgumentException.class, () -> service.atualizar(participacao));
        verify(repository, never()).atualizar(any());
    }

    // ---------- remover ----------

    @Test
    void remover_deveRemover_quandoParticipacaoExiste() {
        Participacao participacao = participacaoValida();
        participacao.setId(1L);
        when(entityManager.find(Participacao.class, 1L)).thenReturn(participacao);

        service.remover(1L);

        verify(transaction).begin();
        verify(entityManager).remove(participacao);
        verify(transaction).commit();
    }

    @Test
    void remover_naoDeveRemover_quandoParticipacaoNaoEncontrada() {
        when(entityManager.find(Participacao.class, 1L)).thenReturn(null);

        service.remover(1L);

        verify(entityManager, never()).remove(any());
        verify(transaction).commit();
    }

    @Test
    void remover_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.remover(null));
        verify(entityManager, never()).find(eq(Participacao.class), any());
        verify(transaction, never()).begin();
    }

    // ---------- buscarPorId / listarTodos / buscarPorPessoaId / buscarPorExpedicaoId ----------

    @Test
    void buscarPorId_deveRetornar_quandoEncontrada() {
        Participacao participacao = participacaoValida();
        when(repository.buscarPorId(1L)).thenReturn(participacao);

        assertSame(participacao, service.buscarPorId(1L));
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(null));
    }

    @Test
    void listarTodos_deveDelegarParaRepositorio() {
        List<Participacao> lista = Arrays.asList(participacaoValida());
        when(repository.listarTodos()).thenReturn(lista);

        assertEquals(lista, service.listarTodos());
    }

    @Test
    void buscarPorPessoaId_deveDelegarParaRepositorio() {
        List<Participacao> lista = Arrays.asList(participacaoValida());
        when(repository.buscarPorPessoaId(1L)).thenReturn(lista);

        assertEquals(lista, service.buscarPorPessoaId(1L));
    }

    @Test
    void buscarPorExpedicaoId_deveDelegarParaRepositorio() {
        List<Participacao> lista = Arrays.asList(participacaoValida());
        when(repository.buscarPorExpedicaoId(2L)).thenReturn(lista);

        assertEquals(lista, service.buscarPorExpedicaoId(2L));
    }
}
package com.furnadelampiao.service;

import com.furnadelampiao.domain.Pessoa;
import com.furnadelampiao.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private PessoaRepository repository;

    private PessoaService service;

    @BeforeEach
    void setUp() {
        lenient().when(entityManager.getTransaction()).thenReturn(transaction);
        service = new PessoaService(entityManager, repository);
    }

    private Pessoa pessoaValida() {
        return Pessoa.builder()
                .nome("Maria Silva")
                .cpf("12345678901")
                .dataNasc(LocalDate.of(1990, 5, 10))
                .email("maria@teste.com")
                .telefone("83999999999")
                .situacaoAtiva(true)
                .build();
    }

    @Test
    void salvar_devePersistirDentroDeTransacao_quandoPessoaValida() {
        Pessoa pessoa = pessoaValida();

        service.salvar(pessoa);

        InOrder inOrder = inOrder(transaction, repository);
        inOrder.verify(transaction).begin();
        inOrder.verify(repository).salvar(pessoa);
        inOrder.verify(transaction).commit();
        verify(transaction, never()).rollback();
    }

    @Test
    void salvar_deveFazerRollback_quandoRepositorioLancaExcecao() {
        Pessoa pessoa = pessoaValida();
        doThrow(new RuntimeException("falha de banco")).when(repository).salvar(pessoa);
        when(transaction.isActive()).thenReturn(true);

        assertThrows(RuntimeException.class, () -> service.salvar(pessoa));

        verify(transaction).rollback();
        verify(transaction, never()).commit();
    }

    @Test
    void salvar_deveLancarExcecao_quandoPessoaNula() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.salvar(null));
        assertEquals("Pessoa não pode ser nula.", ex.getMessage());
        verifyNoInteractions(repository);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void salvar_deveLancarExcecao_quandoNomeInvalido(String nomeInvalido) {
        Pessoa pessoa = pessoaValida();
        pessoa.setNome(nomeInvalido);

        assertThrows(IllegalArgumentException.class, () -> service.salvar(pessoa));
        verifyNoInteractions(repository);
    }

    @Test
    void salvar_deveDefinirSituacaoAtivaComoTrue_quandoNula() {
        Pessoa pessoa = pessoaValida();
        pessoa.setSituacaoAtiva(null);

        service.salvar(pessoa);

        assertTrue(pessoa.getSituacaoAtiva());
        verify(repository).salvar(pessoa);
    }

    @Test
    void buscarPorId_deveRetornarPessoa_quandoEncontrada() {
        Pessoa pessoa = pessoaValida();
        when(repository.buscarPorId(1L)).thenReturn(pessoa);

        Pessoa resultado = service.buscarPorId(1L);

        assertSame(pessoa, resultado);
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(null));
        verifyNoInteractions(repository);
    }


    @Test
    void listarTodos_deveDelegarParaRepositorio() {
        List<Pessoa> pessoas = Arrays.asList(pessoaValida(), pessoaValida());
        when(repository.listarTodos()).thenReturn(pessoas);

        List<Pessoa> resultado = service.listarTodos();

        assertEquals(pessoas, resultado);
    }

    @Test
    void atualizar_deveAtualizarDentroDeTransacao_quandoPessoaValidaComId() {
        Pessoa pessoa = pessoaValida();
        pessoa.setId(10L);

        service.atualizar(pessoa);

        InOrder inOrder = inOrder(transaction, repository);
        inOrder.verify(transaction).begin();
        inOrder.verify(repository).atualizar(pessoa);
        inOrder.verify(transaction).commit();
    }

    @Test
    void atualizar_deveLancarExcecao_quandoIdNulo() {
        Pessoa pessoa = pessoaValida();
        pessoa.setId(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.atualizar(pessoa));
        assertEquals("Pessoa precisa de ID para ser atualizada.", ex.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void atualizar_deveLancarExcecao_quandoPessoaInvalida() {
        assertThrows(IllegalArgumentException.class, () -> service.atualizar(null));
        verifyNoInteractions(repository);
    }

    @Test
    void removerPorId_deveRemoverDentroDeTransacao_quandoIdValido() {
        service.removerPorId(5L);

        InOrder inOrder = inOrder(transaction, repository);
        inOrder.verify(transaction).begin();
        inOrder.verify(repository).removerPorId(5L);
        inOrder.verify(transaction).commit();
    }

    @Test
    void removerPorId_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.removerPorId(null));
        verifyNoInteractions(repository);
    }
}
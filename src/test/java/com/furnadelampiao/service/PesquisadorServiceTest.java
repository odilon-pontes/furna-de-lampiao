package com.furnadelampiao.service;

import com.furnadelampiao.domain.Pesquisador;
import com.furnadelampiao.enums.Titulacao;
import com.furnadelampiao.repository.PesquisadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PesquisadorServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private PesquisadorRepository repository;

    private PesquisadorService service;

    @BeforeEach
    void setUp() {
        lenient().when(entityManager.getTransaction()).thenReturn(transaction);
        service = new PesquisadorService(entityManager, repository);
    }

    private Pesquisador pesquisadorValido() {
        return Pesquisador.builder()
                .nome("Dra. Ana Costa")
                .numRegistroInstitucional("REG-001")
                .areaPrincipalPesquisa("Espeleobiologia")
                .titulacao(Titulacao.DOUTORADO)
                .valorDiarioBolsa(new BigDecimal("250.00"))
                .build();
    }

    @Test
    void cadastrar_devePersistir_quandoValido() {
        Pesquisador pesquisador = pesquisadorValido();

        service.cadastrar(pesquisador);

        verify(transaction).begin();
        verify(repository).salvar(pesquisador);
        verify(transaction).commit();
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(null));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoNomeAusente() {
        Pesquisador pesquisador = pesquisadorValido();
        pesquisador.setNome(" ");

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(pesquisador));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoNumRegistroAusente() {
        Pesquisador pesquisador = pesquisadorValido();
        pesquisador.setNumRegistroInstitucional(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(pesquisador));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoAreaPesquisaAusente() {
        Pesquisador pesquisador = pesquisadorValido();
        pesquisador.setAreaPrincipalPesquisa(" ");

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(pesquisador));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoTitulacaoAusente() {
        Pesquisador pesquisador = pesquisadorValido();
        pesquisador.setTitulacao(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(pesquisador));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoValorDiarioBolsaNegativo() {
        Pesquisador pesquisador = pesquisadorValido();
        pesquisador.setValorDiarioBolsa(new BigDecimal("-10.00"));

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(pesquisador));
        verifyNoInteractions(repository);
    }

    @Test
    void atualizar_deveAtualizar_quandoValido() {
        Pesquisador pesquisador = pesquisadorValido();
        pesquisador.setId(1L);

        service.atualizar(pesquisador);

        verify(repository).atualizar(pesquisador);
    }

    @Test
    void atualizar_deveLancarExcecao_quandoIdNulo() {
        Pesquisador pesquisador = pesquisadorValido();

        assertThrows(IllegalArgumentException.class, () -> service.atualizar(pesquisador));
        verify(repository, never()).atualizar(any());
    }

    @Test
    void buscarPorId_deveRetornar_quandoEncontrado() {
        Pesquisador pesquisador = pesquisadorValido();
        when(repository.buscarPorId(1L)).thenReturn(pesquisador);

        assertSame(pesquisador, service.buscarPorId(1L));
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(null));
    }

    @Test
    void listarTodos_deveDelegarParaRepositorio() {
        List<Pesquisador> lista = Arrays.asList(pesquisadorValido());
        when(repository.listarTodos()).thenReturn(lista);

        assertEquals(lista, service.listarTodos());
    }

    @Test
    void buscarPorAreaPesquisa_deveDelegarParaRepositorio() {
        List<Pesquisador> lista = Arrays.asList(pesquisadorValido());
        when(repository.buscarPorAreaPesquisa("Espeleobiologia")).thenReturn(lista);

        assertEquals(lista, service.buscarPorAreaPesquisa("Espeleobiologia"));
    }

    @Test
    void buscarPorTitulacao_deveDelegarParaRepositorio() {
        List<Pesquisador> lista = Arrays.asList(pesquisadorValido());
        when(repository.buscarPorTitulacao(Titulacao.DOUTORADO)).thenReturn(lista);

        assertEquals(lista, service.buscarPorTitulacao(Titulacao.DOUTORADO));
    }
}
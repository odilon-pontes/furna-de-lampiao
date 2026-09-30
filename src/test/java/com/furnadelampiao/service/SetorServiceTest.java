package com.furnadelampiao.service;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.Setor;
import com.furnadelampiao.enums.CondicaoSetor;
import com.furnadelampiao.enums.NivelDificuldadeSetor;
import com.furnadelampiao.repository.CavernaRepository;
import com.furnadelampiao.repository.SetorRepository;
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
class SetorServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private SetorRepository repository;

    @Mock
    private CavernaRepository cavernaRepository;

    private SetorService service;

    @BeforeEach
    void setUp() {
        lenient().when(entityManager.getTransaction()).thenReturn(transaction);
        service = new SetorService(entityManager, repository, cavernaRepository);
    }

    private Caverna cavernaExistente() {
        Caverna caverna = Caverna.builder().nomeOficial("Gruta X").build();
        caverna.setId(1L);
        return caverna;
    }

    private Setor setorValido() {
        return Setor.builder()
                .denominacao("Salão Principal")
                .nivelEstimadoDificuldade(NivelDificuldadeSetor.MODERADO)
                .condicaoCorrente(CondicaoSetor.DISPONIVEL)
                .profundidadeMaxima(new BigDecimal("15.50"))
                .extensaoAproximada(new BigDecimal("100.00"))
                .riscoInundacao(new BigDecimal("2.00"))
                .caverna(cavernaExistente())
                .build();
    }

    // ---------- cadastrar ----------

    @Test
    void cadastrar_devePersistir_quandoSetorValidoECavernaExiste() {
        Setor setor = setorValido();
        when(cavernaRepository.buscarPorId(1L)).thenReturn(cavernaExistente());

        service.cadastrar(setor);

        verify(transaction).begin();
        verify(repository).salvar(setor);
        verify(transaction).commit();
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoSetorNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(null));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoDenominacaoAusente() {
        Setor setor = setorValido();
        setor.setDenominacao(" ");

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(setor));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoNivelDificuldadeAusente() {
        Setor setor = setorValido();
        setor.setNivelEstimadoDificuldade(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(setor));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCondicaoCorrenteAusente() {
        Setor setor = setorValido();
        setor.setCondicaoCorrente(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(setor));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCavernaNaoInformada() {
        Setor setor = setorValido();

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(setor));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCavernaNaoExisteNoBanco() {
        Setor setor = setorValido();
        when(cavernaRepository.buscarPorId(1L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(setor));
        verify(repository, never()).salvar(any());
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoProfundidadeMaximaNegativa() {
        Setor setor = setorValido();
        setor.setProfundidadeMaxima(new BigDecimal("-1"));
        when(cavernaRepository.buscarPorId(1L)).thenReturn(cavernaExistente());

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(setor));
        verify(repository, never()).salvar(any());
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoExtensaoAproximadaNegativa() {
        Setor setor = setorValido();
        setor.setExtensaoAproximada(new BigDecimal("-1"));
        when(cavernaRepository.buscarPorId(1L)).thenReturn(cavernaExistente());

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(setor));
        verify(repository, never()).salvar(any());
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoRiscoInundacaoNegativo() {
        Setor setor = setorValido();
        setor.setRiscoInundacao(new BigDecimal("-1"));
        when(cavernaRepository.buscarPorId(1L)).thenReturn(cavernaExistente());

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(setor));
        verify(repository, never()).salvar(any());
    }

    @Test
    void buscarPorId_deveRetornarSetor_quandoEncontrado() {
        Setor setor = setorValido();
        when(repository.buscarPorId(1L)).thenReturn(setor);

        assertSame(setor, service.buscarPorId(1L));
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(null));
    }

    @Test
    void listarTodos_deveDelegarParaRepositorio() {
        List<Setor> lista = Arrays.asList(setorValido());
        when(repository.listarTodos()).thenReturn(lista);

        assertEquals(lista, service.listarTodos());
    }

    @Test
    void listarPorCaverna_deveDelegarParaRepositorio() {
        List<Setor> lista = Arrays.asList(setorValido());
        when(repository.listarPorCaverna(1L)).thenReturn(lista);

        assertEquals(lista, service.listarPorCaverna(1L));
    }

    @Test
    void listarPorCaverna_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.listarPorCaverna(null));
        verifyNoInteractions(repository);
    }

    @Test
    void listarPorNivelDificuldade_deveDelegarParaRepositorio() {
        List<Setor> lista = Arrays.asList(setorValido());
        when(repository.listarPorNivelDificuldade(NivelDificuldadeSetor.ALTO)).thenReturn(lista);

        assertEquals(lista, service.listarPorNivelDificuldade(NivelDificuldadeSetor.ALTO));
    }

    @Test
    void listarPorNivelDificuldade_deveLancarExcecao_quandoNivelNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.listarPorNivelDificuldade(null));
        verifyNoInteractions(repository);
    }

    @Test
    void atualizar_deveAtualizar_quandoValido() {
        Setor setor = setorValido();
        setor.setId(9L);
        when(cavernaRepository.buscarPorId(1L)).thenReturn(cavernaExistente());

        service.atualizar(setor);

        verify(repository).atualizar(setor);
    }

    @Test
    void atualizar_deveLancarExcecao_quandoIdNulo() {
        Setor setor = setorValido();
        when(cavernaRepository.buscarPorId(1L)).thenReturn(cavernaExistente());

        assertThrows(IllegalArgumentException.class, () -> service.atualizar(setor));
        verify(repository, never()).atualizar(any());
    }

    @Test
    void removerPorId_deveRemover_quandoIdValido() {
        service.removerPorId(1L);
        verify(repository).removerPorId(1L);
    }

    @Test
    void removerPorId_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.removerPorId(null));
        verifyNoInteractions(repository);
    }
}
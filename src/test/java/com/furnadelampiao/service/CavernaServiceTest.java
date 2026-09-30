package com.furnadelampiao.service;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.Localizacao;
import com.furnadelampiao.enums.UnidadeFederativa;
import com.furnadelampiao.repository.CavernaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
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
class CavernaServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private CavernaRepository repository;

    private CavernaService service;

    @BeforeEach
    void setUp() {
        lenient().when(entityManager.getTransaction()).thenReturn(transaction);
        service = new CavernaService(entityManager, repository);
    }

    private Caverna cavernaValida() {
        return Caverna.builder()
                .nomeOficial("Gruta do Lampião")
                .codCadastroAmbiental("COD-001")
                .municipio("Pombal")
                .uf(UnidadeFederativa.PB)
                .coordenadas(Localizacao.builder()
                        .latitude(new BigDecimal("-6.700000"))
                        .longitude(new BigDecimal("-37.800000"))
                        .datumGeodesico("SIRGAS2000")
                        .build())
                .acessoAtualmentePermitido(true)
                .build();
    }

    @Test
    void cadastrar_devePersistirDentroDeTransacao_quandoCavernaValidaESemDuplicidade() {
        Caverna caverna = cavernaValida();
        when(repository.buscarPorCodCadastroAmbiental("COD-001")).thenReturn(null);

        service.cadastrar(caverna);

        InOrder inOrder = inOrder(transaction, repository);
        inOrder.verify(transaction).begin();
        inOrder.verify(repository).salvar(caverna);
        inOrder.verify(transaction).commit();
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCodigoAmbientalJaCadastrado() {
        Caverna caverna = cavernaValida();
        Caverna existente = cavernaValida();
        existente.setId(1L);
        when(repository.buscarPorCodCadastroAmbiental("COD-001")).thenReturn(existente);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(caverna));

        verify(repository, never()).salvar(any());
        verify(transaction, never()).begin();
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCavernaNula() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(null));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoNomeOficialAusente() {
        Caverna caverna = cavernaValida();
        caverna.setNomeOficial(" ");

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(caverna));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCodCadastroAmbientalAusente() {
        Caverna caverna = cavernaValida();
        caverna.setCodCadastroAmbiental(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(caverna));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoUfAusente() {
        Caverna caverna = cavernaValida();
        caverna.setUf(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(caverna));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCoordenadasAusentes() {
        Caverna caverna = cavernaValida();
        caverna.setCoordenadas(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(caverna));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveDefinirAcessoPermitidoComoFalse_quandoNulo() {
        Caverna caverna = cavernaValida();
        caverna.setAcessoAtualmentePermitido(null);
        when(repository.buscarPorCodCadastroAmbiental(anyString())).thenReturn(null);

        service.cadastrar(caverna);

        assertFalse(caverna.getAcessoAtualmentePermitido());
    }

    @Test
    void buscarPorId_deveRetornarCaverna_quandoEncontrada() {
        Caverna caverna = cavernaValida();
        when(repository.buscarPorId(1L)).thenReturn(caverna);

        assertSame(caverna, service.buscarPorId(1L));
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(null));
    }

    @Test
    void listarTodos_deveDelegarParaRepositorio() {
        List<Caverna> lista = Arrays.asList(cavernaValida());
        when(repository.listarTodos()).thenReturn(lista);

        assertEquals(lista, service.listarTodos());
    }

    @Test
    void listarPorUf_deveDelegarParaRepositorio() {
        List<Caverna> lista = Arrays.asList(cavernaValida());
        when(repository.listarPorUf(UnidadeFederativa.PB)).thenReturn(lista);

        assertEquals(lista, service.listarPorUf(UnidadeFederativa.PB));
    }

    @Test
    void listarComAcessoPermitido_deveDelegarParaRepositorio() {
        List<Caverna> lista = Arrays.asList(cavernaValida());
        when(repository.listarComAcessoPermitido()).thenReturn(lista);

        assertEquals(lista, service.listarComAcessoPermitido());
    }

    @Test
    void atualizar_deveAtualizar_quandoValidaSemConflitoDeCodigo() {
        Caverna caverna = cavernaValida();
        caverna.setId(1L);
        when(repository.buscarPorCodCadastroAmbiental("COD-001")).thenReturn(caverna);

        service.atualizar(caverna);

        verify(repository).atualizar(caverna);
    }

    @Test
    void atualizar_deveLancarExcecao_quandoOutraCavernaTemMesmoCodigo() {
        Caverna caverna = cavernaValida();
        caverna.setId(1L);
        Caverna outra = cavernaValida();
        outra.setId(2L);
        when(repository.buscarPorCodCadastroAmbiental("COD-001")).thenReturn(outra);

        assertThrows(IllegalArgumentException.class, () -> service.atualizar(caverna));
        verify(repository, never()).atualizar(any());
    }

    @Test
    void atualizar_deveLancarExcecao_quandoIdNulo() {
        Caverna caverna = cavernaValida();
        caverna.setId(null);

        assertThrows(IllegalArgumentException.class, () -> service.atualizar(caverna));
        verifyNoInteractions(repository);
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
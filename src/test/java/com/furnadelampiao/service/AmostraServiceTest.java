package com.furnadelampiao.service;

import com.furnadelampiao.domain.Amostra;
import com.furnadelampiao.domain.ColetaCientifica;
import com.furnadelampiao.enums.CategoriaAmostra;
import com.furnadelampiao.enums.CondicaoConservacaoAmostra;
import com.furnadelampiao.enums.UnidadeMedida;
import com.furnadelampiao.repository.AmostraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AmostraServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private AmostraRepository repository;

    private AmostraService service;

    @BeforeEach
    void setUp() {
        lenient().when(entityManager.getTransaction()).thenReturn(transaction);
        service = new AmostraService(entityManager, repository);
    }

    private Amostra amostraValida() {
        return Amostra.builder()
                .codCampo("AM-001")
                .categoriaAmostra(CategoriaAmostra.values()[0])
                .volume(new BigDecimal("10.00"))
                .unidadeMedida(UnidadeMedida.values()[0])
                .dataAcondicionamento(LocalDateTime.of(2026, 1, 10, 10, 30))
                .condicaoAmostra(CondicaoConservacaoAmostra.values()[0])
                .indicacaoMaterialPerigoso(false)
                .coletaCientifica(new ColetaCientifica())
                .build();
    }

    @Test
    void cadastrar_devePersistirDentroDeTransacao_quandoAmostraValidaESemDuplicidade() {
        Amostra amostra = amostraValida();

        when(repository.buscarPorCodigoCampo("AM-001"))
                .thenReturn(null);

        service.cadastrar(amostra);

        InOrder inOrder = inOrder(transaction, repository);

        inOrder.verify(transaction).begin();
        inOrder.verify(repository).salvar(amostra);
        inOrder.verify(transaction).commit();
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCodigoCampoJaCadastrado() {
        Amostra amostra = amostraValida();
        Amostra existente = amostraValida();
        existente.setId(1L);

        when(repository.buscarPorCodigoCampo("AM-001"))
                .thenReturn(existente);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(amostra)
        );

        verify(repository, never()).salvar(any());
        verify(transaction, never()).begin();
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoAmostraNula() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(null)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCodigoCampoAusente() {
        Amostra amostra = amostraValida();
        amostra.setCodCampo(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(amostra)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCodigoCampoVazio() {
        Amostra amostra = amostraValida();
        amostra.setCodCampo("   ");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(amostra)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveDefinirMaterialPerigosoComoFalse_quandoNulo() {
        Amostra amostra = amostraValida();
        amostra.setIndicacaoMaterialPerigoso(null);

        when(repository.buscarPorCodigoCampo("AM-001"))
                .thenReturn(null);

        service.cadastrar(amostra);

        assertFalse(amostra.getIndicacaoMaterialPerigoso());
        verify(repository).salvar(amostra);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCategoriaNula() {
        Amostra amostra = amostraValida();
        amostra.setCategoriaAmostra(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(amostra)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoVolumeNulo() {
        Amostra amostra = amostraValida();
        amostra.setVolume(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(amostra)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoVolumeZero() {
        Amostra amostra = amostraValida();
        amostra.setVolume(BigDecimal.ZERO);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(amostra)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoVolumeNegativo() {
        Amostra amostra = amostraValida();
        amostra.setVolume(new BigDecimal("-1"));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(amostra)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoUnidadeMedidaNula() {
        Amostra amostra = amostraValida();
        amostra.setUnidadeMedida(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(amostra)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoDataAcondicionamentoNula() {
        Amostra amostra = amostraValida();
        amostra.setDataAcondicionamento(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(amostra)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCondicaoAmostraNula() {
        Amostra amostra = amostraValida();
        amostra.setCondicaoAmostra(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(amostra)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoColetaCientificaNula() {
        Amostra amostra = amostraValida();
        amostra.setColetaCientifica(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(amostra)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void buscarPorId_deveRetornarAmostra_quandoEncontrada() {
        Amostra amostra = amostraValida();
        when(repository.buscarPorId(1L)).thenReturn(amostra);

        assertSame(
                amostra,
                service.buscarPorId(1L)
        );

        verify(repository).buscarPorId(1L);
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoIdNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.buscarPorId(null)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoIdZero() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.buscarPorId(0L)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoIdNegativo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.buscarPorId(-1L)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void listarTodos_deveDelegarParaRepositorio() {
        List<Amostra> lista = Arrays.asList(
                amostraValida(),
                amostraValida()
        );

        when(repository.listarTodos()).thenReturn(lista);

        assertEquals(
                lista,
                service.listarTodos()
        );

        verify(repository).listarTodos();
    }

    @Test
    void buscarPorCodigoCampo_deveDelegarParaRepositorio() {
        Amostra amostra = amostraValida();

        when(repository.buscarPorCodigoCampo("AM-001"))
                .thenReturn(amostra);

        assertSame(
                amostra,
                service.buscarPorCodigoCampo("AM-001")
        );

        verify(repository).buscarPorCodigoCampo("AM-001");
    }

    @Test
    void buscarPorCodigoCampo_deveRemoverEspacos() {
        Amostra amostra = amostraValida();

        when(repository.buscarPorCodigoCampo("AM-001"))
                .thenReturn(amostra);

        assertSame(
                amostra,
                service.buscarPorCodigoCampo("  AM-001  ")
        );

        verify(repository).buscarPorCodigoCampo("AM-001");
    }

    @Test
    void buscarPorCodigoCampo_deveLancarExcecao_quandoCodigoNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.buscarPorCodigoCampo(null)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void buscarPorCodigoCampo_deveLancarExcecao_quandoCodigoVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.buscarPorCodigoCampo("")
        );

        verifyNoInteractions(repository);
    }

    @Test
    void buscarPorCodigoCampo_deveLancarExcecao_quandoCodigoApenasEspacos() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.buscarPorCodigoCampo("   ")
        );

        verifyNoInteractions(repository);
    }

    @Test
    void buscarPorColetaCientificaId_deveDelegarParaRepositorio() {
        List<Amostra> lista = Arrays.asList(
                amostraValida()
        );

        when(repository.buscarPorColetaCientificaId(10L))
                .thenReturn(lista);

        assertEquals(
                lista,
                service.buscarPorColetaCientificaId(10L)
        );

        verify(repository).buscarPorColetaCientificaId(10L);
    }

    @Test
    void buscarPorColetaCientificaId_deveLancarExcecao_quandoIdNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.buscarPorColetaCientificaId(null)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void buscarPorColetaCientificaId_deveLancarExcecao_quandoIdZero() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.buscarPorColetaCientificaId(0L)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void buscarPorColetaCientificaId_deveLancarExcecao_quandoIdNegativo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.buscarPorColetaCientificaId(-1L)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void atualizar_deveAtualizar_quandoValidaESemConflito() {
        Amostra amostra = amostraValida();
        amostra.setId(1L);

        when(repository.buscarPorCodigoCampo("AM-001"))
                .thenReturn(amostra);

        service.atualizar(amostra);

        verify(transaction).begin();
        verify(repository).atualizar(amostra);
        verify(transaction).commit();
    }

    @Test
    void atualizar_deveLancarExcecao_quandoOutraAmostraTemMesmoCodigo() {
        Amostra amostra = amostraValida();
        amostra.setId(1L);

        Amostra outra = amostraValida();
        outra.setId(2L);

        when(repository.buscarPorCodigoCampo("AM-001"))
                .thenReturn(outra);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.atualizar(amostra)
        );

        verify(repository, never()).atualizar(any());
        verify(transaction, never()).begin();
    }

    @Test
    void atualizar_deveLancarExcecao_quandoAmostraNula() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.atualizar(null)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void atualizar_deveLancarExcecao_quandoIdNulo() {
        Amostra amostra = amostraValida();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.atualizar(amostra)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void removerPorId_deveRemover_quandoIdValido() {
        Amostra amostra = amostraValida();

        when(entityManager.find(Amostra.class, 1L))
                .thenReturn(amostra);

        service.removerPorId(1L);

        verify(entityManager).find(Amostra.class, 1L);
        verify(transaction).begin();
        verify(repository).removerPorId(1L);
        verify(transaction).commit();
    }

    @Test
    void removerPorId_deveLancarExcecao_quandoAmostraNaoExiste() {
        when(entityManager.find(Amostra.class, 999L))
                .thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.removerPorId(999L)
        );

        verify(entityManager).find(Amostra.class, 999L);
        verify(repository, never()).removerPorId(anyLong());
        verify(transaction, never()).begin();
    }
}
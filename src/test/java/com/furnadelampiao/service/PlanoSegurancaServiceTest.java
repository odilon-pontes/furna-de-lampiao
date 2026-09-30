package com.furnadelampiao.service;

import com.furnadelampiao.domain.Localizacao;
import com.furnadelampiao.domain.PlanoSeguranca;
import com.furnadelampiao.repository.PlanoSegurancaRepository;
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
class PlanoSegurancaServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private PlanoSegurancaRepository repository;

    private PlanoSegurancaService service;

    @BeforeEach
    void setUp() {
        lenient().when(entityManager.getTransaction()).thenReturn(transaction);
        service = new PlanoSegurancaService(entityManager, repository);
    }

    private PlanoSeguranca planoValido() {
        return PlanoSeguranca.builder()
                .procedimentosEvacuacao(Arrays.asList("Evacuar pela entrada principal"))
                .pontoExternoEncontro(Localizacao.builder()
                        .latitude(new BigDecimal("-6.700000"))
                        .longitude(new BigDecimal("-37.800000"))
                        .datumGeodesico("SIRGAS2000")
                        .build())
                .tempoMaxSemComunicacao(120)
                .telefoneEmergencia("190")
                .necessidadeEquipeMedica(true)
                .mapaRota(new byte[] {1, 2, 3})
                .build();
    }

    @Test
    void cadastrar_devePersistir_quandoValido() {
        PlanoSeguranca plano = planoValido();

        service.cadastrar(plano);

        verify(transaction).begin();
        verify(repository).salvar(plano);
        verify(transaction).commit();
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(null));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoTempoMaxSemComunicacaoAusente() {
        PlanoSeguranca plano = planoValido();
        plano.setTempoMaxSemComunicacao(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(plano));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoTelefoneEmergenciaAusente() {
        PlanoSeguranca plano = planoValido();
        plano.setTelefoneEmergencia(" ");

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(plano));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoPontoExternoEncontroAusente() {
        PlanoSeguranca plano = planoValido();
        plano.setPontoExternoEncontro(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(plano));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveDefinirNecessidadeEquipeMedicaComoFalse_quandoNula() {
        PlanoSeguranca plano = planoValido();
        plano.setNecessidadeEquipeMedica(null);

        service.cadastrar(plano);

        assertFalse(plano.getNecessidadeEquipeMedica());
    }

    @Test
    void buscarPorId_deveRetornar_quandoEncontrado() {
        PlanoSeguranca plano = planoValido();
        when(repository.buscarPorId(1L)).thenReturn(plano);

        assertSame(plano, service.buscarPorId(1L));
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(null));
    }

    @Test
    void buscarMapaRota_deveDelegarParaRepositorio() {
        byte[] mapa = {1, 2, 3};
        when(repository.buscarMapaRotaPorId(1L)).thenReturn(mapa);

        assertArrayEquals(mapa, service.buscarMapaRota(1L));
    }

    @Test
    void buscarMapaRota_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarMapaRota(null));
        verifyNoInteractions(repository);
    }

    @Test
    void listarTodos_deveDelegarParaRepositorio() {
        List<PlanoSeguranca> lista = Arrays.asList(planoValido());
        when(repository.listarTodos()).thenReturn(lista);

        assertEquals(lista, service.listarTodos());
    }

    @Test
    void atualizar_deveAtualizar_quandoValidoComId() {
        PlanoSeguranca plano = planoValido();
        plano.setId(1L);

        service.atualizar(plano);

        verify(repository).atualizar(plano);
    }

    @Test
    void atualizar_deveLancarExcecao_quandoIdNulo() {
        PlanoSeguranca plano = planoValido();

        assertThrows(IllegalArgumentException.class, () -> service.atualizar(plano));
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
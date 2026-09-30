package com.furnadelampiao.service;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.domain.PlanoSeguranca;
import com.furnadelampiao.domain.Setor;
import com.furnadelampiao.dto.ExpedicaoDetalheDTO;
import com.furnadelampiao.dto.ExpedicaoResumoDTO;
import com.furnadelampiao.enums.SituacaoExpedicao;
import com.furnadelampiao.repository.CavernaRepository;
import com.furnadelampiao.repository.ExpedicaoRepository;
import com.furnadelampiao.repository.PlanoSegurancaRepository;
import com.furnadelampiao.repository.SetorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpedicaoServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private ExpedicaoRepository repository;

    @Mock
    private CavernaRepository cavernaRepository;

    @Mock
    private PlanoSegurancaRepository planoSegurancaRepository;

    @Mock
    private SetorRepository setorRepository;

    private ExpedicaoService service;

    @BeforeEach
    void setUp() {
        lenient().when(entityManager.getTransaction()).thenReturn(transaction);
        service = new ExpedicaoService(
                entityManager, repository, cavernaRepository, planoSegurancaRepository, setorRepository);
    }

    private Caverna cavernaExistente(Long id) {
        Caverna caverna = Caverna.builder().nomeOficial("Gruta X").build();
        caverna.setId(id);
        return caverna;
    }

    private PlanoSeguranca planoExistente(Long id) {
        PlanoSeguranca plano = PlanoSeguranca.builder().build();
        plano.setId(id);
        return plano;
    }

    private Setor setorDaCaverna(Long id, Long cavernaId) {
        Setor setor = Setor.builder().caverna(cavernaExistente(cavernaId)).build();
        setor.setId(id);
        return setor;
    }

    private Expedicao expedicaoValida() {
        return Expedicao.builder()
                .codigo("EXP-001")
                .titulo("Expedição Serra Verde")
                .objetivo("Mapeamento")
                .inicioPrevisto(LocalDateTime.now().plusDays(1))
                .terminoPrevisto(LocalDateTime.now().plusDays(5))
                .orcamentoAprovado(new BigDecimal("1000.00"))
                .custoRealizado(new BigDecimal("0.00"))
                .qtdMaxParticipantes(10)
                .caverna(cavernaExistente(1L))
                .planoSeguranca(planoExistente(2L))
                .build();
    }

    private void mockAssociacoesValidas() {
        when(cavernaRepository.buscarPorId(1L)).thenReturn(cavernaExistente(1L));
        when(planoSegurancaRepository.buscarPorId(2L)).thenReturn(planoExistente(2L));
    }

    @Test
    void cadastrar_devePersistirEDefinirSituacaoPlanejada_quandoValidaSemSituacao() {
        Expedicao expedicao = expedicaoValida();
        mockAssociacoesValidas();
        when(repository.buscarPorCodigo("EXP-001")).thenReturn(null);

        service.cadastrar(expedicao);

        assertEquals(SituacaoExpedicao.PLANEJADA, expedicao.getSituacao());
        verify(transaction).begin();
        verify(repository).salvar(expedicao);
        verify(transaction).commit();
    }

    @Test
    void cadastrar_naoDeveAlterarSituacao_quandoJaInformada() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setSituacao(SituacaoExpedicao.AUTORIZADA);
        mockAssociacoesValidas();
        when(repository.buscarPorCodigo("EXP-001")).thenReturn(null);

        service.cadastrar(expedicao);

        assertEquals(SituacaoExpedicao.AUTORIZADA, expedicao.getSituacao());
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCodigoJaCadastrado() {
        Expedicao expedicao = expedicaoValida();
        mockAssociacoesValidas();
        Expedicao existente = expedicaoValida();
        existente.setId(99L);
        when(repository.buscarPorCodigo("EXP-001")).thenReturn(existente);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(expedicao));
        verify(repository, never()).salvar(any());
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoExpedicaoNula() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(null));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCodigoAusente() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setCodigo(" ");

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(expedicao));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoTituloAusente() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setTitulo(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(expedicao));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCavernaNaoInformada() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setCaverna(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(expedicao));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCavernaNaoExisteNoBanco() {
        Expedicao expedicao = expedicaoValida();
        when(cavernaRepository.buscarPorId(1L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(expedicao));
        verify(repository, never()).salvar(any());
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoPlanoSegurancaNaoInformado() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setPlanoSeguranca(null);
        when(cavernaRepository.buscarPorId(1L)).thenReturn(cavernaExistente(1L));

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(expedicao));
        verify(repository, never()).salvar(any());
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoPlanoSegurancaNaoExisteNoBanco() {
        Expedicao expedicao = expedicaoValida();
        when(cavernaRepository.buscarPorId(1L)).thenReturn(cavernaExistente(1L));
        when(planoSegurancaRepository.buscarPorId(2L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(expedicao));
        verify(repository, never()).salvar(any());
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoDatasAusentes() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setInicioPrevisto(null);
        mockAssociacoesValidas();

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(expedicao));
        verify(repository, never()).salvar(any());
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoTerminoNaoPosteriorAoInicio() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setTerminoPrevisto(expedicao.getInicioPrevisto());
        mockAssociacoesValidas();

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(expedicao));
        verify(repository, never()).salvar(any());
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoQtdMaxParticipantesZeroOuNula() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setQtdMaxParticipantes(0);
        mockAssociacoesValidas();

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(expedicao));
        verify(repository, never()).salvar(any());
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoOrcamentoNegativo() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setOrcamentoAprovado(new BigDecimal("-1"));
        mockAssociacoesValidas();

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(expedicao));
        verify(repository, never()).salvar(any());
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCustoRealizadoNegativo() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setCustoRealizado(new BigDecimal("-1"));
        mockAssociacoesValidas();

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(expedicao));
        verify(repository, never()).salvar(any());
    }

    @Test
    void cadastrar_deveDefinirCancelamentoEmergencialComoFalse_quandoNulo() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setCancelamentoEmergencial(null);
        mockAssociacoesValidas();
        when(repository.buscarPorCodigo("EXP-001")).thenReturn(null);

        service.cadastrar(expedicao);

        assertFalse(expedicao.getCancelamentoEmergencial());
    }

    @Test
    void buscarPorId_deveRetornar_quandoEncontrada() {
        Expedicao expedicao = expedicaoValida();
        when(repository.buscarPorId(1L)).thenReturn(expedicao);

        assertSame(expedicao, service.buscarPorId(1L));
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(null));
    }

    @Test
    void buscarDetalhesPorId_deveDelegarParaRepositorio() {
        ExpedicaoDetalheDTO dto = new ExpedicaoDetalheDTO(
                1L, "EXP-001", "Título", "Objetivo", "Gruta X",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                BigDecimal.TEN, BigDecimal.ONE, 10, SituacaoExpedicao.PLANEJADA, false);
        when(repository.buscarDetalhesPorId(1L)).thenReturn(dto);

        assertSame(dto, service.buscarDetalhesPorId(1L));
    }

    @Test
    void buscarDetalhesPorId_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarDetalhesPorId(null));
        verifyNoInteractions(repository);
    }

    @Test
    void listarTodos_deveDelegarParaRepositorio() {
        List<Expedicao> lista = Arrays.asList(expedicaoValida());
        when(repository.listarTodos()).thenReturn(lista);

        assertEquals(lista, service.listarTodos());
    }

    @Test
    void listarPorCaverna_deveDelegarParaRepositorio() {
        List<Expedicao> lista = Arrays.asList(expedicaoValida());
        when(repository.listarPorCaverna(1L)).thenReturn(lista);

        assertEquals(lista, service.listarPorCaverna(1L));
    }

    @Test
    void listarPorCaverna_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.listarPorCaverna(null));
        verifyNoInteractions(repository);
    }

    @Test
    void listarPorSituacao_deveDelegarParaRepositorio() {
        List<Expedicao> lista = Arrays.asList(expedicaoValida());
        when(repository.listarPorSituacao(SituacaoExpedicao.PLANEJADA)).thenReturn(lista);

        assertEquals(lista, service.listarPorSituacao(SituacaoExpedicao.PLANEJADA));
    }

    @Test
    void listarPorSituacao_deveLancarExcecao_quandoNula() {
        assertThrows(IllegalArgumentException.class, () -> service.listarPorSituacao(null));
        verifyNoInteractions(repository);
    }

    @Test
    void listarResumoPorPeriodoESituacao_deveDelegarParaRepositorio() {
        LocalDate inicio = LocalDate.now();
        LocalDate fim = inicio.plusDays(10);
        List<ExpedicaoResumoDTO> lista = Arrays.asList(
                new ExpedicaoResumoDTO("EXP-001", "T", "C",
                        LocalDateTime.now(), LocalDateTime.now(), SituacaoExpedicao.PLANEJADA));
        when(repository.listarResumoPorPeriodoESituacao(inicio, fim, SituacaoExpedicao.PLANEJADA))
                .thenReturn(lista);

        assertEquals(lista, service.listarResumoPorPeriodoESituacao(inicio, fim, SituacaoExpedicao.PLANEJADA));
    }

    @Test
    void listarResumoPorPeriodoESituacao_deveLancarExcecao_quandoDatasNulas() {
        assertThrows(IllegalArgumentException.class,
                () -> service.listarResumoPorPeriodoESituacao(null, LocalDate.now(), SituacaoExpedicao.PLANEJADA));
        verifyNoInteractions(repository);
    }

    @Test
    void listarResumoPorPeriodoESituacao_deveLancarExcecao_quandoFimAntesDoInicio() {
        LocalDate inicio = LocalDate.now();
        LocalDate fim = inicio.minusDays(1);

        assertThrows(IllegalArgumentException.class,
                () -> service.listarResumoPorPeriodoESituacao(inicio, fim, SituacaoExpedicao.PLANEJADA));
        verifyNoInteractions(repository);
    }

    @Test
    void listarResumoPorPeriodoESituacao_deveLancarExcecao_quandoSituacaoNula() {
        LocalDate inicio = LocalDate.now();
        LocalDate fim = inicio.plusDays(1);

        assertThrows(IllegalArgumentException.class,
                () -> service.listarResumoPorPeriodoESituacao(inicio, fim, null));
        verifyNoInteractions(repository);
    }

    @Test
    void atualizar_deveAtualizar_quandoValidaSemConflito() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setId(1L);
        mockAssociacoesValidas();
        when(repository.buscarPorCodigo("EXP-001")).thenReturn(expedicao);

        service.atualizar(expedicao);

        verify(repository).atualizar(expedicao);
    }

    @Test
    void atualizar_deveLancarExcecao_quandoIdNulo() {
        Expedicao expedicao = expedicaoValida();
        mockAssociacoesValidas();

        assertThrows(IllegalArgumentException.class, () -> service.atualizar(expedicao));
        verify(repository, never()).atualizar(any());
    }

    @Test
    void atualizar_deveLancarExcecao_quandoOutraExpedicaoTemMesmoCodigo() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setId(1L);
        mockAssociacoesValidas();
        Expedicao outra = expedicaoValida();
        outra.setId(2L);
        when(repository.buscarPorCodigo("EXP-001")).thenReturn(outra);

        assertThrows(IllegalArgumentException.class, () -> service.atualizar(expedicao));
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

    @Test
    void associarSetor_deveAssociar_quandoValido() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setId(1L);
        Setor setor = setorDaCaverna(10L, 1L);
        when(repository.buscarPorId(1L)).thenReturn(expedicao);
        when(setorRepository.buscarPorId(10L)).thenReturn(setor);

        service.associarSetor(1L, 10L);

        assertTrue(expedicao.getSetoresVisitados().contains(setor));
        verify(repository).atualizar(expedicao);
    }

    @Test
    void associarSetor_deveLancarExcecao_quandoIdsNulos() {
        assertThrows(IllegalArgumentException.class, () -> service.associarSetor(null, 1L));
        assertThrows(IllegalArgumentException.class, () -> service.associarSetor(1L, null));
        verifyNoInteractions(repository);
    }

    @Test
    void associarSetor_deveLancarExcecao_quandoExpedicaoNaoExiste() {
        when(repository.buscarPorId(1L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.associarSetor(1L, 10L));
        verify(repository, never()).atualizar(any());
    }

    @Test
    void associarSetor_deveLancarExcecao_quandoSetorNaoExiste() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setId(1L);
        when(repository.buscarPorId(1L)).thenReturn(expedicao);
        when(setorRepository.buscarPorId(10L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.associarSetor(1L, 10L));
        verify(repository, never()).atualizar(any());
    }

    @Test
    void associarSetor_deveLancarExcecao_quandoSetorDeOutraCaverna() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setId(1L);
        Setor setorDeOutraCaverna = setorDaCaverna(10L, 999L);
        when(repository.buscarPorId(1L)).thenReturn(expedicao);
        when(setorRepository.buscarPorId(10L)).thenReturn(setorDeOutraCaverna);

        assertThrows(IllegalArgumentException.class, () -> service.associarSetor(1L, 10L));
        verify(repository, never()).atualizar(any());
    }

    @Test
    void associarSetor_deveLancarExcecao_quandoJaAssociado() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setId(1L);
        Setor setor = setorDaCaverna(10L, 1L);
        expedicao.getSetoresVisitados().add(setor);
        when(repository.buscarPorId(1L)).thenReturn(expedicao);
        when(setorRepository.buscarPorId(10L)).thenReturn(setor);

        assertThrows(IllegalArgumentException.class, () -> service.associarSetor(1L, 10L));
        verify(repository, never()).atualizar(any());
    }

    @Test
    void desassociarSetor_deveRemoverAssociacao_quandoValido() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setId(1L);
        Setor setor = setorDaCaverna(10L, 1L);
        expedicao.getSetoresVisitados().add(setor);
        when(repository.buscarPorId(1L)).thenReturn(expedicao);

        service.desassociarSetor(1L, 10L);

        assertTrue(expedicao.getSetoresVisitados().isEmpty());
        verify(repository).atualizar(expedicao);
    }

    @Test
    void desassociarSetor_deveLancarExcecao_quandoIdsNulos() {
        assertThrows(IllegalArgumentException.class, () -> service.desassociarSetor(null, 1L));
        verifyNoInteractions(repository);
    }

    @Test
    void desassociarSetor_deveLancarExcecao_quandoExpedicaoNaoExiste() {
        when(repository.buscarPorId(1L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.desassociarSetor(1L, 10L));
        verify(repository, never()).atualizar(any());
    }

    @Test
    void desassociarSetor_deveLancarExcecao_quandoSetorNaoAssociado() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setId(1L);
        when(repository.buscarPorId(1L)).thenReturn(expedicao);

        assertThrows(IllegalArgumentException.class, () -> service.desassociarSetor(1L, 10L));
        verify(repository, never()).atualizar(any());
    }

    @Test
    void listarSetoresVisitados_deveRetornarLista_quandoExpedicaoExiste() {
        Expedicao expedicao = expedicaoValida();
        expedicao.setId(1L);
        Setor setor = setorDaCaverna(10L, 1L);
        expedicao.setSetoresVisitados(new ArrayList<>(List.of(setor)));
        when(repository.buscarPorId(1L)).thenReturn(expedicao);

        assertEquals(List.of(setor), service.listarSetoresVisitados(1L));
    }

    @Test
    void listarSetoresVisitados_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.listarSetoresVisitados(null));
        verifyNoInteractions(repository);
    }

    @Test
    void listarSetoresVisitados_deveLancarExcecao_quandoExpedicaoNaoExiste() {
        when(repository.buscarPorId(1L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.listarSetoresVisitados(1L));
    }
}
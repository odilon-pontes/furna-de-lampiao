package com.furnadelampiao.service;

import com.furnadelampiao.domain.Equipamento;
import com.furnadelampiao.enums.SituacaoOperacional;
import com.furnadelampiao.enums.TipoEquipamento;
import com.furnadelampiao.repository.EquipamentoRepository;
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
class EquipamentoServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private EquipamentoRepository repository;

    private EquipamentoService service;

    @BeforeEach
    void setUp() {
        lenient().when(entityManager.getTransaction()).thenReturn(transaction);
        service = new EquipamentoService(entityManager, repository);
    }

    private Equipamento equipamentoValido() {
        return Equipamento.builder()
                .codPatrimonial("PAT-001")
                .nome("Capacete espeleológico")
                .tipo(TipoEquipamento.CAPACETE)
                .fabricante("Petzl")
                .valorAquisicao(new BigDecimal("300.00"))
                .dataCompra(LocalDate.now().minusMonths(6))
                .dataUltimaManutencao(LocalDate.now().minusMonths(1))
                .indicacaoCalibracao(false)
                .build();
    }

    @Test
    void cadastrar_devePersistirEDefinirSituacaoDisponivel_quandoValidoSemDuplicidade() {
        Equipamento equipamento = equipamentoValido();
        when(repository.buscarPorCodPatrimonial("PAT-001")).thenReturn(null);

        service.cadastrar(equipamento);

        assertEquals(SituacaoOperacional.DISPONIVEL, equipamento.getSituacaoOperacional());
        verify(transaction).begin();
        verify(repository).salvar(equipamento);
        verify(transaction).commit();
    }

    @Test
    void cadastrar_naoDeveAlterarSituacao_quandoJaInformada() {
        Equipamento equipamento = equipamentoValido();
        equipamento.setSituacaoOperacional(SituacaoOperacional.EM_USO);
        when(repository.buscarPorCodPatrimonial("PAT-001")).thenReturn(null);

        service.cadastrar(equipamento);

        assertEquals(SituacaoOperacional.EM_USO, equipamento.getSituacaoOperacional());
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCodigoPatrimonialJaCadastrado() {
        Equipamento equipamento = equipamentoValido();
        Equipamento existente = equipamentoValido();
        existente.setId(1L);
        when(repository.buscarPorCodPatrimonial("PAT-001")).thenReturn(existente);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(equipamento));
        verify(repository, never()).salvar(any());
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(null));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoCodigoPatrimonialAusente() {
        Equipamento equipamento = equipamentoValido();
        equipamento.setCodPatrimonial(" ");

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(equipamento));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoNomeAusente() {
        Equipamento equipamento = equipamentoValido();
        equipamento.setNome(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(equipamento));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoTipoAusente() {
        Equipamento equipamento = equipamentoValido();
        equipamento.setTipo(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(equipamento));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoDataCompraAusente() {
        Equipamento equipamento = equipamentoValido();
        equipamento.setDataCompra(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(equipamento));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoDataCompraFutura() {
        Equipamento equipamento = equipamentoValido();
        equipamento.setDataCompra(LocalDate.now().plusDays(1));

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(equipamento));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoUltimaManutencaoAnteriorACompra() {
        Equipamento equipamento = equipamentoValido();
        equipamento.setDataUltimaManutencao(equipamento.getDataCompra().minusDays(1));

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(equipamento));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoValorAquisicaoNegativo() {
        Equipamento equipamento = equipamentoValido();
        equipamento.setValorAquisicao(new BigDecimal("-1"));

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(equipamento));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveDefinirIndicacaoCalibracaoComoFalse_quandoNula() {
        Equipamento equipamento = equipamentoValido();
        equipamento.setIndicacaoCalibracao(null);
        when(repository.buscarPorCodPatrimonial("PAT-001")).thenReturn(null);

        service.cadastrar(equipamento);

        assertFalse(equipamento.getIndicacaoCalibracao());
    }

    @Test
    void buscarPorId_deveRetornar_quandoEncontrado() {
        Equipamento equipamento = equipamentoValido();
        when(repository.buscarPorId(1L)).thenReturn(equipamento);

        assertSame(equipamento, service.buscarPorId(1L));
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(null));
    }

    @Test
    void listarDisponiveisEntre_deveDelegarParaRepositorio() {
        LocalDate inicio = LocalDate.now();
        LocalDate fim = inicio.plusDays(5);
        List<Equipamento> lista = Arrays.asList(equipamentoValido());
        when(repository.listarDisponiveisEntre(inicio, fim)).thenReturn(lista);

        assertEquals(lista, service.listarDisponiveisEntre(inicio, fim));
    }

    @Test
    void listarDisponiveisEntre_deveLancarExcecao_quandoDatasNulas() {
        assertThrows(IllegalArgumentException.class,
                () -> service.listarDisponiveisEntre(null, LocalDate.now()));
        verifyNoInteractions(repository);
    }

    @Test
    void listarDisponiveisEntre_deveLancarExcecao_quandoFimAntesDoInicio() {
        LocalDate inicio = LocalDate.now();
        LocalDate fim = inicio.minusDays(1);

        assertThrows(IllegalArgumentException.class,
                () -> service.listarDisponiveisEntre(inicio, fim));
        verifyNoInteractions(repository);
    }

    @Test
    void listarTodos_deveDelegarParaRepositorio() {
        List<Equipamento> lista = Arrays.asList(equipamentoValido());
        when(repository.listarTodos()).thenReturn(lista);

        assertEquals(lista, service.listarTodos());
    }

    @Test
    void listarPorTipo_deveDelegarParaRepositorio() {
        List<Equipamento> lista = Arrays.asList(equipamentoValido());
        when(repository.listarPorTipo(TipoEquipamento.CAPACETE)).thenReturn(lista);

        assertEquals(lista, service.listarPorTipo(TipoEquipamento.CAPACETE));
    }

    @Test
    void listarPorTipo_deveLancarExcecao_quandoNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.listarPorTipo(null));
        verifyNoInteractions(repository);
    }

    @Test
    void listarPorSituacaoOperacional_deveDelegarParaRepositorio() {
        List<Equipamento> lista = Arrays.asList(equipamentoValido());
        when(repository.listarPorSituacaoOperacional(SituacaoOperacional.DISPONIVEL)).thenReturn(lista);

        assertEquals(lista, service.listarPorSituacaoOperacional(SituacaoOperacional.DISPONIVEL));
    }

    @Test
    void listarPorSituacaoOperacional_deveLancarExcecao_quandoNula() {
        assertThrows(IllegalArgumentException.class, () -> service.listarPorSituacaoOperacional(null));
        verifyNoInteractions(repository);
    }

    @Test
    void atualizar_deveAtualizar_quandoValidoSemConflito() {
        Equipamento equipamento = equipamentoValido();
        equipamento.setId(1L);
        when(repository.buscarPorCodPatrimonial("PAT-001")).thenReturn(equipamento);

        service.atualizar(equipamento);

        verify(repository).atualizar(equipamento);
    }

    @Test
    void atualizar_deveLancarExcecao_quandoOutroEquipamentoTemMesmoCodigo() {
        Equipamento equipamento = equipamentoValido();
        equipamento.setId(1L);
        Equipamento outro = equipamentoValido();
        outro.setId(2L);
        when(repository.buscarPorCodPatrimonial("PAT-001")).thenReturn(outro);

        assertThrows(IllegalArgumentException.class, () -> service.atualizar(equipamento));
        verify(repository, never()).atualizar(any());
    }

    @Test
    void atualizar_deveLancarExcecao_quandoIdNulo() {
        Equipamento equipamento = equipamentoValido();

        assertThrows(IllegalArgumentException.class, () -> service.atualizar(equipamento));
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
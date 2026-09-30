package com.furnadelampiao.service;

import com.furnadelampiao.domain.GuiaEspeleologico;
import com.furnadelampiao.enums.NivelCertificacao;
import com.furnadelampiao.repository.GuiaEspeleologicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuiaEspeleologicoServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private GuiaEspeleologicoRepository repository;

    private GuiaEspeleologicoService service;

    @BeforeEach
    void setUp() {
        lenient().when(entityManager.getTransaction()).thenReturn(transaction);
        service = new GuiaEspeleologicoService(entityManager, repository);
    }

    private GuiaEspeleologico guiaValido() {
        return GuiaEspeleologico.builder()
                .nome("Zé da Espeleo")
                .dataNasc(LocalDate.of(1985, 1, 1))
                .numCredenciamento("CRED-001")
                .nivelCertificacao(NivelCertificacao.NIVEL_I)
                .dataValidadeCertificacao(LocalDate.now().plusYears(1))
                .qtdExpedicoesConcluidas(5)
                .build();
    }

    // ---------- cadastrar ----------

    @Test
    void cadastrar_devePersistir_quandoValido() {
        GuiaEspeleologico guia = guiaValido();

        service.cadastrar(guia);

        verify(transaction).begin();
        verify(repository).salvar(guia);
        verify(transaction).commit();
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(null));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoNomeAusente() {
        GuiaEspeleologico guia = guiaValido();
        guia.setNome(" ");

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(guia));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoNumCredenciamentoAusente() {
        GuiaEspeleologico guia = guiaValido();
        guia.setNumCredenciamento(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(guia));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoNivelCertificacaoAusente() {
        GuiaEspeleologico guia = guiaValido();
        guia.setNivelCertificacao(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(guia));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoDataValidadeCertificacaoAusente() {
        GuiaEspeleologico guia = guiaValido();
        guia.setDataValidadeCertificacao(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(guia));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoQtdExpedicoesConcluidasNegativa() {
        GuiaEspeleologico guia = guiaValido();
        guia.setQtdExpedicoesConcluidas(-1);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(guia));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoQtdExpedicoesConcluidasNula() {
        GuiaEspeleologico guia = guiaValido();
        guia.setQtdExpedicoesConcluidas(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(guia));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoDataNascimentoFutura() {
        GuiaEspeleologico guia = guiaValido();
        guia.setDataNasc(LocalDate.now().plusDays(1));

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(guia));
        verifyNoInteractions(repository);
    }

    @Test
    void cadastrar_deveLancarExcecao_quandoValidadeCertificacaoAnteriorAoNascimento() {
        GuiaEspeleologico guia = guiaValido();
        guia.setDataNasc(LocalDate.of(2000, 1, 1));
        guia.setDataValidadeCertificacao(LocalDate.of(1999, 1, 1));

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(guia));
        verifyNoInteractions(repository);
    }

    @Test
    void buscarPorId_deveRetornar_quandoEncontrado() {
        GuiaEspeleologico guia = guiaValido();
        when(repository.buscarPorId(1L)).thenReturn(guia);

        assertSame(guia, service.buscarPorId(1L));
    }

    @Test
    void buscarPorId_deveLancarExcecao_quandoIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(null));
    }

    @Test
    void atualizar_deveAtualizar_quandoValidoEExistente() {
        GuiaEspeleologico guia = guiaValido();
        guia.setId(1L);
        when(repository.buscarPorId(1L)).thenReturn(guia);

        service.atualizar(guia);

        verify(repository).atualizar(guia);
    }

    @Test
    void atualizar_deveLancarExcecao_quandoIdNulo() {
        GuiaEspeleologico guia = guiaValido();

        assertThrows(IllegalArgumentException.class, () -> service.atualizar(guia));
        verify(repository, never()).atualizar(any());
    }

    @Test
    void atualizar_deveLancarExcecao_quandoGuiaNaoEncontrado() {
        GuiaEspeleologico guia = guiaValido();
        guia.setId(1L);
        when(repository.buscarPorId(1L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.atualizar(guia));
        verify(repository, never()).atualizar(any());
    }

    @Test
    void listarTodos_deveDelegarParaRepositorio() {
        List<GuiaEspeleologico> lista = Arrays.asList(guiaValido());
        when(repository.listarTodos()).thenReturn(lista);

        assertEquals(lista, service.listarTodos());
    }

    @Test
    void buscarPorNivelCertificacao_deveDelegarParaRepositorio() {
        List<GuiaEspeleologico> lista = Arrays.asList(guiaValido());
        when(repository.buscarPorNivelCertificacao(NivelCertificacao.NIVEL_I)).thenReturn(lista);

        assertEquals(lista, service.buscarPorNivelCertificacao(NivelCertificacao.NIVEL_I));
    }

    @Test
    void buscarPorNivelCertificacao_deveLancarExcecao_quandoNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarPorNivelCertificacao(null));
        verifyNoInteractions(repository);
    }

    @Test
    void listarCertificacoesVencidas_deveDelegarParaRepositorio() {
        List<GuiaEspeleologico> lista = Arrays.asList(guiaValido());
        when(repository.listarCertificacoesVencidas()).thenReturn(lista);

        assertEquals(lista, service.listarCertificacoesVencidas());
    }
}

package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.Equipamento;
import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.domain.Movimentacao;
import com.furnadelampiao.domain.Pessoa;
import com.furnadelampiao.domain.PlanoSeguranca;
import com.furnadelampiao.enums.*;
import com.furnadelampiao.domain.Localizacao;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovimentacaoRepositoryJpaTest {

    private EntityManagerFactory criarEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("furnaTestPU");
    }

    @Test
    void deveSalvarEBuscarPorId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pessoa pessoa = criarPessoa();
        Equipamento equipamento = criarEquipamento();
        Expedicao expedicao = criarExpedicao();

        entityManager.getTransaction().begin();

        persistirDependencias(entityManager, expedicao);
        entityManager.persist(pessoa);
        entityManager.persist(equipamento);

        Movimentacao movimentacao =
                criarMovimentacao(pessoa, equipamento, expedicao);

        entityManager.persist(movimentacao);

        entityManager.getTransaction().commit();

        MovimentacaoRepositoryJpa repository =
                new MovimentacaoRepositoryJpa(entityManager);

        Movimentacao resultado =
                repository.buscarPorId(movimentacao.getId());

        assertNotNull(resultado);
        assertEquals(movimentacao.getId(), resultado.getId());
        assertEquals(
                equipamento.getId(),
                resultado.getEquipamento().getId()
        );
        assertEquals(
                pessoa.getId(),
                resultado.getPessoa().getId()
        );

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarNullAoBuscarPorIdInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        MovimentacaoRepositoryJpa repository =
                new MovimentacaoRepositoryJpa(entityManager);

        Movimentacao resultado =
                repository.buscarPorId(999999L);

        assertNull(resultado);

        entityManager.close();
        emf.close();
    }

    @Test
    void deveBuscarPorPessoaId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pessoa pessoa = criarPessoa();
        Equipamento equipamento1 = criarEquipamento();
        Equipamento equipamento2 = criarEquipamento();

        equipamento2.setCodPatrimonial("EQ-002");

        Expedicao expedicao1 = criarExpedicao();
        Expedicao expedicao2 = criarExpedicao();

        entityManager.getTransaction().begin();

        persistirDependencias(entityManager, expedicao1);
        persistirDependencias(entityManager, expedicao2);

        entityManager.persist(pessoa);
        entityManager.persist(equipamento1);
        entityManager.persist(equipamento2);

        Movimentacao movimentacao1 =
                criarMovimentacao(pessoa, equipamento1, expedicao1);

        Movimentacao movimentacao2 =
                criarMovimentacao(pessoa, equipamento2, expedicao2);

        entityManager.persist(movimentacao1);
        entityManager.persist(movimentacao2);

        entityManager.getTransaction().commit();

        MovimentacaoRepositoryJpa repository =
                new MovimentacaoRepositoryJpa(entityManager);

        List<Movimentacao> resultado =
                repository.buscarPorPessoaId(pessoa.getId());

        assertEquals(2, resultado.size());

        assertTrue(
                resultado.stream()
                        .allMatch(m -> m.getPessoa().getId()
                                .equals(pessoa.getId()))
        );

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarListaVaziaAoBuscarPorPessoaInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        MovimentacaoRepositoryJpa repository =
                new MovimentacaoRepositoryJpa(entityManager);

        List<Movimentacao> resultado =
                repository.buscarPorPessoaId(999999L);

        assertTrue(resultado.isEmpty());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveBuscarPorExpedicaoId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pessoa pessoa1 = criarPessoa();
        Pessoa pessoa2 = criarPessoa();

        pessoa2.setCpf("98765432100");
        pessoa2.setEmail("pessoa2@teste.com");

        Equipamento equipamento1 = criarEquipamento();
        Equipamento equipamento2 = criarEquipamento();

        equipamento2.setCodPatrimonial("EQ-002");

        Expedicao expedicao = criarExpedicao();

        entityManager.getTransaction().begin();

        persistirDependencias(entityManager, expedicao);

        entityManager.persist(pessoa1);
        entityManager.persist(pessoa2);
        entityManager.persist(equipamento1);
        entityManager.persist(equipamento2);

        Movimentacao movimentacao1 =
                criarMovimentacao(pessoa1, equipamento1, expedicao);

        Movimentacao movimentacao2 =
                criarMovimentacao(pessoa2, equipamento2, expedicao);

        entityManager.persist(movimentacao1);
        entityManager.persist(movimentacao2);

        entityManager.getTransaction().commit();

        MovimentacaoRepositoryJpa repository =
                new MovimentacaoRepositoryJpa(entityManager);

        List<Movimentacao> resultado =
                repository.buscarPorExpedicaoId(expedicao.getId());

        assertEquals(2, resultado.size());

        assertTrue(
                resultado.stream()
                        .allMatch(m -> m.getExpedicao().getId()
                                .equals(expedicao.getId()))
        );

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarListaVaziaAoBuscarPorExpedicaoInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        MovimentacaoRepositoryJpa repository =
                new MovimentacaoRepositoryJpa(entityManager);

        List<Movimentacao> resultado =
                repository.buscarPorExpedicaoId(999999L);

        assertTrue(resultado.isEmpty());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveBuscarPorEquipamentoId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pessoa pessoa1 = criarPessoa();
        Pessoa pessoa2 = criarPessoa();

        pessoa2.setCpf("98765432100");
        pessoa2.setEmail("pessoa2@teste.com");

        Equipamento equipamento = criarEquipamento();

        Expedicao expedicao1 = criarExpedicao();
        Expedicao expedicao2 = criarExpedicao();

        entityManager.getTransaction().begin();

        persistirDependencias(entityManager, expedicao1);
        persistirDependencias(entityManager, expedicao2);

        entityManager.persist(pessoa1);
        entityManager.persist(pessoa2);
        entityManager.persist(equipamento);

        Movimentacao movimentacao1 =
                criarMovimentacao(pessoa1, equipamento, expedicao1);

        Movimentacao movimentacao2 =
                criarMovimentacao(pessoa2, equipamento, expedicao2);

        movimentacao2.setDataHoraRetirada(
                LocalDateTime.now().plusHours(1)
        );

        entityManager.persist(movimentacao1);
        entityManager.persist(movimentacao2);

        entityManager.getTransaction().commit();

        MovimentacaoRepositoryJpa repository =
                new MovimentacaoRepositoryJpa(entityManager);

        List<Movimentacao> resultado =
                repository.buscarPorEquipamentoId(equipamento.getId());

        assertEquals(2, resultado.size());

        assertTrue(
                resultado.stream()
                        .allMatch(m -> m.getEquipamento().getId()
                                .equals(equipamento.getId()))
        );

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarListaVaziaAoBuscarPorEquipamentoInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        MovimentacaoRepositoryJpa repository =
                new MovimentacaoRepositoryJpa(entityManager);

        List<Movimentacao> resultado =
                repository.buscarPorEquipamentoId(999999L);

        assertTrue(resultado.isEmpty());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveListarTodos() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pessoa pessoa1 = criarPessoa();
        Pessoa pessoa2 = criarPessoa();

        pessoa2.setCpf("98765432100");
        pessoa2.setEmail("pessoa2@teste.com");

        Equipamento equipamento1 = criarEquipamento();
        Equipamento equipamento2 = criarEquipamento();

        equipamento2.setCodPatrimonial("EQ-002");

        Expedicao expedicao1 = criarExpedicao();
        Expedicao expedicao2 = criarExpedicao();

        entityManager.getTransaction().begin();

        persistirDependencias(entityManager, expedicao1);
        persistirDependencias(entityManager, expedicao2);

        entityManager.persist(pessoa1);
        entityManager.persist(pessoa2);
        entityManager.persist(equipamento1);
        entityManager.persist(equipamento2);

        Movimentacao movimentacao1 =
                criarMovimentacao(pessoa1, equipamento1, expedicao1);

        Movimentacao movimentacao2 =
                criarMovimentacao(pessoa2, equipamento2, expedicao2);

        entityManager.persist(movimentacao1);
        entityManager.persist(movimentacao2);

        entityManager.getTransaction().commit();

        MovimentacaoRepositoryJpa repository =
                new MovimentacaoRepositoryJpa(entityManager);

        List<Movimentacao> resultado =
                repository.listarTodos();

        assertEquals(2, resultado.size());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveAtualizar() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pessoa pessoa = criarPessoa();
        Equipamento equipamento = criarEquipamento();
        Expedicao expedicao = criarExpedicao();

        entityManager.getTransaction().begin();

        persistirDependencias(entityManager, expedicao);
        entityManager.persist(pessoa);
        entityManager.persist(equipamento);

        Movimentacao movimentacao =
                criarMovimentacao(pessoa, equipamento, expedicao);

        entityManager.persist(movimentacao);

        entityManager.getTransaction().commit();

        entityManager.clear();

        movimentacao.setCustoAvaria(new BigDecimal("350.00"));
        movimentacao.setEstadoRetorno(EstadoRetorno.values()[0]);
        movimentacao.setDataDevolucao(LocalDate.now());

        entityManager.getTransaction().begin();

        MovimentacaoRepositoryJpa repository =
                new MovimentacaoRepositoryJpa(entityManager);

        repository.atualizar(movimentacao);

        entityManager.getTransaction().commit();

        Movimentacao resultado =
                repository.buscarPorId(movimentacao.getId());

        assertEquals(
                new BigDecimal("350.00"),
                resultado.getCustoAvaria()
        );
        assertEquals(
                EstadoRetorno.values()[0],
                resultado.getEstadoRetorno()
        );
        assertEquals(
                LocalDate.now(),
                resultado.getDataDevolucao()
        );

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRemoverPorId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pessoa pessoa = criarPessoa();
        Equipamento equipamento = criarEquipamento();
        Expedicao expedicao = criarExpedicao();

        entityManager.getTransaction().begin();

        persistirDependencias(entityManager, expedicao);
        entityManager.persist(pessoa);
        entityManager.persist(equipamento);

        Movimentacao movimentacao =
                criarMovimentacao(pessoa, equipamento, expedicao);

        entityManager.persist(movimentacao);

        entityManager.getTransaction().commit();

        Long id = movimentacao.getId();

        MovimentacaoRepositoryJpa repository =
                new MovimentacaoRepositoryJpa(entityManager);

        entityManager.getTransaction().begin();
        repository.removerPorId(id);
        entityManager.getTransaction().commit();

        assertNull(repository.buscarPorId(id));

        entityManager.close();
        emf.close();
    }

    @Test
    void naoDeveFalharAoRemoverIdInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        MovimentacaoRepositoryJpa repository =
                new MovimentacaoRepositoryJpa(entityManager);

        entityManager.getTransaction().begin();
        repository.removerPorId(999999L);
        entityManager.getTransaction().commit();

        assertNull(repository.buscarPorId(999999L));

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarTrueQuandoExisteMovimentacaoAtiva() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pessoa pessoa = criarPessoa();
        Equipamento equipamento = criarEquipamento();
        Expedicao expedicao = criarExpedicao();

        entityManager.getTransaction().begin();

        persistirDependencias(entityManager, expedicao);
        entityManager.persist(pessoa);
        entityManager.persist(equipamento);

        Movimentacao movimentacao =
                criarMovimentacao(pessoa, equipamento, expedicao);

        entityManager.persist(movimentacao);

        entityManager.getTransaction().commit();

        MovimentacaoRepositoryJpa repository =
                new MovimentacaoRepositoryJpa(entityManager);

        boolean resultado =
                repository.existeMovimentacaoAtivaPorEquipamentoId(
                        equipamento.getId()
                );

        assertTrue(resultado);

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarFalseQuandoNaoExisteMovimentacaoAtiva() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pessoa pessoa = criarPessoa();
        Equipamento equipamento = criarEquipamento();
        Expedicao expedicao = criarExpedicao();

        entityManager.getTransaction().begin();

        persistirDependencias(entityManager, expedicao);
        entityManager.persist(pessoa);
        entityManager.persist(equipamento);

        Movimentacao movimentacao =
                criarMovimentacao(pessoa, equipamento, expedicao);

        movimentacao.setEstadoRetorno(EstadoRetorno.values()[0]);
        movimentacao.setDataDevolucao(LocalDate.now());

        entityManager.persist(movimentacao);

        entityManager.getTransaction().commit();

        MovimentacaoRepositoryJpa repository =
                new MovimentacaoRepositoryJpa(entityManager);

        boolean resultado =
                repository.existeMovimentacaoAtivaPorEquipamentoId(
                        equipamento.getId()
                );

        assertFalse(resultado);

        entityManager.close();
        emf.close();
    }

    private Movimentacao criarMovimentacao(
            Pessoa pessoa,
            Equipamento equipamento,
            Expedicao expedicao) {

        return Movimentacao.builder()
                .dataHoraRetirada(LocalDateTime.now())
                .dataPrevisaoDevolucao(LocalDate.now().plusDays(10))
                .dataDevolucao(null)
                .estadoSaida(EstadoSaida.values()[0])
                .estadoRetorno(null)
                .custoAvaria(null)
                .expedicao(expedicao)
                .equipamento(equipamento)
                .pessoa(pessoa)
                .build();
    }

    private Pessoa criarPessoa() {
        return Pessoa.builder()
                .nome("Pessoa Teste")
                .cpf("12345678901")
                .dataNasc(LocalDate.of(1995, 5, 10))
                .email("pessoa@teste.com")
                .telefone("83999999999")
                .situacaoAtiva(true)
                .build();
    }

    private Equipamento criarEquipamento() {
        return Equipamento.builder()
                .codPatrimonial("EQ-001")
                .nome("Equipamento Teste")
                .tipo(TipoEquipamento.values()[0])
                .fabricante("Fabricante Teste")
                .valorAquisicao(new BigDecimal("1500.00"))
                .dataCompra(LocalDate.of(2025, 1, 10))
                .dataUltimaManutencao(LocalDate.of(2026, 1, 10))
                .situacaoOperacional(SituacaoOperacional.values()[0])
                .indicacaoCalibracao(false)
                .build();
    }

    private Expedicao criarExpedicao() {
        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();

        return Expedicao.builder()
                .codigo("EXP-" + System.nanoTime())
                .titulo("Expedicao Teste")
                .objetivo("Objetivo teste")
                .inicioPrevisto(LocalDateTime.now())
                .terminoPrevisto(LocalDateTime.now().plusDays(2))
                .orcamentoAprovado(new BigDecimal("1000.00"))
                .custoRealizado(BigDecimal.ZERO)
                .qtdMaxParticipantes(10)
                .situacao(SituacaoExpedicao.values()[0])
                .cancelamentoEmergencial(false)
                .caverna(caverna)
                .planoSeguranca(plano)
                .build();
    }

    private Caverna criarCaverna() {
        return Caverna.builder()
                .nomeOficial("Caverna Teste")
                .codCadastroAmbiental("CAV-" + System.nanoTime())
                .municipio("Joao Pessoa")
                .uf(UnidadeFederativa.PB)
                .coordenadas(
                        new Localizacao(
                                new BigDecimal("222.222"),
                                new BigDecimal("333.333"),
                                "datum_geodesico"
                        )
                )
                .build();
    }

    private PlanoSeguranca criarPlanoSeguranca() {
        return PlanoSeguranca.builder()
                .procedimentosEvacuacao(
                        new java.util.ArrayList<>(
                                List.of(
                                        "Procedimento 1",
                                        "Procedimento 2"
                                )
                        )
                )
                .pontoExternoEncontro(
                        new Localizacao(
                                new BigDecimal("111.111"),
                                new BigDecimal("222.222"),
                                "datum_geodesico"
                        )
                )
                .tempoMaxSemComunicacao(30)
                .telefoneEmergencia("83999999999")
                .necessidadeEquipeMedica(false)
                .mapaRota(new byte[]{1, 2, 3})
                .build();
    }

    private void persistirDependencias(
            EntityManager entityManager,
            Expedicao expedicao) {

        entityManager.persist(expedicao.getCaverna());
        entityManager.persist(expedicao.getPlanoSeguranca());
        entityManager.persist(expedicao);
    }
}


package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.ColetaCientifica;
import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.domain.Pesquisador;
import com.furnadelampiao.domain.PlanoSeguranca;
import com.furnadelampiao.domain.Setor;
import com.furnadelampiao.enums.*;
import com.furnadelampiao.domain.Localizacao;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColetaCientificaRepositoryJpaTest {

    private EntityManagerFactory criarEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("furnaTestPU");
    }

    private Pesquisador criarPesquisador() {
        return Pesquisador.builder()
                .nome("Pesquisador Teste")
                .cpf("12345678901")
                .email("pesquisador@teste.com")
                .telefone("83999999999")
                .dataNasc(LocalDate.of(1995, 5, 10))
                .areaPrincipalPesquisa("Biologia")
                .numRegistroInstitucional("REG-TESTE-001")
                .titulacao(Titulacao.DOUTORADO)
                .build();
    }
    private Caverna criarCaverna() {
        return Caverna.builder()
                .nomeOficial("Caverna Teste")
                .codCadastroAmbiental("TESTE-" + System.nanoTime())
                .municipio("João Pessoa")
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
                .procedimentosEvacuacao(new ArrayList<>(List.of(
                        "Procedimento 1",
                        "Procedimento 2"
                )))
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

    private Expedicao criarExpedicao(
            Caverna caverna,
            PlanoSeguranca plano) {

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

    private Setor criarSetor(Caverna caverna) {
        return Setor.builder()
                .denominacao("Setor Teste")
                .nivelEstimadoDificuldade(NivelDificuldadeSetor.values()[0])
                .profundidadeMaxima(new BigDecimal("50.00"))
                .extensaoAproximada(new BigDecimal("100.00"))
                .descricao("Setor para teste")
                .riscoInundacao(new BigDecimal("10.00"))
                .condicaoCorrente(CondicaoSetor.values()[0])
                .caverna(caverna)
                .build();
    }
    private ColetaCientifica criarColeta(
            Pesquisador pesquisador,
            Expedicao expedicao,
            Setor setor,
            LocalDateTime dataHora) {

        return ColetaCientifica.builder()
                .dataHoraColeta(dataHora)
                .metodoEmpregado(MetodoEmpregado.values()[0])
                .descricaoPonto("Ponto de coleta teste")
                .temperatura(new BigDecimal("25.50"))
                .umidadeRelativa(new BigDecimal("70.00"))
                .profundidade(new BigDecimal("15.50"))
                .observacoes("Observacao teste")
                .situacaoValidacao(SituacaoValidacaoColeta.values()[0])
                .pesquisador(pesquisador)
                .expedicao(expedicao)
                .setor(setor)
                .build();
    }

    private void persistirDependencias(
            EntityManager entityManager,
            Pesquisador pesquisador,
            Caverna caverna,
            PlanoSeguranca plano,
            Expedicao expedicao,
            Setor setor) {

        entityManager.persist(pesquisador);
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(setor);
    }

    @Test
    void deveSalvarEBuscarPorId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pesquisador pesquisador = criarPesquisador();
        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);
        Setor setor = criarSetor(caverna);

        ColetaCientifica coleta = criarColeta(
                pesquisador,
                expedicao,
                setor,
                LocalDateTime.now()
        );

        entityManager.getTransaction().begin();

        entityManager.persist(pesquisador);
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(setor);

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        repository.salvar(coleta);

        entityManager.getTransaction().commit();

        ColetaCientifica resultado =
                repository.buscarPorId(coleta.getId());

        assertNotNull(resultado);
        assertEquals(coleta.getId(), resultado.getId());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarNullAoBuscarPorIdInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        ColetaCientifica resultado =
                repository.buscarPorId(Long.MAX_VALUE);

        assertNull(resultado);

        entityManager.close();
        emf.close();
    }

    @Test
    void deveBuscarPorPesquisadorId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pesquisador pesquisador = criarPesquisador();
        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);
        Setor setor = criarSetor(caverna);

        ColetaCientifica coleta = criarColeta(
                pesquisador,
                expedicao,
                setor,
                LocalDateTime.now()
        );

        entityManager.getTransaction().begin();

        entityManager.persist(pesquisador);
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(setor);
        entityManager.persist(coleta);

        entityManager.getTransaction().commit();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        List<ColetaCientifica> resultado =
                repository.buscarPorPesquisadorId(pesquisador.getId());

        assertEquals(1, resultado.size());
        assertEquals(coleta.getId(), resultado.get(0).getId());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarListaVaziaAoBuscarPorPesquisadorInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        List<ColetaCientifica> resultado =
                repository.buscarPorPesquisadorId(Long.MAX_VALUE);

        assertTrue(resultado.isEmpty());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveBuscarPorExpedicaoId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pesquisador pesquisador = criarPesquisador();
        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);
        Setor setor = criarSetor(caverna);

        ColetaCientifica coleta = criarColeta(
                pesquisador,
                expedicao,
                setor,
                LocalDateTime.now()
        );

        entityManager.getTransaction().begin();

        entityManager.persist(pesquisador);
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(setor);
        entityManager.persist(coleta);

        entityManager.getTransaction().commit();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        List<ColetaCientifica> resultado =
                repository.buscarPorExpedicaoId(expedicao.getId());

        assertEquals(1, resultado.size());
        assertEquals(coleta.getId(), resultado.get(0).getId());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarListaVaziaAoBuscarPorExpedicaoInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        List<ColetaCientifica> resultado =
                repository.buscarPorExpedicaoId(Long.MAX_VALUE);

        assertTrue(resultado.isEmpty());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveBuscarPorSetorId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pesquisador pesquisador = criarPesquisador();
        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);
        Setor setor = criarSetor(caverna);

        ColetaCientifica coleta = criarColeta(
                pesquisador,
                expedicao,
                setor,
                LocalDateTime.now()
        );

        entityManager.getTransaction().begin();

        entityManager.persist(pesquisador);
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(setor);
        entityManager.persist(coleta);

        entityManager.getTransaction().commit();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        List<ColetaCientifica> resultado =
                repository.buscarPorSetorId(setor.getId());

        assertEquals(1, resultado.size());
        assertEquals(coleta.getId(), resultado.get(0).getId());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarListaVaziaAoBuscarPorSetorInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        List<ColetaCientifica> resultado =
                repository.buscarPorSetorId(Long.MAX_VALUE);

        assertTrue(resultado.isEmpty());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveBuscarPorSituacaoValidacao() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pesquisador pesquisador = criarPesquisador();
        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);
        Setor setor = criarSetor(caverna);

        ColetaCientifica coleta = criarColeta(
                pesquisador,
                expedicao,
                setor,
                LocalDateTime.now()
        );

        entityManager.getTransaction().begin();

        entityManager.persist(pesquisador);
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(setor);
        entityManager.persist(coleta);

        entityManager.getTransaction().commit();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        List<ColetaCientifica> resultado =
                repository.buscarPorSituacaoValidacao(
                        coleta.getSituacaoValidacao()
                );

        assertEquals(1, resultado.size());
        assertEquals(coleta.getId(), resultado.get(0).getId());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarListaVaziaAoBuscarPorSituacaoSemResultados() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        SituacaoValidacaoColeta[] valores =
                SituacaoValidacaoColeta.values();

        if (valores.length < 2) {
            entityManager.close();
            emf.close();
            return;
        }

        List<ColetaCientifica> resultado =
                repository.buscarPorSituacaoValidacao(valores[1]);

        assertTrue(resultado.isEmpty());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveBuscarPorMetodoEmpregado() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pesquisador pesquisador = criarPesquisador();
        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);
        Setor setor = criarSetor(caverna);

        ColetaCientifica coleta = criarColeta(
                pesquisador,
                expedicao,
                setor,
                LocalDateTime.now()
        );

        entityManager.getTransaction().begin();

        entityManager.persist(pesquisador);
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(setor);
        entityManager.persist(coleta);

        entityManager.getTransaction().commit();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        List<ColetaCientifica> resultado =
                repository.buscarPorMetodoEmpregado(
                        coleta.getMetodoEmpregado()
                );

        assertEquals(1, resultado.size());
        assertEquals(coleta.getId(), resultado.get(0).getId());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarListaVaziaAoBuscarPorMetodoSemResultados() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        MetodoEmpregado[] valores =
                MetodoEmpregado.values();

        if (valores.length < 2) {
            entityManager.close();
            emf.close();
            return;
        }

        List<ColetaCientifica> resultado =
                repository.buscarPorMetodoEmpregado(valores[1]);

        assertTrue(resultado.isEmpty());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveBuscarPorPeriodo() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pesquisador pesquisador = criarPesquisador();
        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);
        Setor setor = criarSetor(caverna);

        LocalDateTime dataColeta = LocalDateTime.of(
                2026, 9, 28, 10, 0
        );

        ColetaCientifica coleta = criarColeta(
                pesquisador,
                expedicao,
                setor,
                dataColeta
        );

        entityManager.getTransaction().begin();

        entityManager.persist(pesquisador);
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(setor);
        entityManager.persist(coleta);

        entityManager.getTransaction().commit();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        List<ColetaCientifica> resultado =
                repository.buscarPorPeriodo(
                        LocalDateTime.of(2026, 9, 28, 0, 0),
                        LocalDateTime.of(2026, 9, 28, 23, 59, 59)
                );

        assertEquals(1, resultado.size());
        assertEquals(coleta.getId(), resultado.get(0).getId());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarListaVaziaAoBuscarPorPeriodoSemResultados() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pesquisador pesquisador = criarPesquisador();
        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);
        Setor setor = criarSetor(caverna);

        ColetaCientifica coleta = criarColeta(
                pesquisador,
                expedicao,
                setor,
                LocalDateTime.of(2026, 9, 28, 10, 0)
        );

        entityManager.getTransaction().begin();

        entityManager.persist(pesquisador);
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(setor);
        entityManager.persist(coleta);

        entityManager.getTransaction().commit();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        List<ColetaCientifica> resultado =
                repository.buscarPorPeriodo(
                        LocalDateTime.of(2026, 9, 29, 0, 0),
                        LocalDateTime.of(2026, 9, 29, 23, 59, 59)
                );

        assertTrue(resultado.isEmpty());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveListarTodos() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pesquisador pesquisador = criarPesquisador();

        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);

        Setor setor = criarSetor(caverna);

        ColetaCientifica coleta1 = criarColeta(
                pesquisador,
                expedicao,
                setor,
                LocalDateTime.now()
        );

        ColetaCientifica coleta2 = criarColeta(
                pesquisador,
                expedicao,
                setor,
                LocalDateTime.now().plusHours(1)
        );

        entityManager.getTransaction().begin();

        entityManager.persist(pesquisador);
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(setor);
        entityManager.persist(coleta1);
        entityManager.persist(coleta2);

        entityManager.getTransaction().commit();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        List<ColetaCientifica> resultado =
                repository.listarTodos();

        assertEquals(2, resultado.size());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveAtualizar() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pesquisador pesquisador = criarPesquisador();
        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);
        Setor setor = criarSetor(caverna);

        ColetaCientifica coleta = criarColeta(
                pesquisador,
                expedicao,
                setor,
                LocalDateTime.now()
        );

        entityManager.getTransaction().begin();

        entityManager.persist(pesquisador);
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(setor);
        entityManager.persist(coleta);

        entityManager.getTransaction().commit();

        entityManager.clear();

        coleta.setDescricaoPonto("Ponto atualizado");

        entityManager.getTransaction().begin();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        repository.atualizar(coleta);

        entityManager.getTransaction().commit();

        ColetaCientifica resultado =
                repository.buscarPorId(coleta.getId());

        assertEquals(
                "Ponto atualizado",
                resultado.getDescricaoPonto()
        );

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRemoverPorId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pesquisador pesquisador = criarPesquisador();
        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);
        Setor setor = criarSetor(caverna);

        ColetaCientifica coleta = criarColeta(
                pesquisador,
                expedicao,
                setor,
                LocalDateTime.now()
        );

        entityManager.getTransaction().begin();

        entityManager.persist(pesquisador);
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(setor);
        entityManager.persist(coleta);

        entityManager.getTransaction().commit();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        entityManager.getTransaction().begin();

        repository.removerPorId(coleta.getId());

        entityManager.getTransaction().commit();

        assertNull(repository.buscarPorId(coleta.getId()));

        entityManager.close();
        emf.close();
    }

    @Test
    void naoDeveFalharAoRemoverIdInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        ColetaCientificaRepositoryJpa repository =
                new ColetaCientificaRepositoryJpa(entityManager);

        entityManager.getTransaction().begin();

        repository.removerPorId(Long.MAX_VALUE);

        entityManager.getTransaction().commit();

        assertNull(repository.buscarPorId(Long.MAX_VALUE));

        entityManager.close();
        emf.close();
    }
}

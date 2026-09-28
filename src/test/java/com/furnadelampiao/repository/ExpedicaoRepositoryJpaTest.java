package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.domain.Localizacao;
import com.furnadelampiao.domain.PlanoSeguranca;
import com.furnadelampiao.dto.ExpedicaoResumoDTO;
import com.furnadelampiao.enums.SituacaoExpedicao;
import com.furnadelampiao.enums.UnidadeFederativa;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpedicaoRepositoryJpaTest {

    @Test
    void deveSalvarEBuscarPorId() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("furnaTestPU");

        EntityManager entityManager = emf.createEntityManager();

        ExpedicaoRepositoryJpa repository =
                new ExpedicaoRepositoryJpa(entityManager);

        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);

        entityManager.getTransaction().begin();

        entityManager.persist(caverna);
        entityManager.persist(plano);
        repository.salvar(expedicao);

        entityManager.getTransaction().commit();

        Long id = expedicao.getId();

        Expedicao resultado = repository.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(expedicao.getCodigo(), resultado.getCodigo());
        assertEquals(expedicao.getTitulo(), resultado.getTitulo());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarNullAoBuscarPorIdInexistente() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("furnaTestPU");

        EntityManager entityManager = emf.createEntityManager();

        ExpedicaoRepositoryJpa repository =
                new ExpedicaoRepositoryJpa(entityManager);

        Expedicao resultado = repository.buscarPorId(999999L);

        assertNull(resultado);

        entityManager.close();
        emf.close();
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

    @Test
    void deveBuscarPorCodigo() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("furnaTestPU");

        EntityManager entityManager = emf.createEntityManager();

        ExpedicaoRepositoryJpa repository =
                new ExpedicaoRepositoryJpa(entityManager);

        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);

        entityManager.getTransaction().begin();

        entityManager.persist(caverna);
        entityManager.persist(plano);
        repository.salvar(expedicao);

        entityManager.getTransaction().commit();

        Expedicao resultado =
                repository.buscarPorCodigo(expedicao.getCodigo());

        assertNotNull(resultado);
        assertEquals(expedicao.getId(), resultado.getId());
        assertEquals(expedicao.getCodigo(), resultado.getCodigo());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarNullAoBuscarPorCodigoInexistente() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("furnaTestPU");

        EntityManager entityManager = emf.createEntityManager();

        ExpedicaoRepositoryJpa repository =
                new ExpedicaoRepositoryJpa(entityManager);

        Expedicao resultado =
                repository.buscarPorCodigo("CODIGO-INEXISTENTE");

        assertNull(resultado);

        entityManager.close();
        emf.close();
    }

    @Test
    void deveListarPorCaverna() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("furnaTestPU");

        EntityManager entityManager = emf.createEntityManager();

        ExpedicaoRepositoryJpa repository =
                new ExpedicaoRepositoryJpa(entityManager);

        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);

        entityManager.getTransaction().begin();

        entityManager.persist(caverna);
        entityManager.persist(plano);
        repository.salvar(expedicao);

        entityManager.getTransaction().commit();

        List<Expedicao> resultado =
                repository.listarPorCaverna(caverna.getId());

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(expedicao.getId(), resultado.get(0).getId());
        assertEquals(caverna.getId(), resultado.get(0).getCaverna().getId());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarListaVaziaAoListarPorCavernaInexistente() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("furnaTestPU");

        EntityManager entityManager = emf.createEntityManager();

        ExpedicaoRepositoryJpa repository =
                new ExpedicaoRepositoryJpa(entityManager);

        List<Expedicao> resultado =
                repository.listarPorCaverna(999999L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveListarPorSituacao() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("furnaTestPU");

        EntityManager entityManager = emf.createEntityManager();

        ExpedicaoRepositoryJpa repository =
                new ExpedicaoRepositoryJpa(entityManager);

        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);

        SituacaoExpedicao situacao = expedicao.getSituacao();

        entityManager.getTransaction().begin();

        entityManager.persist(caverna);
        entityManager.persist(plano);
        repository.salvar(expedicao);

        entityManager.getTransaction().commit();

        List<Expedicao> resultado =
                repository.listarPorSituacao(situacao);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(expedicao.getId(), resultado.get(0).getId());
        assertEquals(situacao, resultado.get(0).getSituacao());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarListaVaziaAoListarPorSituacaoSemResultados() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("furnaTestPU");

        EntityManager entityManager = emf.createEntityManager();

        ExpedicaoRepositoryJpa repository =
                new ExpedicaoRepositoryJpa(entityManager);

        SituacaoExpedicao situacao = SituacaoExpedicao.values()[1];

        List<Expedicao> resultado =
                repository.listarPorSituacao(situacao);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        entityManager.close();
        emf.close();
    }

    private PlanoSeguranca criarPlanoSeguranca() {
        return PlanoSeguranca.builder()
                .procedimentosEvacuacao(List.of(
                        "Procedimento 1",
                        "Procedimento 2"
                ))
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

    @Test
    void deveListarResumoPorPeriodoESituacao() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("furnaTestPU");

        EntityManager entityManager = emf.createEntityManager();

        ExpedicaoRepositoryJpa repository =
                new ExpedicaoRepositoryJpa(entityManager);

        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);

        entityManager.getTransaction().begin();

        entityManager.persist(caverna);
        entityManager.persist(plano);
        repository.salvar(expedicao);

        entityManager.getTransaction().commit();

        LocalDate inicio = expedicao.getInicioPrevisto().toLocalDate().minusDays(1);
        LocalDate fim = expedicao.getTerminoPrevisto().toLocalDate().plusDays(1);

        List<ExpedicaoResumoDTO> resultado =
                repository.listarResumoPorPeriodoESituacao(
                        inicio,
                        fim,
                        expedicao.getSituacao()
                );

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        ExpedicaoResumoDTO resumo = resultado.get(0);

        assertEquals(expedicao.getCodigo(), resumo.getCodigo());
        assertEquals(expedicao.getTitulo(), resumo.getTitulo());
        assertEquals(caverna.getNomeOficial(), resumo.getNomeCaverna());
        assertEquals(
                expedicao.getInicioPrevisto().withNano(
                        expedicao.getInicioPrevisto().getNano() / 1000 * 1000
                ),
                resumo.getInicioPrevisto()
        );

        assertEquals(
                expedicao.getTerminoPrevisto().withNano(
                        expedicao.getTerminoPrevisto().getNano() / 1000 * 1000
                ),
                resumo.getTerminoPrevisto()
        );
        assertEquals(expedicao.getSituacao(), resumo.getSituacao());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarListaVaziaAoListarResumoSemResultados() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("furnaTestPU");

        EntityManager entityManager = emf.createEntityManager();

        ExpedicaoRepositoryJpa repository =
                new ExpedicaoRepositoryJpa(entityManager);

        List<ExpedicaoResumoDTO> resultado =
                repository.listarResumoPorPeriodoESituacao(
                        LocalDate.of(2000, 1, 1),
                        LocalDate.of(2000, 1, 2),
                        SituacaoExpedicao.values()[0]
                );

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        entityManager.close();
        emf.close();
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
}
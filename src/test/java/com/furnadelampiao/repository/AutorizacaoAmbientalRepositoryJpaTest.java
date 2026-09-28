
package com.furnadelampiao.repository;

import com.furnadelampiao.domain.AutorizacaoAmbiental;
import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.domain.Localizacao;
import com.furnadelampiao.domain.PlanoSeguranca;
import com.furnadelampiao.enums.SituacaoAutorizacao;
import com.furnadelampiao.enums.SituacaoExpedicao;
import com.furnadelampiao.enums.UnidadeFederativa;
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

class AutorizacaoAmbientalRepositoryJpaTest {

    private EntityManagerFactory criarEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("furnaTestPU");
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
            PlanoSeguranca planoSeguranca) {

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
                .planoSeguranca(planoSeguranca)
                .build();
    }

    private AutorizacaoAmbiental criarAutorizacao(
            Expedicao expedicao,
            SituacaoAutorizacao situacao) {

        return AutorizacaoAmbiental.builder()
                .num((int) (System.nanoTime() % Integer.MAX_VALUE))
                .orgaoEmissor("IBAMA")
                .dataEmissao(LocalDate.now())
                .validade(LocalDate.now().plusDays(30))
                .situacao(situacao)
                .observacoes("Autorizacao para teste")
                .arquivoPdfAssinado(new byte[]{1, 2, 3})
                .expedicao(expedicao)
                .build();
    }

    @Test
    void deveSalvarEBuscarPorId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);
        AutorizacaoAmbiental autorizacao =
                criarAutorizacao(expedicao, SituacaoAutorizacao.VIGENTE);

        entityManager.getTransaction().begin();
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(autorizacao);
        entityManager.getTransaction().commit();

        Long id = autorizacao.getId();

        AutorizacaoAmbientalRepositoryJpa repository =
                new AutorizacaoAmbientalRepositoryJpa(entityManager);

        AutorizacaoAmbiental resultado = repository.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("IBAMA", resultado.getOrgaoEmissor());
        assertEquals(SituacaoAutorizacao.VIGENTE, resultado.getSituacao());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarNullAoBuscarPorIdInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        AutorizacaoAmbientalRepositoryJpa repository =
                new AutorizacaoAmbientalRepositoryJpa(entityManager);

        AutorizacaoAmbiental resultado = repository.buscarPorId(999999L);

        assertNull(resultado);

        entityManager.close();
        emf.close();
    }

    @Test
    void deveListarTodos() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);

        AutorizacaoAmbiental autorizacao1 =
                criarAutorizacao(expedicao, SituacaoAutorizacao.VIGENTE);

        AutorizacaoAmbiental autorizacao2 =
                criarAutorizacao(expedicao, SituacaoAutorizacao.EXPIRADA);

        entityManager.getTransaction().begin();
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(autorizacao1);
        entityManager.persist(autorizacao2);
        entityManager.getTransaction().commit();

        AutorizacaoAmbientalRepositoryJpa repository =
                new AutorizacaoAmbientalRepositoryJpa(entityManager);

        List<AutorizacaoAmbiental> resultado = repository.listarTodos();

        assertEquals(2, resultado.size());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveAtualizar() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);
        AutorizacaoAmbiental autorizacao =
                criarAutorizacao(expedicao, SituacaoAutorizacao.VIGENTE);

        entityManager.getTransaction().begin();
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(autorizacao);
        entityManager.getTransaction().commit();

        Long id = autorizacao.getId();

        entityManager.clear();

        AutorizacaoAmbientalRepositoryJpa repository =
                new AutorizacaoAmbientalRepositoryJpa(entityManager);

        AutorizacaoAmbiental autorizacaoAtualizada =
                repository.buscarPorId(id);

        autorizacaoAtualizada.setOrgaoEmissor("ICMBio");
        autorizacaoAtualizada.setObservacoes("Autorizacao atualizada");

        entityManager.getTransaction().begin();
        repository.atualizar(autorizacaoAtualizada);
        entityManager.getTransaction().commit();

        entityManager.clear();

        AutorizacaoAmbiental resultado =
                repository.buscarPorId(id);

        assertEquals("ICMBio", resultado.getOrgaoEmissor());
        assertEquals("Autorizacao atualizada", resultado.getObservacoes());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRemoverPorId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);
        AutorizacaoAmbiental autorizacao =
                criarAutorizacao(expedicao, SituacaoAutorizacao.VIGENTE);

        entityManager.getTransaction().begin();
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(autorizacao);
        entityManager.getTransaction().commit();

        Long id = autorizacao.getId();

        AutorizacaoAmbientalRepositoryJpa repository =
                new AutorizacaoAmbientalRepositoryJpa(entityManager);

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

        AutorizacaoAmbientalRepositoryJpa repository =
                new AutorizacaoAmbientalRepositoryJpa(entityManager);

        entityManager.getTransaction().begin();
        assertDoesNotThrow(() ->
                repository.removerPorId(999999L)
        );
        entityManager.getTransaction().commit();

        entityManager.close();
        emf.close();
    }

    @Test
    void deveContarVigentesPorExpedicao() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);

        AutorizacaoAmbiental autorizacaoVigente1 =
                criarAutorizacao(expedicao, SituacaoAutorizacao.VIGENTE);

        AutorizacaoAmbiental autorizacaoVigente2 =
                criarAutorizacao(expedicao, SituacaoAutorizacao.VIGENTE);

        entityManager.getTransaction().begin();
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(autorizacaoVigente1);
        entityManager.persist(autorizacaoVigente2);
        entityManager.getTransaction().commit();

        AutorizacaoAmbientalRepositoryJpa repository =
                new AutorizacaoAmbientalRepositoryJpa(entityManager);

        long resultado = repository.contarVigentesPorExpedicao(
                expedicao.getId(),
                autorizacaoVigente1.getId()
        );

        assertEquals(1L, resultado);

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarZeroAoContarVigentesSemAutorizacaoValida() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);

        AutorizacaoAmbiental autorizacaoVencida =
                criarAutorizacao(expedicao, SituacaoAutorizacao.EXPIRADA);

        entityManager.getTransaction().begin();
        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(autorizacaoVencida);
        entityManager.getTransaction().commit();

        AutorizacaoAmbientalRepositoryJpa repository =
                new AutorizacaoAmbientalRepositoryJpa(entityManager);

        long resultado = repository.contarVigentesPorExpedicao(
                expedicao.getId(),
                autorizacaoVencida.getId()
        );

        assertEquals(0L, resultado);

        entityManager.close();
        emf.close();
    }
}

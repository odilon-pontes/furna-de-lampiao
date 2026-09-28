
package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.domain.Localizacao;
import com.furnadelampiao.domain.PlanoSeguranca;
import com.furnadelampiao.domain.Relatorio;
import com.furnadelampiao.enums.SituacaoExpedicao;
import com.furnadelampiao.enums.SituacaoRelatorioFinal;
import com.furnadelampiao.enums.UnidadeFederativa;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RelatorioRepositoryJpaTest {

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

    private Relatorio criarRelatorio(Expedicao expedicao) {
        return Relatorio.builder()
                .titulo("Relatorio Teste")
                .resumo("Resumo do relatorio de teste")
                .dataSubmissao(LocalDateTime.now())
                .numeroTotalPaginas(10)
                .situacaoRelatorioFinal(SituacaoRelatorioFinal.values()[0])
                .arquivoCompleto(new byte[]{10, 20, 30, 40})
                .publicacaoAutorizada(false)
                .expedicao(expedicao)
                .build();
    }

    private void persistirExpedicao(
            EntityManager entityManager,
            Caverna caverna,
            PlanoSeguranca plano,
            Expedicao expedicao) {

        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
    }

    @Test
    void deveSalvarEBuscarPorId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);
        Relatorio relatorio = criarRelatorio(expedicao);

        entityManager.getTransaction().begin();
        persistirExpedicao(entityManager, caverna, plano, expedicao);
        entityManager.persist(relatorio);
        entityManager.getTransaction().commit();

        Long id = relatorio.getId();

        RelatorioRepositoryJpa repository =
                new RelatorioRepositoryJpa(entityManager);

        Relatorio resultado = repository.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Relatorio Teste", resultado.getTitulo());
        assertEquals(10, resultado.getNumeroTotalPaginas());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarNullAoBuscarPorIdInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        RelatorioRepositoryJpa repository =
                new RelatorioRepositoryJpa(entityManager);

        Relatorio resultado = repository.buscarPorId(999999L);

        assertNull(resultado);

        entityManager.close();
        emf.close();
    }

    @Test
    void deveBuscarPorExpedicaoId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);
        Relatorio relatorio = criarRelatorio(expedicao);

        entityManager.getTransaction().begin();
        persistirExpedicao(entityManager, caverna, plano, expedicao);
        entityManager.persist(relatorio);
        entityManager.getTransaction().commit();

        RelatorioRepositoryJpa repository =
                new RelatorioRepositoryJpa(entityManager);

        Relatorio resultado =
                repository.buscarPorExpedicaoId(expedicao.getId());

        assertNotNull(resultado);
        assertEquals(relatorio.getId(), resultado.getId());
        assertEquals(expedicao.getId(), resultado.getExpedicao().getId());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarNullAoBuscarPorExpedicaoInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        RelatorioRepositoryJpa repository =
                new RelatorioRepositoryJpa(entityManager);

        Relatorio resultado =
                repository.buscarPorExpedicaoId(999999L);

        assertNull(resultado);

        entityManager.close();
        emf.close();
    }

    @Test
    void deveListarTodos() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Caverna caverna1 = criarCaverna();
        PlanoSeguranca plano1 = criarPlanoSeguranca();
        Expedicao expedicao1 = criarExpedicao(caverna1, plano1);
        Relatorio relatorio1 = criarRelatorio(expedicao1);

        Caverna caverna2 = criarCaverna();
        PlanoSeguranca plano2 = criarPlanoSeguranca();
        Expedicao expedicao2 = criarExpedicao(caverna2, plano2);
        Relatorio relatorio2 = criarRelatorio(expedicao2);

        entityManager.getTransaction().begin();

        persistirExpedicao(
                entityManager,
                caverna1,
                plano1,
                expedicao1
        );
        entityManager.persist(relatorio1);

        persistirExpedicao(
                entityManager,
                caverna2,
                plano2,
                expedicao2
        );
        entityManager.persist(relatorio2);

        entityManager.getTransaction().commit();

        RelatorioRepositoryJpa repository =
                new RelatorioRepositoryJpa(entityManager);

        List<Relatorio> resultado = repository.listarTodos();

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
        Relatorio relatorio = criarRelatorio(expedicao);

        entityManager.getTransaction().begin();
        persistirExpedicao(entityManager, caverna, plano, expedicao);
        entityManager.persist(relatorio);
        entityManager.getTransaction().commit();

        Long id = relatorio.getId();

        entityManager.clear();

        RelatorioRepositoryJpa repository =
                new RelatorioRepositoryJpa(entityManager);

        Relatorio relatorioAtualizado =
                repository.buscarPorId(id);

        relatorioAtualizado.setTitulo("Relatorio Atualizado");
        relatorioAtualizado.setResumo("Resumo atualizado");
        relatorioAtualizado.setNumeroTotalPaginas(20);
        relatorioAtualizado.setPublicacaoAutorizada(true);

        entityManager.getTransaction().begin();
        repository.atualizar(relatorioAtualizado);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Relatorio resultado =
                repository.buscarPorId(id);

        assertEquals("Relatorio Atualizado", resultado.getTitulo());
        assertEquals("Resumo atualizado", resultado.getResumo());
        assertEquals(20, resultado.getNumeroTotalPaginas());
        assertTrue(resultado.getPublicacaoAutorizada());

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
        Relatorio relatorio = criarRelatorio(expedicao);

        entityManager.getTransaction().begin();
        persistirExpedicao(entityManager, caverna, plano, expedicao);
        entityManager.persist(relatorio);
        entityManager.getTransaction().commit();

        Long id = relatorio.getId();

        RelatorioRepositoryJpa repository =
                new RelatorioRepositoryJpa(entityManager);

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

        RelatorioRepositoryJpa repository =
                new RelatorioRepositoryJpa(entityManager);

        entityManager.getTransaction().begin();

        assertDoesNotThrow(() ->
                repository.removerPorId(999999L)
        );

        entityManager.getTransaction().commit();

        entityManager.close();
        emf.close();
    }

    @Test
    void deveBuscarArquivoPorRelatorioId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Caverna caverna = criarCaverna();
        PlanoSeguranca plano = criarPlanoSeguranca();
        Expedicao expedicao = criarExpedicao(caverna, plano);
        byte[] arquivo = new byte[]{10, 20, 30, 40};

        Relatorio relatorio = Relatorio.builder()
                .titulo("Relatorio Teste")
                .resumo("Resumo do relatorio")
                .dataSubmissao(LocalDateTime.now())
                .numeroTotalPaginas(10)
                .situacaoRelatorioFinal(SituacaoRelatorioFinal.values()[0])
                .arquivoCompleto(arquivo)
                .publicacaoAutorizada(false)
                .expedicao(expedicao)
                .build();

        entityManager.getTransaction().begin();
        persistirExpedicao(entityManager, caverna, plano, expedicao);
        entityManager.persist(relatorio);
        entityManager.getTransaction().commit();

        RelatorioRepositoryJpa repository =
                new RelatorioRepositoryJpa(entityManager);

        byte[] resultado =
                repository.buscarArquivoPorRelatorioId(relatorio.getId());

        assertNotNull(resultado);
        assertArrayEquals(arquivo, resultado);

        entityManager.close();
        emf.close();
    }
}

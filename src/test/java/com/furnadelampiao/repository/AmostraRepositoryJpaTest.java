
package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Amostra;
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

class AmostraRepositoryJpaTest {

    private EntityManagerFactory criarEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("furnaTestPU");
    }

    @Test
    void deveSalvarEBuscarPorId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        ColetaCientifica coleta = criarColeta();

        entityManager.getTransaction().begin();
        persistirDependencias(entityManager, coleta);
        entityManager.persist(coleta);

        Amostra amostra = criarAmostra(coleta);
        entityManager.persist(amostra);
        entityManager.getTransaction().commit();

        AmostraRepositoryJpa repository =
                new AmostraRepositoryJpa(entityManager);

        Amostra resultado = repository.buscarPorId(amostra.getId());

        assertNotNull(resultado);
        assertEquals(amostra.getId(), resultado.getId());
        assertEquals("AMO-001", resultado.getCodCampo());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarNullAoBuscarPorIdInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        AmostraRepositoryJpa repository =
                new AmostraRepositoryJpa(entityManager);

        Amostra resultado = repository.buscarPorId(999999L);

        assertNull(resultado);

        entityManager.close();
        emf.close();
    }

    @Test
    void deveListarTodos() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        ColetaCientifica coleta = criarColeta();

        entityManager.getTransaction().begin();
        persistirDependencias(entityManager, coleta);
        entityManager.persist(coleta);

        Amostra amostra1 = criarAmostra(coleta);
        Amostra amostra2 = criarAmostra(coleta);

        amostra2.setCodCampo("AMO-002");

        entityManager.persist(amostra1);
        entityManager.persist(amostra2);
        entityManager.getTransaction().commit();

        AmostraRepositoryJpa repository =
                new AmostraRepositoryJpa(entityManager);

        List<Amostra> resultado = repository.listarTodos();

        assertEquals(2, resultado.size());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveAtualizar() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        ColetaCientifica coleta = criarColeta();

        entityManager.getTransaction().begin();
        persistirDependencias(entityManager, coleta);
        entityManager.persist(coleta);

        Amostra amostra = criarAmostra(coleta);
        entityManager.persist(amostra);
        entityManager.getTransaction().commit();

        entityManager.clear();

        amostra.setObservacoes("Observacao atualizada");
        amostra.setVolume(new BigDecimal("25.50"));

        entityManager.getTransaction().begin();

        AmostraRepositoryJpa repository =
                new AmostraRepositoryJpa(entityManager);

        repository.atualizar(amostra);

        entityManager.getTransaction().commit();

        Amostra resultado = repository.buscarPorId(amostra.getId());

        assertEquals("Observacao atualizada", resultado.getObservacoes());
        assertEquals(
                new BigDecimal("25.50"),
                resultado.getVolume()
        );

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRemoverPorId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        ColetaCientifica coleta = criarColeta();

        entityManager.getTransaction().begin();
        persistirDependencias(entityManager, coleta);
        entityManager.persist(coleta);

        Amostra amostra = criarAmostra(coleta);
        entityManager.persist(amostra);
        entityManager.getTransaction().commit();

        Long id = amostra.getId();

        AmostraRepositoryJpa repository =
                new AmostraRepositoryJpa(entityManager);

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

        AmostraRepositoryJpa repository =
                new AmostraRepositoryJpa(entityManager);

        entityManager.getTransaction().begin();
        repository.removerPorId(999999L);
        entityManager.getTransaction().commit();

        assertNull(repository.buscarPorId(999999L));

        entityManager.close();
        emf.close();
    }

    @Test
    void deveBuscarPorColetaCientificaId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        ColetaCientifica coleta = criarColeta();

        entityManager.getTransaction().begin();
        persistirDependencias(entityManager, coleta);
        entityManager.persist(coleta);

        Amostra amostra1 = criarAmostra(coleta);
        Amostra amostra2 = criarAmostra(coleta);

        amostra2.setCodCampo("AMO-002");

        entityManager.persist(amostra1);
        entityManager.persist(amostra2);
        entityManager.getTransaction().commit();

        AmostraRepositoryJpa repository =
                new AmostraRepositoryJpa(entityManager);

        List<Amostra> resultado =
                repository.buscarPorColetaCientificaId(coleta.getId());

        assertEquals(2, resultado.size());
        assertTrue(
                resultado.stream()
                        .allMatch(a -> a.getColetaCientifica().getId()
                                .equals(coleta.getId()))
        );

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarListaVaziaAoBuscarPorColetaCientificaInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        AmostraRepositoryJpa repository =
                new AmostraRepositoryJpa(entityManager);

        List<Amostra> resultado =
                repository.buscarPorColetaCientificaId(999999L);

        assertTrue(resultado.isEmpty());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveBuscarPorCodigoCampo() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        ColetaCientifica coleta = criarColeta();

        entityManager.getTransaction().begin();
        persistirDependencias(entityManager, coleta);
        entityManager.persist(coleta);

        Amostra amostra = criarAmostra(coleta);
        entityManager.persist(amostra);
        entityManager.getTransaction().commit();

        AmostraRepositoryJpa repository =
                new AmostraRepositoryJpa(entityManager);

        Amostra resultado =
                repository.buscarPorCodigoCampo("AMO-001");

        assertNotNull(resultado);
        assertEquals(amostra.getId(), resultado.getId());
        assertEquals("AMO-001", resultado.getCodCampo());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarNullAoBuscarPorCodigoCampoInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        AmostraRepositoryJpa repository =
                new AmostraRepositoryJpa(entityManager);

        Amostra resultado =
                repository.buscarPorCodigoCampo("CODIGO-INEXISTENTE");

        assertNull(resultado);

        entityManager.close();
        emf.close();
    }

    private Amostra criarAmostra(ColetaCientifica coleta) {
        return Amostra.builder()
                .codCampo("AMO-001")
                .categoriaAmostra(CategoriaAmostra.values()[0])
                .volume(new BigDecimal("10.50"))
                .unidadeMedida(UnidadeMedida.values()[0])
                .dataAcondicionamento(LocalDateTime.now())
                .condicaoAmostra(CondicaoConservacaoAmostra.values()[0])
                .indicacaoMaterialPerigoso(false)
                .fotografia(new byte[]{1, 2, 3})
                .observacoes("Amostra para teste")
                .coletaCientifica(coleta)
                .build();
    }

    private ColetaCientifica criarColeta() {
        return ColetaCientifica.builder()
                .dataHoraColeta(LocalDateTime.now())
                .metodoEmpregado(MetodoEmpregado.values()[0])
                .descricaoPonto("Ponto de coleta teste")
                .temperatura(new BigDecimal("25.00"))
                .umidadeRelativa(new BigDecimal("70.00"))
                .profundidade(new BigDecimal("10.00"))
                .observacoes("Coleta para teste")
                .situacaoValidacao(SituacaoValidacaoColeta.values()[0])
                .pesquisador(criarPesquisador())
                .expedicao(criarExpedicao())
                .setor(null)
                .build();
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
                        new ArrayList<>(List.of(
                                "Procedimento 1",
                                "Procedimento 2"
                        ))
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

    private void persistirDependencias(
            EntityManager entityManager,
            ColetaCientifica coleta) {

        Pesquisador pesquisador = coleta.getPesquisador();
        Expedicao expedicao = coleta.getExpedicao();
        Caverna caverna = expedicao.getCaverna();
        PlanoSeguranca plano = expedicao.getPlanoSeguranca();
        Setor setor = criarSetor(caverna);

        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
        entityManager.persist(pesquisador);
        entityManager.persist(setor);

        coleta.setSetor(setor);
    }
}

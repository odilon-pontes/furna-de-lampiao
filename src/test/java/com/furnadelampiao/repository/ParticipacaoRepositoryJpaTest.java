package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.domain.Pessoa;
import com.furnadelampiao.domain.Participacao;
import com.furnadelampiao.domain.Pesquisador;
import com.furnadelampiao.domain.PlanoSeguranca;
import com.furnadelampiao.enums.PapelParticipante;
import com.furnadelampiao.enums.SituacaoExpedicao;
import com.furnadelampiao.domain.Localizacao;
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

class ParticipacaoRepositoryJpaTest {

    private EntityManagerFactory criarEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("furnaTestPU");
    }

    @Test
    void deveSalvarEBuscarPorId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pessoa pessoa = criarPessoa();
        Expedicao expedicao = criarExpedicao();

        entityManager.getTransaction().begin();
        persistirDependencias(entityManager, expedicao);
        entityManager.persist(pessoa);

        Participacao participacao = criarParticipacao(pessoa, expedicao);
        entityManager.persist(participacao);
        entityManager.getTransaction().commit();

        ParticipacaoRepositoryJpa repository =
                new ParticipacaoRepositoryJpa(entityManager);

        Participacao resultado =
                repository.buscarPorId(participacao.getId());

        assertNotNull(resultado);
        assertEquals(participacao.getId(), resultado.getId());
        assertEquals(
                PapelParticipante.values()[0],
                resultado.getPapelParticipante()
        );

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarNullAoBuscarPorIdInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        ParticipacaoRepositoryJpa repository =
                new ParticipacaoRepositoryJpa(entityManager);

        Participacao resultado =
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
        Expedicao expedicao1 = criarExpedicao();
        Expedicao expedicao2 = criarExpedicao();

        entityManager.getTransaction().begin();

        persistirDependencias(entityManager, expedicao1);
        persistirDependencias(entityManager, expedicao2);
        entityManager.persist(pessoa);

        Participacao participacao1 =
                criarParticipacao(pessoa, expedicao1);

        Participacao participacao2 =
                criarParticipacao(pessoa, expedicao2);

        entityManager.persist(participacao1);
        entityManager.persist(participacao2);

        entityManager.getTransaction().commit();

        ParticipacaoRepositoryJpa repository =
                new ParticipacaoRepositoryJpa(entityManager);

        List<Participacao> resultado =
                repository.buscarPorPessoaId(pessoa.getId());

        assertEquals(2, resultado.size());

        assertTrue(
                resultado.stream()
                        .allMatch(p -> p.getPessoa().getId()
                                .equals(pessoa.getId()))
        );

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarListaVaziaAoBuscarPorPessoaInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        ParticipacaoRepositoryJpa repository =
                new ParticipacaoRepositoryJpa(entityManager);

        List<Participacao> resultado =
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
        pessoa2.setEmail("pesquisador2@teste.com");

        Expedicao expedicao = criarExpedicao();

        entityManager.getTransaction().begin();

        persistirDependencias(entityManager, expedicao);
        entityManager.persist(pessoa1);
        entityManager.persist(pessoa2);

        Participacao participacao1 =
                criarParticipacao(pessoa1, expedicao);

        Participacao participacao2 =
                criarParticipacao(pessoa2, expedicao);

        entityManager.persist(participacao1);
        entityManager.persist(participacao2);

        entityManager.getTransaction().commit();

        ParticipacaoRepositoryJpa repository =
                new ParticipacaoRepositoryJpa(entityManager);

        List<Participacao> resultado =
                repository.buscarPorExpedicaoId(expedicao.getId());

        assertEquals(2, resultado.size());

        assertTrue(
                resultado.stream()
                        .allMatch(p -> p.getExpedicao().getId()
                                .equals(expedicao.getId()))
        );

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarListaVaziaAoBuscarPorExpedicaoInexistente() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        ParticipacaoRepositoryJpa repository =
                new ParticipacaoRepositoryJpa(entityManager);

        List<Participacao> resultado =
                repository.buscarPorExpedicaoId(999999L);

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
        pessoa2.setEmail("pesquisador2@teste.com");

        Expedicao expedicao1 = criarExpedicao();
        Expedicao expedicao2 = criarExpedicao();

        entityManager.getTransaction().begin();

        persistirDependencias(entityManager, expedicao1);
        persistirDependencias(entityManager, expedicao2);

        entityManager.persist(pessoa1);
        entityManager.persist(pessoa2);

        Participacao participacao1 =
                criarParticipacao(pessoa1, expedicao1);

        Participacao participacao2 =
                criarParticipacao(pessoa2, expedicao2);

        entityManager.persist(participacao1);
        entityManager.persist(participacao2);

        entityManager.getTransaction().commit();

        ParticipacaoRepositoryJpa repository =
                new ParticipacaoRepositoryJpa(entityManager);

        List<Participacao> resultado =
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
        Expedicao expedicao = criarExpedicao();

        entityManager.getTransaction().begin();

        persistirDependencias(entityManager, expedicao);
        entityManager.persist(pessoa);

        Participacao participacao =
                criarParticipacao(pessoa, expedicao);

        entityManager.persist(participacao);
        entityManager.getTransaction().commit();

        entityManager.clear();

        participacao.setValorDiaria(new BigDecimal("250.00"));
        participacao.setQtdPrevistaDias(15);
        participacao.setPresencaConfirmada(true);

        entityManager.getTransaction().begin();

        ParticipacaoRepositoryJpa repository =
                new ParticipacaoRepositoryJpa(entityManager);

        repository.atualizar(participacao);

        entityManager.getTransaction().commit();

        Participacao resultado =
                repository.buscarPorId(participacao.getId());

        assertEquals(
                new BigDecimal("250.00"),
                resultado.getValorDiaria()
        );
        assertEquals(15, resultado.getQtdPrevistaDias());
        assertTrue(resultado.getPresencaConfirmada());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRemoverPorId() {
        EntityManagerFactory emf = criarEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        Pessoa pessoa = criarPessoa();
        Expedicao expedicao = criarExpedicao();

        entityManager.getTransaction().begin();

        persistirDependencias(entityManager, expedicao);
        entityManager.persist(pessoa);

        Participacao participacao =
                criarParticipacao(pessoa, expedicao);

        entityManager.persist(participacao);
        entityManager.getTransaction().commit();

        Long id = participacao.getId();

        ParticipacaoRepositoryJpa repository =
                new ParticipacaoRepositoryJpa(entityManager);

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

        ParticipacaoRepositoryJpa repository =
                new ParticipacaoRepositoryJpa(entityManager);

        entityManager.getTransaction().begin();
        repository.removerPorId(999999L);
        entityManager.getTransaction().commit();

        assertNull(repository.buscarPorId(999999L));

        entityManager.close();
        emf.close();
    }

    private Pessoa criarPessoa() {
        return Pessoa.builder()
                .nome("Pessoa Teste")
                .cpf("12345678901")
                .email("pessoa@teste.com")
                .telefone("83999999999")
                .dataNasc(LocalDate.of(1995, 5, 10))
                .build();
    }

    private Participacao criarParticipacao(
            Pessoa pessoa,
            Expedicao expedicao) {

        return Participacao.builder()
                .pessoa(pessoa)
                .expedicao(expedicao)
                .papelParticipante(PapelParticipante.values()[0])
                .dataConfirmacao(LocalDate.now())
                .valorDiaria(new BigDecimal("150.00"))
                .qtdPrevistaDias(10)
                .presencaConfirmada(false)
                .observacoes("Participacao para teste")
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

        Caverna caverna = expedicao.getCaverna();
        PlanoSeguranca plano = expedicao.getPlanoSeguranca();

        entityManager.persist(caverna);
        entityManager.persist(plano);
        entityManager.persist(expedicao);
    }
}

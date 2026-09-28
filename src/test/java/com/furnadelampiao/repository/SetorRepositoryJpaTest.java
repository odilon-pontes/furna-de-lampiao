package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.Localizacao;
import com.furnadelampiao.domain.Setor;
import com.furnadelampiao.enums.CondicaoSetor;
import com.furnadelampiao.enums.NivelDificuldadeSetor;
import com.furnadelampiao.enums.UnidadeFederativa;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SetorRepositoryJpaTest {

    private static EntityManagerFactory emf;

    private EntityManager entityManager;
    private SetorRepositoryJpa repository;

    @BeforeAll
    static void iniciarBanco() {
        emf = Persistence.createEntityManagerFactory("furnaTestPU");
    }

    @BeforeEach
    void iniciarTeste() {
        entityManager = emf.createEntityManager();
        repository = new SetorRepositoryJpa(entityManager);

        entityManager.getTransaction().begin();

        entityManager.createQuery("DELETE FROM Setor").executeUpdate();
        entityManager.createQuery("DELETE FROM Caverna").executeUpdate();

        entityManager.getTransaction().commit();
    }

    @AfterEach
    void finalizarTeste() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }

    @AfterAll
    static void finalizarBanco() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    void deveSalvarSetor() {

        Caverna caverna = criarCaverna("Caverna Salvar");
        Setor setor = criarSetor(
                "Setor A",
                NivelDificuldadeSetor.BAIXO
        );

        caverna.adicionarSetor(setor);

        entityManager.getTransaction().begin();

        entityManager.persist(caverna);

        entityManager.getTransaction().commit();

        assertNotNull(setor.getId());

        Setor setorBanco =
                entityManager.find(Setor.class, setor.getId());

        assertNotNull(setorBanco);
        assertEquals("Setor A", setorBanco.getDenominacao());
    }

    @Test
    void deveBuscarSetorPorId() {

        Caverna caverna = criarCaverna("Caverna Busca");
        Setor setor = criarSetor(
                "Setor A",
                NivelDificuldadeSetor.MODERADO
        );

        caverna.adicionarSetor(setor);

        entityManager.getTransaction().begin();
        entityManager.persist(caverna);
        entityManager.getTransaction().commit();

        Setor resultado =
                repository.buscarPorId(setor.getId());

        assertNotNull(resultado);
        assertEquals(setor.getId(), resultado.getId());
        assertEquals("Setor A", resultado.getDenominacao());
    }

    @Test
    void deveRetornarNullAoBuscarSetorInexistente() {

        Setor resultado =
                repository.buscarPorId(999999L);

        assertNull(resultado);
    }

    @Test
    void deveListarTodosOsSetores() {

        Caverna caverna = criarCaverna("Caverna Lista");

        Setor setor1 = criarSetor(
                "Setor A",
                NivelDificuldadeSetor.BAIXO
        );

        Setor setor2 = criarSetor(
                "Setor B",
                NivelDificuldadeSetor.ALTO
        );

        caverna.adicionarSetor(setor1);
        caverna.adicionarSetor(setor2);

        entityManager.getTransaction().begin();
        entityManager.persist(caverna);
        entityManager.getTransaction().commit();

        List<Setor> setores =
                repository.listarTodos();

        assertEquals(2, setores.size());
    }

    @Test
    void deveListarSetoresPorCaverna() {

        Caverna caverna1 = criarCaverna("Caverna 1");
        Caverna caverna2 = criarCaverna("Caverna 2");

        Setor setor1 = criarSetor(
                "Setor A",
                NivelDificuldadeSetor.BAIXO
        );

        Setor setor2 = criarSetor(
                "Setor B",
                NivelDificuldadeSetor.ALTO
        );

        Setor setor3 = criarSetor(
                "Setor C",
                NivelDificuldadeSetor.MODERADO
        );

        caverna1.adicionarSetor(setor1);
        caverna1.adicionarSetor(setor2);
        caverna2.adicionarSetor(setor3);

        entityManager.getTransaction().begin();

        entityManager.persist(caverna1);
        entityManager.persist(caverna2);

        entityManager.getTransaction().commit();

        List<Setor> setores =
                repository.listarPorCaverna(
                        caverna1.getId()
                );

        assertEquals(2, setores.size());

        assertTrue(
                setores.stream()
                        .allMatch(
                                s -> s.getCaverna()
                                        .getId()
                                        .equals(caverna1.getId())
                        )
        );
    }

    @Test
    void deveRetornarListaVaziaParaCavernaSemSetores() {

        Caverna caverna = criarCaverna(
                "Caverna Sem Setores"
        );

        entityManager.getTransaction().begin();
        entityManager.persist(caverna);
        entityManager.getTransaction().commit();

        List<Setor> setores =
                repository.listarPorCaverna(
                        caverna.getId()
                );

        assertTrue(setores.isEmpty());
    }

    @Test
    void deveListarSetoresPorNivelDeDificuldade() {

        Caverna caverna = criarCaverna(
                "Caverna Dificuldade"
        );

        Setor setor1 = criarSetor(
                "Setor Fácil 1",
                NivelDificuldadeSetor.BAIXO
        );

        Setor setor2 = criarSetor(
                "Setor Fácil 2",
                NivelDificuldadeSetor.BAIXO
        );

        Setor setor3 = criarSetor(
                "Setor Difícil",
                NivelDificuldadeSetor.ALTO
        );

        caverna.adicionarSetor(setor1);
        caverna.adicionarSetor(setor2);
        caverna.adicionarSetor(setor3);

        entityManager.getTransaction().begin();
        entityManager.persist(caverna);
        entityManager.getTransaction().commit();

        List<Setor> setores =
                repository.listarPorNivelDificuldade(
                        NivelDificuldadeSetor.BAIXO
                );

        assertEquals(2, setores.size());

        assertTrue(
                setores.stream()
                        .allMatch(
                                s -> s.getNivelEstimadoDificuldade()
                                        == NivelDificuldadeSetor.BAIXO
                        )
        );
    }

    @Test
    void deveRetornarListaVaziaParaNivelSemSetores() {

        Caverna caverna = criarCaverna(
                "Caverna Sem Nivel"
        );

        Setor setor = criarSetor(
                "Setor Alto",
                NivelDificuldadeSetor.ALTO
        );

        caverna.adicionarSetor(setor);

        entityManager.getTransaction().begin();
        entityManager.persist(caverna);
        entityManager.getTransaction().commit();

        List<Setor> setores =
                repository.listarPorNivelDificuldade(
                        NivelDificuldadeSetor.BAIXO
                );

        assertTrue(setores.isEmpty());
    }

    @Test
    void deveAtualizarSetor() {

        Caverna caverna = criarCaverna(
                "Caverna Atualizacao"
        );

        Setor setor = criarSetor(
                "Nome Antigo",
                NivelDificuldadeSetor.BAIXO
        );

        caverna.adicionarSetor(setor);

        entityManager.getTransaction().begin();
        entityManager.persist(caverna);
        entityManager.getTransaction().commit();

        Long id = setor.getId();

        setor.setDenominacao("Nome Novo");
        setor.setNivelEstimadoDificuldade(
                NivelDificuldadeSetor.ALTO
        );

        entityManager.getTransaction().begin();

        repository.atualizar(setor);

        entityManager.getTransaction().commit();

        entityManager.clear();

        Setor atualizado =
                repository.buscarPorId(id);

        assertNotNull(atualizado);
        assertEquals(
                "Nome Novo",
                atualizado.getDenominacao()
        );
        assertEquals(
                NivelDificuldadeSetor.ALTO,
                atualizado.getNivelEstimadoDificuldade()
        );
    }

    @Test
    void deveRemoverSetorPorId() {

        Caverna caverna = criarCaverna(
                "Caverna Remocao"
        );

        Setor setor = criarSetor(
                "Setor Remover",
                NivelDificuldadeSetor.BAIXO
        );

        caverna.adicionarSetor(setor);

        entityManager.getTransaction().begin();
        entityManager.persist(caverna);
        entityManager.getTransaction().commit();

        Long id = setor.getId();

        entityManager.getTransaction().begin();

        repository.removerPorId(id);

        entityManager.getTransaction().commit();

        entityManager.clear();

        Setor resultado =
                null;

        assertNull(resultado);
    }

    @Test
    void deveIgnorarRemocaoDeSetorInexistente() {

        assertDoesNotThrow(() -> {

            entityManager.getTransaction().begin();

            repository.removerPorId(999999L);

            entityManager.getTransaction().commit();
        });
    }

    private Caverna criarCaverna(String nome) {

        return Caverna.builder()
                .nomeOficial(nome)
                .codCadastroAmbiental("TESTE-" + nome)
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

    private Setor criarSetor(
            String denominacao,
            NivelDificuldadeSetor nivel
    ) {

        return Setor.builder()
                .denominacao(denominacao)
                .nivelEstimadoDificuldade(nivel)
                .condicaoCorrente(
                        CondicaoSetor.EM_MONITORAMENTO
                )
                .build();
    }
}
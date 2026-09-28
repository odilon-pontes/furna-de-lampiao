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
    void deveListarTodosOsSetores() {

        // Arrange
        Caverna caverna = criarCaverna("Caverna Teste");

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

        // Act
        List<Setor> setores = repository.listarTodos();

        // Assert
        assertEquals(2, setores.size());
    }

    @Test
    void deveListarSetoresPorCaverna() {

        // Arrange
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

        // Act
        List<Setor> setores =
                repository.listarPorCaverna(caverna1.getId());

        // Assert
        assertEquals(2, setores.size());

        assertTrue(
                setores.stream()
                        .allMatch(s ->
                                s.getCaverna().getId()
                                        .equals(caverna1.getId())
                        )
        );
    }

    @Test
    void deveListarSetoresPorNivelDeDificuldade() {

        // Arrange
        Caverna caverna = criarCaverna("Caverna Teste 2");

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

        // Act
        List<Setor> setores =
                repository.listarPorNivelDificuldade(
                        NivelDificuldadeSetor.BAIXO
                );

        // Assert
        assertEquals(2, setores.size());

        assertTrue(
                setores.stream()
                        .allMatch(s ->
                                s.getNivelEstimadoDificuldade()
                                        == NivelDificuldadeSetor.BAIXO
                        )
        );
    }

    private Caverna criarCaverna(String nome) {
        return Caverna.builder()
                .nomeOficial(nome)
                .codCadastroAmbiental("TESTE-" + nome)
                .municipio("João Pessoa")
                .uf(UnidadeFederativa.PB)
                .coordenadas(new Localizacao(new BigDecimal(222.222), new BigDecimal(333.333), "datum_geodesico" ))
                .build();
    }

    private Setor criarSetor(
            String denominacao,
            NivelDificuldadeSetor nivel) {

        return Setor.builder()
                .denominacao(denominacao)
                .nivelEstimadoDificuldade(nivel)
                .condicaoCorrente(CondicaoSetor.EM_MONITORAMENTO)
                .build();
    }
}
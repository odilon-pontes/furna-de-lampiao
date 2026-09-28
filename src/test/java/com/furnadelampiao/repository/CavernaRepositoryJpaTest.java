package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.Localizacao;
import com.furnadelampiao.enums.UnidadeFederativa;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CavernaRepositoryJpaTest {

    private static EntityManagerFactory emf;

    private EntityManager entityManager;
    private CavernaRepositoryJpa repository;

    @BeforeAll
    static void iniciarBanco() {
        emf = Persistence.createEntityManagerFactory("furnaTestPU");
    }

    @BeforeEach
    void iniciarTeste() {
        entityManager = emf.createEntityManager();
        repository = new CavernaRepositoryJpa(entityManager);

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

    // =========================================================
    // SALVAR
    // =========================================================

    @Test
    void deveSalvarCaverna() {

        Caverna caverna = criarCaverna(
                "Caverna Salvar",
                "CAD-SALVAR",
                UnidadeFederativa.PB
        );

        entityManager.getTransaction().begin();

        repository.salvar(caverna);

        entityManager.getTransaction().commit();

        assertNotNull(caverna.getId());

        Caverna salva =
                entityManager.find(
                        Caverna.class,
                        caverna.getId()
                );

        assertNotNull(salva);
        assertEquals(
                "Caverna Salvar",
                salva.getNomeOficial()
        );
        assertEquals(
                "CAD-SALVAR",
                salva.getCodCadastroAmbiental()
        );
    }

    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    @Test
    void deveBuscarCavernaPorId() {

        Caverna caverna = criarCaverna(
                "Caverna Busca",
                "CAD-BUSCA",
                UnidadeFederativa.PB
        );

        entityManager.getTransaction().begin();
        repository.salvar(caverna);
        entityManager.getTransaction().commit();

        Caverna resultado =
                repository.buscarPorId(caverna.getId());

        assertNotNull(resultado);
        assertEquals(
                caverna.getId(),
                resultado.getId()
        );
        assertEquals(
                "Caverna Busca",
                resultado.getNomeOficial()
        );
    }

    @Test
    void deveRetornarNullAoBuscarCavernaInexistente() {

        Caverna resultado =
                repository.buscarPorId(999999L);

        assertNull(resultado);
    }

    @Test
    void deveListarTodasAsCavernas() {

        Caverna caverna1 = criarCaverna(
                "Caverna 1",
                "CAD-001",
                UnidadeFederativa.PB
        );

        Caverna caverna2 = criarCaverna(
                "Caverna 2",
                "CAD-002",
                UnidadeFederativa.PE
        );

        entityManager.getTransaction().begin();

        repository.salvar(caverna1);
        repository.salvar(caverna2);

        entityManager.getTransaction().commit();

        List<Caverna> cavernas =
                repository.listarTodos();

        assertEquals(2, cavernas.size());

        assertTrue(
                cavernas.stream()
                        .anyMatch(
                                c -> c.getCodCadastroAmbiental()
                                        .equals("CAD-001")
                        )
        );

        assertTrue(
                cavernas.stream()
                        .anyMatch(
                                c -> c.getCodCadastroAmbiental()
                                        .equals("CAD-002")
                        )
        );
    }

    @Test
    void deveBuscarCavernaPorCodCadastroAmbiental() {

        Caverna caverna = criarCaverna(
                "Caverna Código",
                "CAD-123",
                UnidadeFederativa.PB
        );

        entityManager.getTransaction().begin();

        repository.salvar(caverna);

        entityManager.getTransaction().commit();

        Caverna resultado =
                repository.buscarPorCodCadastroAmbiental(
                        "CAD-123"
                );

        assertNotNull(resultado);

        assertEquals(
                caverna.getId(),
                resultado.getId()
        );

        assertEquals(
                "CAD-123",
                resultado.getCodCadastroAmbiental()
        );
    }

    @Test
    void deveRetornarNullQuandoCodCadastroAmbientalNaoExiste() {

        Caverna resultado =
                repository.buscarPorCodCadastroAmbiental(
                        "CODIGO-INEXISTENTE"
                );

        assertNull(resultado);
    }

    @Test
    void deveListarCavernasPorUf() {

        Caverna cavernaPB1 = criarCaverna(
                "Caverna PB 1",
                "CAD-PB-001",
                UnidadeFederativa.PB
        );

        Caverna cavernaPB2 = criarCaverna(
                "Caverna PB 2",
                "CAD-PB-002",
                UnidadeFederativa.PB
        );

        Caverna cavernaPE = criarCaverna(
                "Caverna PE",
                "CAD-PE-001",
                UnidadeFederativa.PE
        );

        entityManager.getTransaction().begin();

        repository.salvar(cavernaPB1);
        repository.salvar(cavernaPB2);
        repository.salvar(cavernaPE);

        entityManager.getTransaction().commit();

        List<Caverna> cavernas =
                repository.listarPorUf(
                        UnidadeFederativa.PB
                );

        assertEquals(2, cavernas.size());

        assertTrue(
                cavernas.stream()
                        .allMatch(
                                c -> c.getUf()
                                        == UnidadeFederativa.PB
                        )
        );
    }

    @Test
    void deveRetornarListaVaziaParaUfSemCavernas() {

        Caverna caverna = criarCaverna(
                "Caverna PB",
                "CAD-PB-001",
                UnidadeFederativa.PB
        );

        entityManager.getTransaction().begin();

        repository.salvar(caverna);

        entityManager.getTransaction().commit();

        List<Caverna> cavernas =
                repository.listarPorUf(
                        UnidadeFederativa.PE
                );

        assertTrue(cavernas.isEmpty());
    }

    @Test
    void deveListarCavernasComAcessoPermitido() {

        Caverna permitida1 = criarCaverna(
                "Caverna Permitida 1",
                "CAD-PER-001",
                UnidadeFederativa.PB
        );

        Caverna permitida2 = criarCaverna(
                "Caverna Permitida 2",
                "CAD-PER-002",
                UnidadeFederativa.PE
        );

        Caverna bloqueada = criarCaverna(
                "Caverna Bloqueada",
                "CAD-BLOQ-001",
                UnidadeFederativa.PB
        );

        permitida1.setAcessoAtualmentePermitido(true);
        permitida2.setAcessoAtualmentePermitido(true);
        bloqueada.setAcessoAtualmentePermitido(false);

        entityManager.getTransaction().begin();

        repository.salvar(permitida1);
        repository.salvar(permitida2);
        repository.salvar(bloqueada);

        entityManager.getTransaction().commit();

        List<Caverna> cavernas =
                repository.listarComAcessoPermitido();

        assertEquals(2, cavernas.size());

        assertTrue(
                cavernas.stream()
                        .allMatch(
                                c -> Boolean.TRUE.equals(
                                        c.getAcessoAtualmentePermitido()
                                )
                        )
        );
    }

    @Test
    void deveRetornarListaVaziaQuandoNenhumaCavernaTemAcessoPermitido() {

        Caverna caverna1 = criarCaverna(
                "Caverna Bloqueada 1",
                "CAD-BLOQ-001",
                UnidadeFederativa.PB
        );

        Caverna caverna2 = criarCaverna(
                "Caverna Bloqueada 2",
                "CAD-BLOQ-002",
                UnidadeFederativa.PB
        );

        caverna1.setAcessoAtualmentePermitido(false);
        caverna2.setAcessoAtualmentePermitido(false);

        entityManager.getTransaction().begin();

        repository.salvar(caverna1);
        repository.salvar(caverna2);

        entityManager.getTransaction().commit();

        List<Caverna> cavernas =
                repository.listarComAcessoPermitido();

        assertTrue(cavernas.isEmpty());
    }

    @Test
    void deveAtualizarCaverna() {

        Caverna caverna = criarCaverna(
                "Nome Antigo",
                "CAD-ATUALIZAR",
                UnidadeFederativa.PB
        );

        entityManager.getTransaction().begin();

        repository.salvar(caverna);

        entityManager.getTransaction().commit();

        Long id = caverna.getId();

        caverna.setNomeOficial("Nome Novo");
        caverna.setMunicipio("Recife");
        caverna.setUf(UnidadeFederativa.PE);
        caverna.setAcessoAtualmentePermitido(true);

        entityManager.getTransaction().begin();

        repository.atualizar(caverna);

        entityManager.getTransaction().commit();

        entityManager.clear();

        Caverna atualizada =
                repository.buscarPorId(id);

        assertNotNull(atualizada);

        assertEquals(
                "Nome Novo",
                atualizada.getNomeOficial()
        );

        assertEquals(
                "Recife",
                atualizada.getMunicipio()
        );

        assertEquals(
                UnidadeFederativa.PE,
                atualizada.getUf()
        );

        assertTrue(
                atualizada.getAcessoAtualmentePermitido()
        );
    }

    @Test
    void deveRemoverCavernaPorId() {

        Caverna caverna = criarCaverna(
                "Caverna Remover",
                "CAD-REMOVER",
                UnidadeFederativa.PB
        );

        entityManager.getTransaction().begin();

        repository.salvar(caverna);

        entityManager.getTransaction().commit();

        Long id = caverna.getId();

        entityManager.getTransaction().begin();

        repository.removerPorId(id);

        entityManager.getTransaction().commit();

        Caverna resultado =
                repository.buscarPorId(id);

        assertNull(resultado);
    }

    @Test
    void deveIgnorarRemocaoDeCavernaInexistente() {

        assertDoesNotThrow(() -> {

            entityManager.getTransaction().begin();

            repository.removerPorId(999999L);

            entityManager.getTransaction().commit();
        });
    }

    private Caverna criarCaverna(
            String nome,
            String codigo,
            UnidadeFederativa uf
    ) {

        return Caverna.builder()
                .nomeOficial(nome)
                .codCadastroAmbiental(codigo)
                .municipio("João Pessoa")
                .uf(uf)
                .coordenadas(
                        new Localizacao(
                                new BigDecimal("222.222"),
                                new BigDecimal("333.333"),
                                "datum_geodesico"
                        )
                )
                .build();
    }
}
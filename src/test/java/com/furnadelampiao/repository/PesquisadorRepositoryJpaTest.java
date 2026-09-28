package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Pesquisador;
import com.furnadelampiao.enums.Titulacao;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PesquisadorRepositoryJpaTest {

    private static EntityManagerFactory emf;

    private EntityManager entityManager;
    private PesquisadorRepositoryJpa repository;

    @BeforeAll
    static void iniciarBanco() {
        emf = Persistence.createEntityManagerFactory("furnaTestPU");
    }

    @BeforeEach
    void iniciarTeste() {
        entityManager = emf.createEntityManager();
        repository = new PesquisadorRepositoryJpa(entityManager);

        entityManager.getTransaction().begin();

        entityManager.createQuery("DELETE FROM Pesquisador").executeUpdate();
        entityManager.createQuery("DELETE FROM Pessoa").executeUpdate();

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
    void deveSalvarPesquisador() {
        Pesquisador pesquisador = criarPesquisador(
                "Pesquisador 1",
                "11111111111",
                "pesquisador1@email.com",
                "REG-001",
                "Biologia",
                Titulacao.values()[0]
        );

        entityManager.getTransaction().begin();

        repository.salvar(pesquisador);

        entityManager.getTransaction().commit();

        assertNotNull(pesquisador.getId());

        Pesquisador salvo = entityManager.find(
                Pesquisador.class,
                pesquisador.getId()
        );

        assertNotNull(salvo);
        assertEquals("Pesquisador 1", salvo.getNome());
        assertEquals("11111111111", salvo.getCpf());
        assertEquals("REG-001", salvo.getNumRegistroInstitucional());
        assertEquals("Biologia", salvo.getAreaPrincipalPesquisa());
    }

    @Test
    void deveBuscarPesquisadorPorId() {
        Pesquisador pesquisador = criarPesquisador(
                "Pesquisador 2",
                "22222222222",
                "pesquisador2@email.com",
                "REG-002",
                "Geologia",
                Titulacao.values()[0]
        );

        entityManager.getTransaction().begin();

        repository.salvar(pesquisador);

        entityManager.getTransaction().commit();

        Pesquisador resultado =
                repository.buscarPorId(pesquisador.getId());

        assertNotNull(resultado);
        assertEquals(
                pesquisador.getId(),
                resultado.getId()
        );
        assertEquals(
                "Pesquisador 2",
                resultado.getNome()
        );
        assertEquals(
                "REG-002",
                resultado.getNumRegistroInstitucional()
        );
    }

    @Test
    void deveRetornarNullAoBuscarPesquisadorInexistente() {
        Pesquisador resultado =
                repository.buscarPorId(999999L);

        assertNull(resultado);
    }

    @Test
    void deveListarTodosOsPesquisadores() {
        Pesquisador pesquisador1 = criarPesquisador(
                "Pesquisador 3",
                "33333333333",
                "pesquisador3@email.com",
                "REG-003",
                "Biologia",
                Titulacao.values()[0]
        );

        Pesquisador pesquisador2 = criarPesquisador(
                "Pesquisador 4",
                "44444444444",
                "pesquisador4@email.com",
                "REG-004",
                "Geologia",
                Titulacao.values()[1]
        );

        entityManager.getTransaction().begin();

        repository.salvar(pesquisador1);
        repository.salvar(pesquisador2);

        entityManager.getTransaction().commit();

        List<Pesquisador> pesquisadores =
                repository.listarTodos();

        assertEquals(2, pesquisadores.size());

        assertTrue(
                pesquisadores.stream()
                        .anyMatch(
                                p -> p.getNumRegistroInstitucional()
                                        .equals("REG-003")
                        )
        );

        assertTrue(
                pesquisadores.stream()
                        .anyMatch(
                                p -> p.getNumRegistroInstitucional()
                                        .equals("REG-004")
                        )
        );
    }

    @Test
    void deveBuscarPesquisadoresPorTitulacao() {
        Titulacao titulacao = Titulacao.values()[0];

        Pesquisador pesquisador1 = criarPesquisador(
                "Pesquisador 5",
                "55555555555",
                "pesquisador5@email.com",
                "REG-005",
                "Biologia",
                titulacao
        );

        Pesquisador pesquisador2 = criarPesquisador(
                "Pesquisador 6",
                "66666666666",
                "pesquisador6@email.com",
                "REG-006",
                "Geologia",
                titulacao
        );

        Pesquisador pesquisador3 = criarPesquisador(
                "Pesquisador 7",
                "77777777777",
                "pesquisador7@email.com",
                "REG-007",
                "Arqueologia",
                Titulacao.values()[1]
        );

        entityManager.getTransaction().begin();

        repository.salvar(pesquisador1);
        repository.salvar(pesquisador2);
        repository.salvar(pesquisador3);

        entityManager.getTransaction().commit();

        List<Pesquisador> pesquisadores =
                repository.buscarPorTitulacao(titulacao);

        assertEquals(2, pesquisadores.size());

        assertTrue(
                pesquisadores.stream()
                        .allMatch(
                                p -> p.getTitulacao() == titulacao
                        )
        );
    }

    @Test
    void deveRetornarListaVaziaParaTitulacaoSemPesquisadores() {
        Pesquisador pesquisador = criarPesquisador(
                "Pesquisador 8",
                "88888888888",
                "pesquisador8@email.com",
                "REG-008",
                "Biologia",
                Titulacao.values()[0]
        );

        entityManager.getTransaction().begin();

        repository.salvar(pesquisador);

        entityManager.getTransaction().commit();

        List<Pesquisador> pesquisadores =
                repository.buscarPorTitulacao(
                        Titulacao.values()[1]
                );

        assertTrue(pesquisadores.isEmpty());
    }

    @Test
    void deveBuscarPesquisadoresPorAreaPesquisa() {
        Pesquisador pesquisador1 = criarPesquisador(
                "Pesquisador 9",
                "99999999999",
                "pesquisador9@email.com",
                "REG-009",
                "Biologia",
                Titulacao.values()[0]
        );

        Pesquisador pesquisador2 = criarPesquisador(
                "Pesquisador 10",
                "10101010101",
                "pesquisador10@email.com",
                "REG-010",
                "Biologia",
                Titulacao.values()[1]
        );

        Pesquisador pesquisador3 = criarPesquisador(
                "Pesquisador 11",
                "12121212121",
                "pesquisador11@email.com",
                "REG-011",
                "Geologia",
                Titulacao.values()[0]
        );

        entityManager.getTransaction().begin();

        repository.salvar(pesquisador1);
        repository.salvar(pesquisador2);
        repository.salvar(pesquisador3);

        entityManager.getTransaction().commit();

        List<Pesquisador> pesquisadores =
                repository.buscarPorAreaPesquisa("Biologia");

        assertEquals(2, pesquisadores.size());

        assertTrue(
                pesquisadores.stream()
                        .allMatch(
                                p -> p.getAreaPrincipalPesquisa()
                                        .equals("Biologia")
                        )
        );
    }

    @Test
    void deveRetornarListaVaziaParaAreaSemPesquisadores() {
        Pesquisador pesquisador = criarPesquisador(
                "Pesquisador 12",
                "13131313131",
                "pesquisador12@email.com",
                "REG-012",
                "Biologia",
                Titulacao.values()[0]
        );

        entityManager.getTransaction().begin();

        repository.salvar(pesquisador);

        entityManager.getTransaction().commit();

        List<Pesquisador> pesquisadores =
                repository.buscarPorAreaPesquisa("Astronomia");

        assertTrue(pesquisadores.isEmpty());
    }

    @Test
    void deveAtualizarPesquisador() {
        Pesquisador pesquisador = criarPesquisador(
                "Nome Antigo",
                "14141414141",
                "pesquisador14@email.com",
                "REG-014",
                "Biologia",
                Titulacao.values()[0]
        );

        entityManager.getTransaction().begin();

        repository.salvar(pesquisador);

        entityManager.getTransaction().commit();

        Long id = pesquisador.getId();

        pesquisador.setNome("Nome Novo");
        pesquisador.setAreaPrincipalPesquisa("Geologia");
        pesquisador.setNumRegistroInstitucional("REG-014-NOVO");
        pesquisador.setValorDiarioBolsa(
                new BigDecimal("250.00")
        );

        entityManager.getTransaction().begin();

        repository.atualizar(pesquisador);

        entityManager.getTransaction().commit();

        entityManager.clear();

        Pesquisador atualizado =
                repository.buscarPorId(id);

        assertNotNull(atualizado);
        assertEquals("Nome Novo", atualizado.getNome());
        assertEquals(
                "Geologia",
                atualizado.getAreaPrincipalPesquisa()
        );
        assertEquals(
                "REG-014-NOVO",
                atualizado.getNumRegistroInstitucional()
        );
        assertEquals(
                new BigDecimal("250.00"),
                atualizado.getValorDiarioBolsa()
        );
    }

    @Test
    void deveRemoverPesquisadorPorId() {
        Pesquisador pesquisador = criarPesquisador(
                "Pesquisador Remover",
                "15151515151",
                "pesquisador15@email.com",
                "REG-015",
                "Biologia",
                Titulacao.values()[0]
        );

        entityManager.getTransaction().begin();

        repository.salvar(pesquisador);

        entityManager.getTransaction().commit();

        Long id = pesquisador.getId();

        entityManager.getTransaction().begin();

        repository.removerPorId(id);

        entityManager.getTransaction().commit();

        Pesquisador resultado =
                repository.buscarPorId(id);

        assertNull(resultado);
    }

    @Test
    void deveIgnorarRemocaoDePesquisadorInexistente() {
        assertDoesNotThrow(() -> {
            entityManager.getTransaction().begin();

            repository.removerPorId(999999L);

            entityManager.getTransaction().commit();
        });
    }

    private Pesquisador criarPesquisador(
            String nome,
            String cpf,
            String email,
            String registro,
            String area,
            Titulacao titulacao
    ) {
        return Pesquisador.builder()
                .nome(nome)
                .cpf(cpf)
                .dataNasc(LocalDate.of(1990, 5, 15))
                .email(email)
                .telefone("83988888888")
                .situacaoAtiva(true)
                .numRegistroInstitucional(registro)
                .areaPrincipalPesquisa(area)
                .titulacao(titulacao)
                .valorDiarioBolsa(new BigDecimal("200.00"))
                .build();
    }
}
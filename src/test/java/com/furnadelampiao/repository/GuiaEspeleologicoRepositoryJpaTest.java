package com.furnadelampiao.repository;

import com.furnadelampiao.domain.GuiaEspeleologico;
import com.furnadelampiao.enums.NivelCertificacao;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuiaEspeleologicoRepositoryJpaTest {

    private static EntityManagerFactory emf;

    private EntityManager entityManager;
    private GuiaEspeleologicoRepositoryJpa repository;

    @BeforeAll
    static void iniciarBanco() {
        emf = Persistence.createEntityManagerFactory("furnaTestPU");
    }

    @BeforeEach
    void iniciarTeste() {
        entityManager = emf.createEntityManager();
        repository = new GuiaEspeleologicoRepositoryJpa(entityManager);

        entityManager.getTransaction().begin();

        entityManager.createQuery("DELETE FROM GuiaEspeleologico").executeUpdate();
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
    void deveSalvarGuiaEspeleologico() {
        GuiaEspeleologico guia = criarGuia(
                "Guia 1",
                "11111111111",
                "guia1@email.com",
                "CRE-001",
                NivelCertificacao.values()[0],
                LocalDate.now().plusYears(1)
        );

        entityManager.getTransaction().begin();

        repository.salvar(guia);

        entityManager.getTransaction().commit();

        assertNotNull(guia.getId());

        GuiaEspeleologico salvo =
                entityManager.find(
                        GuiaEspeleologico.class,
                        guia.getId()
                );

        assertNotNull(salvo);
        assertEquals("Guia 1", salvo.getNome());
        assertEquals("11111111111", salvo.getCpf());
        assertEquals("CRE-001", salvo.getNumCredenciamento());
        assertEquals(
                NivelCertificacao.values()[0],
                salvo.getNivelCertificacao()
        );
    }

    @Test
    void deveBuscarGuiaPorId() {
        GuiaEspeleologico guia = criarGuia(
                "Guia 2",
                "22222222222",
                "guia2@email.com",
                "CRE-002",
                NivelCertificacao.values()[0],
                LocalDate.now().plusYears(1)
        );

        entityManager.getTransaction().begin();

        repository.salvar(guia);

        entityManager.getTransaction().commit();

        GuiaEspeleologico resultado =
                repository.buscarPorId(guia.getId());

        assertNotNull(resultado);
        assertEquals(guia.getId(), resultado.getId());
        assertEquals("Guia 2", resultado.getNome());
        assertEquals("CRE-002", resultado.getNumCredenciamento());
    }

    @Test
    void deveRetornarNullAoBuscarGuiaInexistente() {
        GuiaEspeleologico resultado =
                repository.buscarPorId(999999L);

        assertNull(resultado);
    }

    @Test
    void deveListarTodosOsGuias() {
        GuiaEspeleologico guia1 = criarGuia(
                "Guia 3",
                "33333333333",
                "guia3@email.com",
                "CRE-003",
                NivelCertificacao.values()[0],
                LocalDate.now().plusYears(1)
        );

        GuiaEspeleologico guia2 = criarGuia(
                "Guia 4",
                "44444444444",
                "guia4@email.com",
                "CRE-004",
                NivelCertificacao.values()[1],
                LocalDate.now().plusYears(2)
        );

        entityManager.getTransaction().begin();

        repository.salvar(guia1);
        repository.salvar(guia2);

        entityManager.getTransaction().commit();

        List<GuiaEspeleologico> guias =
                repository.listarTodos();

        assertEquals(2, guias.size());

        assertTrue(
                guias.stream()
                        .anyMatch(
                                g -> g.getNumCredenciamento()
                                        .equals("CRE-003")
                        )
        );

        assertTrue(
                guias.stream()
                        .anyMatch(
                                g -> g.getNumCredenciamento()
                                        .equals("CRE-004")
                        )
        );
    }

    @Test
    void deveBuscarGuiasPorNivelCertificacao() {
        NivelCertificacao nivel =
                NivelCertificacao.values()[0];

        GuiaEspeleologico guia1 = criarGuia(
                "Guia 5",
                "55555555555",
                "guia5@email.com",
                "CRE-005",
                nivel,
                LocalDate.now().plusYears(1)
        );

        GuiaEspeleologico guia2 = criarGuia(
                "Guia 6",
                "66666666666",
                "guia6@email.com",
                "CRE-006",
                nivel,
                LocalDate.now().plusYears(2)
        );

        GuiaEspeleologico guia3 = criarGuia(
                "Guia 7",
                "77777777777",
                "guia7@email.com",
                "CRE-007",
                NivelCertificacao.values()[1],
                LocalDate.now().plusYears(1)
        );

        entityManager.getTransaction().begin();

        repository.salvar(guia1);
        repository.salvar(guia2);
        repository.salvar(guia3);

        entityManager.getTransaction().commit();

        List<GuiaEspeleologico> guias =
                repository.buscarPorNivelCertificacao(nivel);

        assertEquals(2, guias.size());

        assertTrue(
                guias.stream()
                        .allMatch(
                                g -> g.getNivelCertificacao() == nivel
                        )
        );
    }

    @Test
    void deveRetornarListaVaziaParaNivelSemGuias() {
        GuiaEspeleologico guia = criarGuia(
                "Guia 8",
                "88888888888",
                "guia8@email.com",
                "CRE-008",
                NivelCertificacao.values()[0],
                LocalDate.now().plusYears(1)
        );

        entityManager.getTransaction().begin();

        repository.salvar(guia);

        entityManager.getTransaction().commit();

        List<GuiaEspeleologico> guias =
                repository.buscarPorNivelCertificacao(
                        NivelCertificacao.values()[1]
                );

        assertTrue(guias.isEmpty());
    }

    @Test
    void deveListarCertificacoesVencidas() {
        GuiaEspeleologico vencido1 = criarGuia(
                "Guia 9",
                "99999999999",
                "guia9@email.com",
                "CRE-009",
                NivelCertificacao.values()[0],
                LocalDate.now().minusDays(10)
        );

        GuiaEspeleologico vencido2 = criarGuia(
                "Guia 10",
                "10101010101",
                "guia10@email.com",
                "CRE-010",
                NivelCertificacao.values()[1],
                LocalDate.now().minusYears(1)
        );

        GuiaEspeleologico vigente = criarGuia(
                "Guia 11",
                "11111111112",
                "guia11@email.com",
                "CRE-011",
                NivelCertificacao.values()[0],
                LocalDate.now().plusYears(1)
        );

        entityManager.getTransaction().begin();

        repository.salvar(vencido1);
        repository.salvar(vencido2);
        repository.salvar(vigente);

        entityManager.getTransaction().commit();

        List<GuiaEspeleologico> guias =
                repository.listarCertificacoesVencidas();

        assertEquals(2, guias.size());

        assertTrue(
                guias.stream()
                        .allMatch(
                                g -> g.getDataValidadeCertificacao()
                                        .isBefore(LocalDate.now())
                        )
        );
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistemCertificacoesVencidas() {
        GuiaEspeleologico guia = criarGuia(
                "Guia 12",
                "12121212121",
                "guia12@email.com",
                "CRE-012",
                NivelCertificacao.values()[0],
                LocalDate.now().plusYears(1)
        );

        entityManager.getTransaction().begin();

        repository.salvar(guia);

        entityManager.getTransaction().commit();

        List<GuiaEspeleologico> guias =
                repository.listarCertificacoesVencidas();

        assertTrue(guias.isEmpty());
    }

    @Test
    void deveAtualizarGuiaEspeleologico() {
        GuiaEspeleologico guia = criarGuia(
                "Nome Antigo",
                "13131313131",
                "guia13@email.com",
                "CRE-013",
                NivelCertificacao.values()[0],
                LocalDate.now().plusYears(1)
        );

        entityManager.getTransaction().begin();

        repository.salvar(guia);

        entityManager.getTransaction().commit();

        Long id = guia.getId();

        guia.setNome("Nome Novo");
        guia.setNumCredenciamento("CRE-013-NOVO");
        guia.setDataValidadeCertificacao(
                LocalDate.now().plusYears(3)
        );
        guia.setQtdExpedicoesConcluidas(15);

        entityManager.getTransaction().begin();

        repository.atualizar(guia);

        entityManager.getTransaction().commit();

        entityManager.clear();

        GuiaEspeleologico atualizado =
                repository.buscarPorId(id);

        assertNotNull(atualizado);
        assertEquals("Nome Novo", atualizado.getNome());
        assertEquals(
                "CRE-013-NOVO",
                atualizado.getNumCredenciamento()
        );
        assertEquals(
                15,
                atualizado.getQtdExpedicoesConcluidas()
        );
        assertEquals(
                LocalDate.now().plusYears(3),
                atualizado.getDataValidadeCertificacao()
        );
    }

    @Test
    void deveRemoverGuiaPorId() {
        GuiaEspeleologico guia = criarGuia(
                "Guia Remover",
                "14141414141",
                "guia14@email.com",
                "CRE-014",
                NivelCertificacao.values()[0],
                LocalDate.now().plusYears(1)
        );

        entityManager.getTransaction().begin();

        repository.salvar(guia);

        entityManager.getTransaction().commit();

        Long id = guia.getId();

        entityManager.getTransaction().begin();

        repository.removerPorId(id);

        entityManager.getTransaction().commit();

        GuiaEspeleologico resultado =
                repository.buscarPorId(id);

        assertNull(resultado);
    }

    @Test
    void deveIgnorarRemocaoDeGuiaInexistente() {
        assertDoesNotThrow(() -> {
            entityManager.getTransaction().begin();

            repository.removerPorId(999999L);

            entityManager.getTransaction().commit();
        });
    }

    private GuiaEspeleologico criarGuia(
            String nome,
            String cpf,
            String email,
            String credenciamento,
            NivelCertificacao nivel,
            LocalDate validade
    ) {
        return GuiaEspeleologico.builder()
                .nome(nome)
                .cpf(cpf)
                .dataNasc(LocalDate.of(1990, 5, 15))
                .email(email)
                .telefone("83988888888")
                .situacaoAtiva(true)
                .numCredenciamento(credenciamento)
                .nivelCertificacao(nivel)
                .dataValidadeCertificacao(validade)
                .qtdExpedicoesConcluidas(5)
                .build();
    }
}
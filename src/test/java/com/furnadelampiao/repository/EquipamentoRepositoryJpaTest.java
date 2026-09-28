package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Equipamento;
import com.furnadelampiao.enums.SituacaoOperacional;
import com.furnadelampiao.enums.TipoEquipamento;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EquipamentoRepositoryJpaTest {

    private static EntityManagerFactory emf;

    private EntityManager entityManager;
    private EquipamentoRepositoryJpa repository;

    @BeforeAll
    static void iniciarBanco() {
        emf = Persistence.createEntityManagerFactory("furnaTestPU");
    }

    @BeforeEach
    void iniciarTeste() {
        entityManager = emf.createEntityManager();
        repository = new EquipamentoRepositoryJpa(entityManager);

        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM Equipamento").executeUpdate();
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
    void deveSalvarEquipamento() {
        Equipamento equipamento = criarEquipamento(
                "PAT-001",
                "Equipamento 1",
                TipoEquipamento.values()[0],
                SituacaoOperacional.values()[0]
        );

        entityManager.getTransaction().begin();

        repository.salvar(equipamento);

        entityManager.getTransaction().commit();

        assertNotNull(equipamento.getId());

        Equipamento salvo = entityManager.find(
                Equipamento.class,
                equipamento.getId()
        );

        assertNotNull(salvo);
        assertEquals("PAT-001", salvo.getCodPatrimonial());
        assertEquals("Equipamento 1", salvo.getNome());
    }

    @Test
    void deveBuscarEquipamentoPorId() {
        Equipamento equipamento = criarEquipamento(
                "PAT-002",
                "Equipamento 2",
                TipoEquipamento.values()[0],
                SituacaoOperacional.values()[0]
        );

        entityManager.getTransaction().begin();
        repository.salvar(equipamento);
        entityManager.getTransaction().commit();

        Equipamento resultado = repository.buscarPorId(equipamento.getId());

        assertNotNull(resultado);
        assertEquals(equipamento.getId(), resultado.getId());
        assertEquals("PAT-002", resultado.getCodPatrimonial());
    }

    @Test
    void deveRetornarNullAoBuscarEquipamentoInexistente() {
        Equipamento resultado = repository.buscarPorId(999999L);

        assertNull(resultado);
    }

    @Test
    void deveListarTodosOsEquipamentos() {
        Equipamento equipamento1 = criarEquipamento(
                "PAT-003",
                "Equipamento 3",
                TipoEquipamento.values()[0],
                SituacaoOperacional.values()[0]
        );

        Equipamento equipamento2 = criarEquipamento(
                "PAT-004",
                "Equipamento 4",
                TipoEquipamento.values()[1],
                SituacaoOperacional.values()[1]
        );

        entityManager.getTransaction().begin();

        repository.salvar(equipamento1);
        repository.salvar(equipamento2);

        entityManager.getTransaction().commit();

        List<Equipamento> equipamentos = repository.listarTodos();

        assertEquals(2, equipamentos.size());

        assertTrue(
                equipamentos.stream()
                        .anyMatch(e -> e.getCodPatrimonial().equals("PAT-003"))
        );

        assertTrue(
                equipamentos.stream()
                        .anyMatch(e -> e.getCodPatrimonial().equals("PAT-004"))
        );
    }

    @Test
    void deveBuscarEquipamentoPorCodPatrimonial() {
        Equipamento equipamento = criarEquipamento(
                "PAT-005",
                "Equipamento 5",
                TipoEquipamento.values()[0],
                SituacaoOperacional.values()[0]
        );

        entityManager.getTransaction().begin();
        repository.salvar(equipamento);
        entityManager.getTransaction().commit();

        Equipamento resultado =
                repository.buscarPorCodPatrimonial("PAT-005");

        assertNotNull(resultado);
        assertEquals(equipamento.getId(), resultado.getId());
        assertEquals(
                "PAT-005",
                resultado.getCodPatrimonial()
        );
    }

    @Test
    void deveRetornarNullQuandoCodPatrimonialNaoExiste() {
        Equipamento resultado =
                repository.buscarPorCodPatrimonial("PAT-INEXISTENTE");

        assertNull(resultado);
    }

    @Test
    void deveListarEquipamentosPorTipo() {
        TipoEquipamento tipo = TipoEquipamento.values()[0];

        Equipamento equipamento1 = criarEquipamento(
                "PAT-006",
                "Equipamento 6",
                tipo,
                SituacaoOperacional.values()[0]
        );

        Equipamento equipamento2 = criarEquipamento(
                "PAT-007",
                "Equipamento 7",
                tipo,
                SituacaoOperacional.values()[1]
        );

        Equipamento equipamento3 = criarEquipamento(
                "PAT-008",
                "Equipamento 8",
                TipoEquipamento.values()[1],
                SituacaoOperacional.values()[0]
        );

        entityManager.getTransaction().begin();

        repository.salvar(equipamento1);
        repository.salvar(equipamento2);
        repository.salvar(equipamento3);

        entityManager.getTransaction().commit();

        List<Equipamento> equipamentos =
                repository.listarPorTipo(tipo);

        assertEquals(2, equipamentos.size());

        assertTrue(
                equipamentos.stream()
                        .allMatch(e -> e.getTipo() == tipo)
        );
    }

    @Test
    void deveRetornarListaVaziaParaTipoSemEquipamentos() {
        Equipamento equipamento = criarEquipamento(
                "PAT-009",
                "Equipamento 9",
                TipoEquipamento.values()[0],
                SituacaoOperacional.values()[0]
        );

        entityManager.getTransaction().begin();
        repository.salvar(equipamento);
        entityManager.getTransaction().commit();

        List<Equipamento> equipamentos =
                repository.listarPorTipo(
                        TipoEquipamento.values()[1]
                );

        assertTrue(equipamentos.isEmpty());
    }

    @Test
    void deveListarEquipamentosPorSituacaoOperacional() {
        SituacaoOperacional situacao = SituacaoOperacional.values()[0];

        Equipamento equipamento1 = criarEquipamento(
                "PAT-010",
                "Equipamento 10",
                TipoEquipamento.values()[0],
                situacao
        );

        Equipamento equipamento2 = criarEquipamento(
                "PAT-011",
                "Equipamento 11",
                TipoEquipamento.values()[1],
                situacao
        );

        Equipamento equipamento3 = criarEquipamento(
                "PAT-012",
                "Equipamento 12",
                TipoEquipamento.values()[0],
                SituacaoOperacional.values()[1]
        );

        entityManager.getTransaction().begin();

        repository.salvar(equipamento1);
        repository.salvar(equipamento2);
        repository.salvar(equipamento3);

        entityManager.getTransaction().commit();

        List<Equipamento> equipamentos =
                repository.listarPorSituacaoOperacional(situacao);

        assertEquals(2, equipamentos.size());

        assertTrue(
                equipamentos.stream()
                        .allMatch(
                                e -> e.getSituacaoOperacional() == situacao
                        )
        );
    }

    @Test
    void deveRetornarListaVaziaParaSituacaoSemEquipamentos() {
        Equipamento equipamento = criarEquipamento(
                "PAT-013",
                "Equipamento 13",
                TipoEquipamento.values()[0],
                SituacaoOperacional.values()[0]
        );

        entityManager.getTransaction().begin();
        repository.salvar(equipamento);
        entityManager.getTransaction().commit();

        List<Equipamento> equipamentos =
                repository.listarPorSituacaoOperacional(
                        SituacaoOperacional.values()[1]
                );

        assertTrue(equipamentos.isEmpty());
    }

    @Test
    void deveAtualizarEquipamento() {
        Equipamento equipamento = criarEquipamento(
                "PAT-014",
                "Nome Antigo",
                TipoEquipamento.values()[0],
                SituacaoOperacional.values()[0]
        );

        entityManager.getTransaction().begin();
        repository.salvar(equipamento);
        entityManager.getTransaction().commit();

        Long id = equipamento.getId();

        equipamento.setNome("Nome Novo");
        equipamento.setFabricante("Fabricante Novo");
        equipamento.setValorAquisicao(new BigDecimal("2500.00"));
        equipamento.setIndicacaoCalibracao(true);

        entityManager.getTransaction().begin();

        repository.atualizar(equipamento);

        entityManager.getTransaction().commit();

        entityManager.clear();

        Equipamento atualizado = repository.buscarPorId(id);

        assertNotNull(atualizado);
        assertEquals("Nome Novo", atualizado.getNome());
        assertEquals("Fabricante Novo", atualizado.getFabricante());
        assertEquals(
                new BigDecimal("2500.00"),
                atualizado.getValorAquisicao()
        );
        assertTrue(atualizado.getIndicacaoCalibracao());
    }

    @Test
    void deveRemoverEquipamentoPorId() {
        Equipamento equipamento = criarEquipamento(
                "PAT-015",
                "Equipamento Remover",
                TipoEquipamento.values()[0],
                SituacaoOperacional.values()[0]
        );

        entityManager.getTransaction().begin();
        repository.salvar(equipamento);
        entityManager.getTransaction().commit();

        Long id = equipamento.getId();

        entityManager.getTransaction().begin();

        repository.removerPorId(id);

        entityManager.getTransaction().commit();

        Equipamento resultado = repository.buscarPorId(id);

        assertNull(resultado);
    }

    @Test
    void deveIgnorarRemocaoDeEquipamentoInexistente() {
        assertDoesNotThrow(() -> {
            entityManager.getTransaction().begin();

            repository.removerPorId(999999L);

            entityManager.getTransaction().commit();
        });
    }

    private Equipamento criarEquipamento(
            String codPatrimonial,
            String nome,
            TipoEquipamento tipo,
            SituacaoOperacional situacao
    ) {
        return Equipamento.builder()
                .codPatrimonial(codPatrimonial)
                .nome(nome)
                .tipo(tipo)
                .fabricante("Fabricante Teste")
                .valorAquisicao(new BigDecimal("1500.00"))
                .dataCompra(LocalDate.of(2025, 1, 15))
                .dataUltimaManutencao(LocalDate.of(2025, 6, 10))
                .situacaoOperacional(situacao)
                .indicacaoCalibracao(false)
                .build();
    }
}
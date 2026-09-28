package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Endereco;
import com.furnadelampiao.domain.Pessoa;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PessoaRepositoryJpaTest {

    private static EntityManagerFactory emf;

    private EntityManager entityManager;
    private PessoaRepositoryJpa repository;

    @BeforeAll
    static void iniciarBanco() {
        emf = Persistence.createEntityManagerFactory("furnaTestPU");
    }

    @BeforeEach
    void iniciarTeste() {
        entityManager = emf.createEntityManager();
        repository = new PessoaRepositoryJpa(entityManager);

        entityManager.getTransaction().begin();
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
    void deveSalvarPessoa() {
        Pessoa pessoa = criarPessoa(
                "Pessoa 1",
                "11111111111",
                "pessoa1@email.com"
        );

        entityManager.getTransaction().begin();

        repository.salvar(pessoa);

        entityManager.getTransaction().commit();

        assertNotNull(pessoa.getId());

        Pessoa salva = entityManager.find(
                Pessoa.class,
                pessoa.getId()
        );

        assertNotNull(salva);
        assertEquals("Pessoa 1", salva.getNome());
        assertEquals("11111111111", salva.getCpf());
        assertEquals("pessoa1@email.com", salva.getEmail());
    }

    @Test
    void deveBuscarPessoaPorId() {
        Pessoa pessoa = criarPessoa(
                "Pessoa 2",
                "22222222222",
                "pessoa2@email.com"
        );

        entityManager.getTransaction().begin();
        repository.salvar(pessoa);
        entityManager.getTransaction().commit();

        Pessoa resultado = repository.buscarPorId(pessoa.getId());

        assertNotNull(resultado);
        assertEquals(pessoa.getId(), resultado.getId());
        assertEquals("Pessoa 2", resultado.getNome());
        assertEquals("22222222222", resultado.getCpf());
    }

    @Test
    void deveRetornarNullAoBuscarPessoaInexistente() {
        Pessoa resultado = repository.buscarPorId(999999L);

        assertNull(resultado);
    }

    @Test
    void deveListarTodasAsPessoas() {
        Pessoa pessoa1 = criarPessoa(
                "Pessoa 3",
                "33333333333",
                "pessoa3@email.com"
        );

        Pessoa pessoa2 = criarPessoa(
                "Pessoa 4",
                "44444444444",
                "pessoa4@email.com"
        );

        entityManager.getTransaction().begin();

        repository.salvar(pessoa1);
        repository.salvar(pessoa2);

        entityManager.getTransaction().commit();

        List<Pessoa> pessoas = repository.listarTodos();

        assertEquals(2, pessoas.size());

        assertTrue(
                pessoas.stream()
                        .anyMatch(
                                p -> p.getCpf().equals("33333333333")
                        )
        );

        assertTrue(
                pessoas.stream()
                        .anyMatch(
                                p -> p.getCpf().equals("44444444444")
                        )
        );
    }

    @Test
    void deveAtualizarPessoa() {
        Pessoa pessoa = criarPessoa(
                "Nome Antigo",
                "55555555555",
                "pessoa5@email.com"
        );

        entityManager.getTransaction().begin();
        repository.salvar(pessoa);
        entityManager.getTransaction().commit();

        Long id = pessoa.getId();

        pessoa.setNome("Nome Novo");
        pessoa.setTelefone("83999999999");
        pessoa.setSituacaoAtiva(false);

        entityManager.getTransaction().begin();

        repository.atualizar(pessoa);

        entityManager.getTransaction().commit();

        entityManager.clear();

        Pessoa atualizada = repository.buscarPorId(id);

        assertNotNull(atualizada);
        assertEquals("Nome Novo", atualizada.getNome());
        assertEquals("83999999999", atualizada.getTelefone());
        assertFalse(atualizada.getSituacaoAtiva());
    }

    @Test
    void deveRemoverPessoaPorId() {
        Pessoa pessoa = criarPessoa(
                "Pessoa Remover",
                "66666666666",
                "pessoa6@email.com"
        );

        entityManager.getTransaction().begin();
        repository.salvar(pessoa);
        entityManager.getTransaction().commit();

        Long id = pessoa.getId();

        entityManager.getTransaction().begin();

        repository.removerPorId(id);

        entityManager.getTransaction().commit();

        Pessoa resultado = repository.buscarPorId(id);

        assertNull(resultado);
    }

    @Test
    void deveIgnorarRemocaoDePessoaInexistente() {
        assertDoesNotThrow(() -> {
            entityManager.getTransaction().begin();

            repository.removerPorId(999999L);

            entityManager.getTransaction().commit();
        });
    }

    private Pessoa criarPessoa(
            String nome,
            String cpf,
            String email
    ) {
        return Pessoa.builder()
                .nome(nome)
                .cpf(cpf)
                .dataNasc(LocalDate.of(2000, 1, 15))
                .email(email)
                .telefone("83988888888")
                .situacaoAtiva(true)
                .build();
    }
}
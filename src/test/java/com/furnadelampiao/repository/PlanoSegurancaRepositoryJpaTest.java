package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Localizacao;
import com.furnadelampiao.domain.PlanoSeguranca;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanoSegurancaRepositoryJpaTest {

    @Test
    void deveSalvarEBuscarPorId() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("furnaTestPU");

        EntityManager entityManager = emf.createEntityManager();

        PlanoSegurancaRepositoryJpa repository =
                new PlanoSegurancaRepositoryJpa(entityManager);

        PlanoSeguranca plano = criarPlanoSeguranca();

        entityManager.getTransaction().begin();

        repository.salvar(plano);

        entityManager.getTransaction().commit();

        PlanoSeguranca resultado =
                repository.buscarPorId(plano.getId());

        assertNotNull(resultado);
        assertEquals(plano.getId(), resultado.getId());
        assertEquals(
                plano.getTempoMaxSemComunicacao(),
                resultado.getTempoMaxSemComunicacao()
        );
        assertEquals(
                plano.getTelefoneEmergencia(),
                resultado.getTelefoneEmergencia()
        );
        assertEquals(
                plano.getNecessidadeEquipeMedica(),
                resultado.getNecessidadeEquipeMedica()
        );

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRetornarNullAoBuscarPorIdInexistente() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("furnaTestPU");

        EntityManager entityManager = emf.createEntityManager();

        PlanoSegurancaRepositoryJpa repository =
                new PlanoSegurancaRepositoryJpa(entityManager);

        PlanoSeguranca resultado =
                repository.buscarPorId(999999L);

        assertNull(resultado);

        entityManager.close();
        emf.close();
    }

    @Test
    void deveListarTodos() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("furnaTestPU");

        EntityManager entityManager = emf.createEntityManager();

        PlanoSegurancaRepositoryJpa repository =
                new PlanoSegurancaRepositoryJpa(entityManager);

        PlanoSeguranca plano1 = criarPlanoSeguranca();
        PlanoSeguranca plano2 = criarPlanoSeguranca();

        entityManager.getTransaction().begin();

        repository.salvar(plano1);
        repository.salvar(plano2);

        entityManager.getTransaction().commit();

        List<PlanoSeguranca> resultado =
                repository.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(
                resultado.stream()
                        .anyMatch(plano -> plano.getId().equals(plano1.getId()))
        );
        assertTrue(
                resultado.stream()
                        .anyMatch(plano -> plano.getId().equals(plano2.getId()))
        );

        entityManager.close();
        emf.close();
    }

    @Test
    void deveAtualizar() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("furnaTestPU");

        EntityManager entityManager = emf.createEntityManager();

        PlanoSegurancaRepositoryJpa repository =
                new PlanoSegurancaRepositoryJpa(entityManager);

        PlanoSeguranca plano = criarPlanoSeguranca();

        entityManager.getTransaction().begin();

        repository.salvar(plano);

        entityManager.getTransaction().commit();

        plano.setTempoMaxSemComunicacao(60);
        plano.setTelefoneEmergencia("83888888888");
        plano.setNecessidadeEquipeMedica(true);

        entityManager.getTransaction().begin();

        repository.atualizar(plano);

        entityManager.getTransaction().commit();

        entityManager.clear();

        PlanoSeguranca resultado =
                repository.buscarPorId(plano.getId());

        assertNotNull(resultado);
        assertEquals(60, resultado.getTempoMaxSemComunicacao());
        assertEquals("83888888888", resultado.getTelefoneEmergencia());
        assertTrue(resultado.getNecessidadeEquipeMedica());

        entityManager.close();
        emf.close();
    }

    @Test
    void deveRemoverPorId() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("furnaTestPU");

        EntityManager entityManager = emf.createEntityManager();

        PlanoSegurancaRepositoryJpa repository =
                new PlanoSegurancaRepositoryJpa(entityManager);

        PlanoSeguranca plano = criarPlanoSeguranca();

        entityManager.getTransaction().begin();

        repository.salvar(plano);

        entityManager.getTransaction().commit();

        Long id = plano.getId();

        entityManager.getTransaction().begin();

        repository.removerPorId(id);

        entityManager.getTransaction().commit();

        PlanoSeguranca resultado =
                repository.buscarPorId(id);

        assertNull(resultado);

        entityManager.close();
        emf.close();
    }

    @Test
    void naoDeveFalharAoRemoverIdInexistente() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("furnaTestPU");

        EntityManager entityManager = emf.createEntityManager();

        PlanoSegurancaRepositoryJpa repository =
                new PlanoSegurancaRepositoryJpa(entityManager);

        entityManager.getTransaction().begin();

        repository.removerPorId(999999L);

        entityManager.getTransaction().commit();

        PlanoSeguranca resultado =
                repository.buscarPorId(999999L);

        assertNull(resultado);

        entityManager.close();
        emf.close();
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
}
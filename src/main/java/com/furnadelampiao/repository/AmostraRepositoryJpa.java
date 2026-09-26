package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Amostra;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class AmostraRepositoryJpa implements AmostraRepository {

    private final EntityManager entityManager;

    public AmostraRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void salvar(Amostra amostra) {
        entityManager.persist(amostra);
    }

    @Override
    public Amostra buscarPorId(Long id) {
        return entityManager.find(Amostra.class, id);
    }

    @Override
    public List<Amostra> listarTodos() {
        return entityManager.createQuery(
                "SELECT a FROM Amostra a",
                Amostra.class
        ).getResultList();
    }

    @Override
    public void atualizar(Amostra amostra) {
        entityManager.merge(amostra);
    }

    @Override
    public void removerPorId(Long id) {
        Amostra amostra = entityManager.find(Amostra.class, id);

        if (amostra != null) {
            entityManager.remove(amostra);
        }
    }

    @Override
    public List<Amostra> buscarPorColetaCientificaId(
            Long coletaCientificaId) {

        return entityManager.createQuery(
                        "SELECT a " +
                                "FROM Amostra a " +
                                "WHERE a.coletaCientifica.id = :coletaId",
                        Amostra.class
                )
                .setParameter("coletaId", coletaCientificaId)
                .getResultList();
    }

    @Override
    public Amostra buscarPorCodigoCampo(String codCampo) {

        try {
            return entityManager.createQuery(
                            "SELECT a " +
                                    "FROM Amostra a " +
                                    "WHERE a.codCampo = :codCampo",
                            Amostra.class
                    )
                    .setParameter("codCampo", codCampo)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }
}
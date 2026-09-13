package com.furnadelampiao.Repository;

import com.furnadelampiao.domain.Setor;
import com.furnadelampiao.enums.NivelDificuldadeSetor;

import javax.persistence.EntityManager;
import java.util.List;

public class SetorRepositoryJpa implements SetorRepository {

    private final EntityManager entityManager;

    public SetorRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void salvar(Setor setor) {
        entityManager.persist(setor);
    }

    @Override
    public Setor buscarPorId(Long id) {
        return entityManager.find(Setor.class, id);
    }

    @Override
    public List<Setor> listarTodos() {
        return entityManager
                .createQuery(
                        "SELECT s FROM Setor s",
                        Setor.class)
                .getResultList();
    }

    @Override
    public List<Setor> listarPorCaverna(Long cavernaId) {
        return entityManager
                .createQuery(
                        "SELECT s FROM Setor s " +
                                "WHERE s.caverna.id = :cavernaId",
                        Setor.class)
                .setParameter("cavernaId", cavernaId)
                .getResultList();
    }

    @Override
    public List<Setor> listarPorNivelDificuldade(NivelDificuldadeSetor nivel) {
        return entityManager
                .createQuery(
                        "SELECT s FROM Setor s " +
                                "WHERE s.nivelEstimadoDificuldade = :nivel",
                        Setor.class)
                .setParameter("nivel", nivel)
                .getResultList();
    }

    @Override
    public void atualizar(Setor setor) {
        entityManager.merge(setor);
    }

    @Override
    public void removerPorId(Long id) {
        Setor setor = entityManager.find(Setor.class, id);

        if (setor != null) {
            entityManager.remove(setor);
        }
    }
}
package com.furnadelampiao.repository;

import com.furnadelampiao.domain.PlanoSeguranca;

import javax.persistence.EntityManager;
import java.util.List;

public class PlanoSegurancaRepositoryJpa implements PlanoSegurancaRepository {

    private final EntityManager entityManager;

    public PlanoSegurancaRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void salvar(PlanoSeguranca planoSeguranca) {
        entityManager.persist(planoSeguranca);
    }

    @Override
    public PlanoSeguranca buscarPorId(Long id) {
        return entityManager.find(PlanoSeguranca.class, id);
    }

    @Override
    public List<PlanoSeguranca> listarTodos() {
        return entityManager
                .createQuery(
                        "SELECT p FROM PlanoSeguranca p",
                        PlanoSeguranca.class)
                .getResultList();
    }

    @Override
    public void atualizar(PlanoSeguranca planoSeguranca) {
        entityManager.merge(planoSeguranca);
    }

    @Override
    public void removerPorId(Long id) {
        PlanoSeguranca planoSeguranca =
                entityManager.find(PlanoSeguranca.class, id);

        if (planoSeguranca != null) {
            entityManager.remove(planoSeguranca);
        }
    }
}
package com.furnadelampiao.repository;

import com.furnadelampiao.domain.PlanoSeguranca;

import javax.persistence.EntityManager;
import java.util.List;
import javax.persistence.NoResultException;

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
        PlanoSeguranca planoSeguranca = entityManager.find(PlanoSeguranca.class, id);

        if (planoSeguranca != null) {
            entityManager.remove(planoSeguranca);
        }
    }

    @Override
    public byte[] buscarMapaRotaPorId(Long id) {
        try {
            return entityManager
                    .createQuery(
                            "SELECT p.mapaRota FROM PlanoSeguranca p WHERE p.id = :id",
                            byte[].class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
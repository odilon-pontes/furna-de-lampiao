package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.enums.UnidadeFederativa;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class CavernaRepositoryJpa implements CavernaRepository {

    private final EntityManager entityManager;

    public CavernaRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void salvar(Caverna caverna) {
        entityManager.persist(caverna);
    }

    @Override
    public Caverna buscarPorId(Long id) {
        return entityManager.find(Caverna.class, id);
    }

    @Override
    public List<Caverna> listarTodos() {
        return entityManager
                .createNamedQuery(
                        "Caverna.listarTodos",
                        Caverna.class)
                .getResultList();
    }

    @Override
    public Caverna buscarPorCodCadastroAmbiental(String codCadastroAmbiental) {
        try {
            return entityManager
                    .createNamedQuery(
                            "Caverna.buscarPorCodCadastroAmbiental",
                            Caverna.class)
                    .setParameter("codigo", codCadastroAmbiental)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Caverna> listarPorUf(UnidadeFederativa uf) {
        return entityManager
                .createNamedQuery(
                        "Caverna.listarPorUf",
                        Caverna.class)
                .setParameter("uf", uf)
                .getResultList();
    }

    @Override
    public List<Caverna> listarComAcessoPermitido() {
        return entityManager
                .createNamedQuery(
                        "Caverna.listarComAcessoPermitido",
                        Caverna.class)
                .getResultList();
    }

    @Override
    public void atualizar(Caverna caverna) {
        entityManager.merge(caverna);
    }

    @Override
    public void removerPorId(Long id) {
        Caverna caverna = entityManager.find(Caverna.class, id);

        if (caverna != null) {
            entityManager.remove(caverna);
        }
    }
}
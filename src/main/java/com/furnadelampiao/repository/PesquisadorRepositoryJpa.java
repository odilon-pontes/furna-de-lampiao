package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Pesquisador;
import com.furnadelampiao.enums.Titulacao;

import javax.persistence.EntityManager;
import java.util.List;

public class PesquisadorRepositoryJpa implements PesquisadorRepository {

    private final EntityManager entityManager;

    public PesquisadorRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void salvar(Pesquisador pesquisador) {
        entityManager.persist(pesquisador);
    }

    @Override
    public Pesquisador buscarPorId(Long id) {
        return entityManager.find(
                Pesquisador.class,
                id
        );
    }

    @Override
    public List<Pesquisador> listarTodos() {
        return entityManager
                .createNamedQuery(
                        "Pesquisador.listarTodos",
                        Pesquisador.class
                )
                .getResultList();
    }

    @Override
    public List<Pesquisador> buscarPorTitulacao(
            Titulacao titulacao) {

        return entityManager
                .createNamedQuery(
                        "Pesquisador.buscarPorTitulacao",
                        Pesquisador.class
                )
                .setParameter("titulacao", titulacao)
                .getResultList();
    }

    @Override
    public List<Pesquisador> buscarPorAreaPesquisa(
            String area) {

        return entityManager
                .createNamedQuery(
                        "Pesquisador.buscarPorAreaPesquisa",
                        Pesquisador.class
                )
                .setParameter("area", area)
                .getResultList();
    }

    @Override
    public void atualizar(Pesquisador pesquisador) {
        entityManager.merge(pesquisador);
    }

    @Override
    public void removerPorId(Long id) {
        Pesquisador pesquisador =
                entityManager.find(
                        Pesquisador.class,
                        id
                );

        if (pesquisador != null) {
            entityManager.remove(pesquisador);
        }
    }
}
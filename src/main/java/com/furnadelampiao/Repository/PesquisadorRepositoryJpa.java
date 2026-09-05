package com.furnadelampiao.Repository;

import com.furnadelampiao.domain.Pesquisador;
import com.furnadelampiao.domain.Pessoa;
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
        entityManager.getTransaction().begin();
        entityManager.persist(pesquisador);
        entityManager.getTransaction().commit();
    }

    @Override
    public Pesquisador buscarPorId(Long id) {
        return entityManager.find(Pesquisador.class, id);
    }

    @Override
    public List<Pesquisador> listarTodos() {
        return entityManager
                .createQuery(
                        "SELECT p FROM Pesquisador p",
                        Pesquisador.class
                )
                .getResultList();
    }

    @Override
    public List<Pesquisador> buscarPorTitulacao(Titulacao titulacao) {
        return entityManager
                .createQuery(
                        "SELECT p FROM Pesquisador p " +
                                "WHERE p.titulacao = :titulacao",
                        Pesquisador.class
                )
                .setParameter("titulacao", titulacao)
                .getResultList();
    }

    @Override
    public List<Pesquisador> buscarPorAreaPesquisa(String area) {
        return entityManager
                .createQuery(
                        "SELECT p FROM Pesquisador p " +
                                "WHERE p.areaPrincipalPesquisa = :area",
                        Pesquisador.class
                )
                .setParameter("area", area)
                .getResultList();
    }

    @Override
    public void atualizar(Pesquisador pesquisador) {
        entityManager.getTransaction().begin();
        entityManager.merge(pesquisador);
        entityManager.getTransaction().commit();
    }

    @Override
    public void removerPorId(Long id) {
        entityManager.getTransaction().begin();

        Pesquisador pesquisador = entityManager.find(Pesquisador.class, id);

        if (pesquisador != null) {
            entityManager.remove(pesquisador);
        }

        entityManager.getTransaction().commit();
    }

}

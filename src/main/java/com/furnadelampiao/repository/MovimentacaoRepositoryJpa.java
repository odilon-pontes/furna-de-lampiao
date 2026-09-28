package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Movimentacao;

import javax.persistence.EntityManager;
import java.util.List;

public class MovimentacaoRepositoryJpa implements MovimentacaoRepository {

    private final EntityManager entityManager;

    public MovimentacaoRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Movimentacao> buscarPorPessoaId(Long pessoaId) {
        return entityManager
                .createNamedQuery(
                        "Movimentacao.buscarPorPessoaId",
                        Movimentacao.class
                )
                .setParameter("id", pessoaId)
                .getResultList();
    }

    @Override
    public List<Movimentacao> buscarPorExpedicaoId(Long expedicaoId) {
        return entityManager
                .createNamedQuery(
                        "Movimentacao.buscarPorExpedicaoId",
                        Movimentacao.class
                )
                .setParameter("id", expedicaoId)
                .getResultList();
    }

    @Override
    public List<Movimentacao> buscarPorEquipamentoId(Long equipamentoId) {
        return entityManager
                .createNamedQuery(
                        "Movimentacao.buscarPorEquipamentoId",
                        Movimentacao.class
                )
                .setParameter("id", equipamentoId)
                .getResultList();
    }

    @Override
    public boolean existeMovimentacaoAtivaPorEquipamentoId(Long id) {

        Long quantidade = entityManager
                .createNamedQuery(
                        "Movimentacao.existeMovimentacaoAtivaPorEquipamentoId",
                        Long.class
                )
                .setParameter("id", id)
                .getSingleResult();

        return quantidade > 0;
    }

    @Override
    public void salvar(Movimentacao movimentacao) {
        entityManager.persist(movimentacao);
    }

    @Override
    public Movimentacao buscarPorId(Long id) {
        return entityManager.find(
                Movimentacao.class,
                id
        );
    }

    @Override
    public List<Movimentacao> listarTodos() {
        return entityManager
                .createNamedQuery(
                        "Movimentacao.listarTodos",
                        Movimentacao.class
                )
                .getResultList();
    }

    @Override
    public void atualizar(Movimentacao movimentacao) {
        entityManager.merge(movimentacao);
    }

    @Override
    public void removerPorId(Long id) {
        Movimentacao movimentacao =
                entityManager.find(
                        Movimentacao.class,
                        id
                );

        if (movimentacao != null) {
            entityManager.remove(movimentacao);
        }
    }
}
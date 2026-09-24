package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Movimentacao;
import com.furnadelampiao.service.MovimentacaoService;

import javax.persistence.EntityManager;
import java.util.List;

public class MovimentacaoRepositoryJpa implements MovimentacaoRepository {

    private final EntityManager entityManager;

    public MovimentacaoRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Movimentacao> buscarPorPessoaId(Long pessoaId) {
        return entityManager.createQuery(
                        "SELECT m FROM Movimentacao m " +
                                "WHERE m.pessoa.id = :id",
                        Movimentacao.class
                )
                .setParameter("id", pessoaId)
                .getResultList();
    }

    @Override
    public List<Movimentacao> buscarPorExpedicaoId(Long expedicaoId) {
        return entityManager.createQuery(
                        "SELECT m FROM Movimentacao m " +
                                "WHERE m.expedicao.id = :id",
                        Movimentacao.class
                )
                .setParameter("id", expedicaoId)
                .getResultList();
    }

    @Override
    public List<Movimentacao> buscarPorEquipamentoId(Long equipamentoId) {
        return entityManager.createQuery(
                        "SELECT m FROM Movimentacao m " +
                                "WHERE m.equipamento.id = :id",
                        Movimentacao.class
                )
                .setParameter("id", equipamentoId)
                .getResultList();
    }

    @Override
    public boolean existeMovimentacaoAtivaPorEquipamentoId(Long id) {
        Long quantidade = entityManager.createQuery(
                "SELECT COUNT(m) FROM Movimentacao m " +
                        "WHERE m.equipamento.id = :id "+
                        "AND m.dataDevolucao IS NULL",
                        Long.class
        ).setParameter("id", id)
                .getSingleResult();

        return quantidade > 0;
    }

    @Override
    public void salvar(Movimentacao movimentacao) {
        entityManager.persist(movimentacao);
    }

    @Override
    public Movimentacao buscarPorId(Long id) {
        return entityManager.find(Movimentacao.class, id);
    }

    @Override
    public List<Movimentacao> listarTodos() {
        return entityManager.createQuery(
                "SELECT m FROM Movimentacao m ",
                Movimentacao.class
            ).getResultList();
    }

    @Override
    public void atualizar(Movimentacao movimentacao) {
        entityManager.merge(movimentacao);
    }

    @Override
    public void removerPorId(Long id) {
        Movimentacao movimentacao = entityManager.find(Movimentacao.class, id);

        if (movimentacao != null) {
            entityManager.remove(movimentacao);
        }
    }
}
package com.furnadelampiao.repository;

import com.furnadelampiao.domain.AutorizacaoAmbiental;
import com.furnadelampiao.enums.SituacaoAutorizacao;

import javax.persistence.EntityManager;
import java.util.List;

public class AutorizacaoAmbientalRepositoryJpa
        implements AutorizacaoAmbientalRepository {

    private final EntityManager entityManager;

    public AutorizacaoAmbientalRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void salvar(AutorizacaoAmbiental autorizacao) {
        entityManager.persist(autorizacao);
    }

    @Override
    public AutorizacaoAmbiental buscarPorId(Long id) {
        return entityManager.find(AutorizacaoAmbiental.class, id);
    }

    @Override
    public List<AutorizacaoAmbiental> listarTodos() {
        return entityManager
                .createQuery(
                        "SELECT a FROM AutorizacaoAmbiental a",
                        AutorizacaoAmbiental.class)
                .getResultList();
    }

    @Override
    public void atualizar(AutorizacaoAmbiental autorizacao) {
        entityManager.merge(autorizacao);
    }

    @Override
    public void removerPorId(Long id) {
        AutorizacaoAmbiental autorizacao = entityManager.find(AutorizacaoAmbiental.class, id);

        if (autorizacao != null) {
            entityManager.remove(autorizacao);
        }
    }

    @Override
    public long contarVigentesPorExpedicao(
            Long expedicaoId,
            Long autorizacaoId) {

        return entityManager
                .createQuery(
                        "SELECT COUNT(a) " +
                                "FROM AutorizacaoAmbiental a " +
                                "WHERE a.expedicao.id = :expedicaoId " +
                                "AND a.situacao = :situacao " +
                                "AND (:autorizacaoId IS NULL OR a.id <> :autorizacaoId)",
                        Long.class)
                .setParameter("expedicaoId", expedicaoId)
                .setParameter("situacao", SituacaoAutorizacao.VIGENTE)
                .setParameter("autorizacaoId", autorizacaoId)
                .getSingleResult();
    }
}
package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.enums.SituacaoExpedicao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class ExpedicaoRepositoryJpa implements ExpedicaoRepository {

    private final EntityManager entityManager;

    public ExpedicaoRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void salvar(Expedicao expedicao) {
        entityManager.persist(expedicao);
    }

    @Override
    public Expedicao buscarPorId(Long id) {
        return entityManager.find(Expedicao.class, id);
    }

    @Override
    public List<Expedicao> listarTodos() {
        return entityManager
                .createQuery(
                        "SELECT e FROM Expedicao e",
                        Expedicao.class)
                .getResultList();
    }

    @Override
    public Expedicao buscarPorCodigo(String codigo) {
        try {
            return entityManager
                    .createQuery(
                            "SELECT e FROM Expedicao e " +
                                    "WHERE e.codigo = :codigo",
                            Expedicao.class)
                    .setParameter("codigo", codigo)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Expedicao> listarPorCaverna(Long cavernaId) {
        return entityManager
                .createQuery(
                        "SELECT e FROM Expedicao e " +
                                "WHERE e.caverna.id = :cavernaId",
                        Expedicao.class)
                .setParameter("cavernaId", cavernaId)
                .getResultList();
    }

    @Override
    public List<Expedicao> listarPorSituacao(SituacaoExpedicao situacao) {
        return entityManager
                .createQuery(
                        "SELECT e FROM Expedicao e " +
                                "WHERE e.situacao = :situacao",
                        Expedicao.class)
                .setParameter("situacao", situacao)
                .getResultList();
    }

    @Override
    public void atualizar(Expedicao expedicao) {
        entityManager.merge(expedicao);
    }

    @Override
    public void removerPorId(Long id) {
        Expedicao expedicao = entityManager.find(Expedicao.class, id);

        if (expedicao != null) {
            entityManager.remove(expedicao);
        }
    }
}
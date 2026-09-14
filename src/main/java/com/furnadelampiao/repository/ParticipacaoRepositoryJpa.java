package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Participacao;
import com.furnadelampiao.domain.Pesquisador;

import javax.persistence.EntityManager;
import java.util.List;

public class ParticipacaoRepositoryJpa implements ParticipacaoRepository{
    private final EntityManager entityManager;

    public ParticipacaoRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void salvar(Participacao participacao) {
        entityManager.persist(participacao);
    }

    @Override
    public List<Participacao> buscarPorPessoaId(Long id) {
        return entityManager
                .createQuery(
                "SELECT p FROM Participacao p "+
                        "WHERE p.pessoa_id = :id",
                        Participacao.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<Participacao> buscarPorExpedicaoId(Long id) {
        return entityManager
                .createQuery(
                        "SELECT p FROM Participacao p "+
                                "WHERE p.expedicao_id = :id",
                        Participacao.class)
                .setParameter("id", id)
                .getResultList();
    }


    @Override
    public Participacao buscarPorId(Long id) {
        return entityManager.find(Participacao.class, id);
    }

    @Override
    public List<Participacao> listarTodos() {
        return entityManager
                .createQuery("SELECT p FROM Participacao",
                        Participacao.class)
                .getResultList();
    }

    @Override
    public void atualizar(Participacao participacao) {
        entityManager.merge(participacao);
    }

    @Override
    public void removerPorId(Long id) {
        Participacao participacao = entityManager.find(Participacao.class, id);

        if (participacao != null) {
            entityManager.remove(participacao);
        }
    }
}

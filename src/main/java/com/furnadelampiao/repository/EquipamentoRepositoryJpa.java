package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Equipamento;
import com.furnadelampiao.enums.SituacaoOperacional;
import com.furnadelampiao.enums.TipoEquipamento;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class EquipamentoRepositoryJpa implements EquipamentoRepository {

    private final EntityManager entityManager;

    public EquipamentoRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void salvar(Equipamento equipamento) {
        entityManager.persist(equipamento);
    }

    @Override
    public Equipamento buscarPorId(Long id) {
        return entityManager.find(Equipamento.class, id);
    }

    @Override
    public List<Equipamento> listarTodos() {
        return entityManager
                .createNamedQuery(
                        "Equipamento.listarTodos",
                        Equipamento.class)
                .getResultList();
    }

    @Override
    public Equipamento buscarPorCodPatrimonial(String codPatrimonial) {
        try {
            return entityManager
                    .createNamedQuery(
                            "Equipamento.buscarPorCodPatrimonial",
                            Equipamento.class)
                    .setParameter("codPatrimonial", codPatrimonial)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Equipamento> listarPorTipo(TipoEquipamento tipo) {
        return entityManager
                .createNamedQuery(
                        "Equipamento.listarPorTipo",
                        Equipamento.class)
                .setParameter("tipo", tipo)
                .getResultList();
    }

    @Override
    public List<Equipamento> listarPorSituacaoOperacional(SituacaoOperacional situacao) {
        return entityManager
                .createNamedQuery(
                        "Equipamento.listarPorSituacaoOperacional",
                        Equipamento.class)
                .setParameter("situacao", situacao)
                .getResultList();
    }

    @Override
    public void atualizar(Equipamento equipamento) {
        entityManager.merge(equipamento);
    }

    @Override
    public void removerPorId(Long id) {
        Equipamento equipamento = entityManager.find(Equipamento.class, id);

        if (equipamento != null) {
            entityManager.remove(equipamento);
        }
    }
}
package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Relatorio;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class RelatorioRepositoryJpa implements RelatorioRepository {

    private final EntityManager entityManager;

    public RelatorioRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void salvar(Relatorio relatorio) {
        entityManager.persist(relatorio);
    }

    @Override
    public Relatorio buscarPorId(Long id) {
        return entityManager.find(Relatorio.class, id);
    }

    @Override
    public Relatorio buscarPorExpedicaoId(Long expedicaoId) {
        try {
            return entityManager.createQuery(
                    "SELECT r " +
                            "FROM Relatorio r " +
                            "WHERE r.expedicao.id = :expedicaoId",
                    Relatorio.class)
                    .setParameter("expedicaoId", expedicaoId)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Relatorio> listarTodos() {
        return entityManager.createQuery(
                "SELECT r FROM Relatorio r",
                Relatorio.class).getResultList();
    }

    @Override
    public void atualizar(Relatorio relatorio) {
        entityManager.merge(relatorio);
    }

    @Override
    public void removerPorId(Long id) {
        Relatorio relatorio = entityManager.find(Relatorio.class, id);

        if (relatorio != null) {
            entityManager.remove(relatorio);
        }
    }

    @Override
    public byte[] buscarArquivoPorRelatorioId(Long id) {

        return entityManager.createQuery(
                "SELECT r.arquivoCompleto " +
                        "FROM Relatorio r " +
                        "WHERE r.id = :id",
                byte[].class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public Long buscarIdPorExpedicaoId(Long expedicaoId) {
        List<Long> ids = entityManager
                .createQuery(
                        "SELECT r.id FROM Relatorio r WHERE r.expedicao.id = :expedicaoId",
                        Long.class)
                .setParameter("expedicaoId", expedicaoId)
                .getResultList();

        return ids.isEmpty() ? null : ids.get(0);
    }
}
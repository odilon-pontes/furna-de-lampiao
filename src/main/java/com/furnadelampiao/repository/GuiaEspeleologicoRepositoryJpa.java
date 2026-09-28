package com.furnadelampiao.repository;

import com.furnadelampiao.domain.GuiaEspeleologico;
import com.furnadelampiao.enums.NivelCertificacao;

import javax.persistence.EntityManager;
import java.util.List;

public class GuiaEspeleologicoRepositoryJpa
        implements GuiaEspeleologicoRepository {

    private final EntityManager entityManager;

    public GuiaEspeleologicoRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void salvar(GuiaEspeleologico guia) {
        entityManager.persist(guia);
    }

    @Override
    public GuiaEspeleologico buscarPorId(Long id) {
        return entityManager.find(GuiaEspeleologico.class, id);
    }

    @Override
    public List<GuiaEspeleologico> listarTodos() {
        return entityManager
                .createNamedQuery(
                        "GuiaEspeleologico.listarTodos",
                        GuiaEspeleologico.class)
                .getResultList();
    }

    @Override
    public List<GuiaEspeleologico> buscarPorNivelCertificacao(
            NivelCertificacao nivel) {

        return entityManager
                .createNamedQuery(
                        "GuiaEspeleologico.buscarPorNivelCertificacao",
                        GuiaEspeleologico.class)
                .setParameter("nivel", nivel)
                .getResultList();
    }

    @Override
    public List<GuiaEspeleologico> listarCertificacoesVencidas() {

        return entityManager
                .createNamedQuery(
                        "GuiaEspeleologico.listarCertificacoesVencidas",
                        GuiaEspeleologico.class)
                .getResultList();
    }

    @Override
    public void atualizar(GuiaEspeleologico guia) {
        entityManager.merge(guia);
    }

    @Override
    public void removerPorId(Long id) {
        GuiaEspeleologico guia = entityManager.find(GuiaEspeleologico.class, id);

        if (guia != null) {
            entityManager.remove(guia);
        }
    }
}
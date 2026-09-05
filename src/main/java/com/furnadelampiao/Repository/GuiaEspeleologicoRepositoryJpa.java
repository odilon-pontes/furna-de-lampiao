package com.furnadelampiao.Repository;

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
        entityManager.getTransaction().begin();
        entityManager.persist(guia);
        entityManager.getTransaction().commit();
    }

    @Override
    public GuiaEspeleologico buscarPorId(Long id) {
        return entityManager.find(GuiaEspeleologico.class, id);
    }

    @Override
    public List<GuiaEspeleologico> listarTodos() {
        return entityManager
                .createQuery(
                        "SELECT g FROM GuiaEspeleologico g",
                        GuiaEspeleologico.class
                )
                .getResultList();
    }

    @Override
    public List<GuiaEspeleologico> buscarPorNivelCertificacao(
            NivelCertificacao nivel) {

        return entityManager
                .createQuery(
                        "SELECT g FROM GuiaEspeleologico g " +
                                "WHERE g.nivelCertificacao = :nivel",
                        GuiaEspeleologico.class
                )
                .setParameter("nivel", nivel)
                .getResultList();
    }

    @Override
    public List<GuiaEspeleologico> listarCertificacoesVencidas() {

        return entityManager
                .createQuery(
                        "SELECT g FROM GuiaEspeleologico g " +
                                "WHERE g.dataValidadeCertificacao < CURRENT_DATE",
                        GuiaEspeleologico.class
                )
                .getResultList();
    }

    @Override
    public void atualizar(GuiaEspeleologico guia) {
        entityManager.getTransaction().begin();
        entityManager.merge(guia);
        entityManager.getTransaction().commit();
    }

    @Override
    public void removerPorId(Long id) {
        entityManager.getTransaction().begin();

        GuiaEspeleologico guia = entityManager.find(GuiaEspeleologico.class, id);

        if (guia != null) {
            entityManager.remove(guia);
        }

        entityManager.getTransaction().commit();
    }
}

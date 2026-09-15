package com.furnadelampiao.repository;

import com.furnadelampiao.domain.ColetaCientifica;
import com.furnadelampiao.enums.MetodoEmpregado;
import com.furnadelampiao.enums.SituacaoValidacaoColeta;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ColetaCientificaRepositoryJpa implements ColetaCientificaRepository {
    private final EntityManager entityManager;

    public ColetaCientificaRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ColetaCientifica buscarPorId(Long id) {
        return entityManager.find(ColetaCientifica.class, id);
    }

    @Override
    public List<ColetaCientifica> buscarPorPesquisadorId(Long id) {
        return entityManager
                .createQuery(
                        "SELECT c FROM ColetaCientifica c "+
                                "WHERE c.pesquisador.id = :id",
                        ColetaCientifica.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<ColetaCientifica> buscarPorExpedicaoId(Long id) {
        return entityManager
                .createQuery(
                        "SELECT c FROM ColetaCientifica c "+
                                "WHERE c.expedicao.id = :id",
                        ColetaCientifica.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<ColetaCientifica> buscarPorSetorId(Long id) {
        return entityManager
                .createQuery(
                        "SELECT c FROM ColetaCientifica c "+
                                "WHERE c.setor.id = :id",
                        ColetaCientifica.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<ColetaCientifica> buscarPorSituacaoValidacao(SituacaoValidacaoColeta situacao) {
        return entityManager
                .createQuery(
                        "SELECT c FROM ColetaCientifica c "+
                                "WHERE c.situacaoValidacao = :situacao",
                        ColetaCientifica.class)
                .setParameter("situacao", situacao)
                .getResultList();
    }

    @Override
    public List<ColetaCientifica> buscarPorMetodoEmpregado(MetodoEmpregado metodo) {
        return entityManager
                .createQuery(
                        "SELECT c FROM ColetaCientifica c "+
                                "WHERE c.metodoEmpregado = :metodo",
                        ColetaCientifica.class)
                .setParameter("metodo", metodo)
                .getResultList();
    }

    @Override
    public List<ColetaCientifica> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return entityManager
                .createQuery(
                        "SELECT c FROM ColetaCientifica c "+
                                "WHERE c.dataHoraColeta BETWEEN :inicio AND :fim",
                        ColetaCientifica.class)
                .setParameter("inicio", inicio)
                .setParameter("fim", fim)
                .getResultList();
    }

    @Override
    public void salvar(ColetaCientifica coleta) {
        entityManager.persist(coleta);
    }


    @Override
    public List<ColetaCientifica> listarTodos() {
        return entityManager
                .createQuery(
                        "SELECT c FROM ColetaCientifica c ",
                        ColetaCientifica.class)
                .getResultList();
    }

    @Override
    public void atualizar(ColetaCientifica coleta) {
        entityManager.merge(coleta);
    }

    @Override
    public void removerPorId(Long id) {
        ColetaCientifica coleta = entityManager.find(ColetaCientifica.class, id);

        if (coleta != null) {
            entityManager.remove(coleta);
        }
    }
}

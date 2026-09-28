package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.enums.SituacaoExpedicao;
import com.furnadelampiao.dto.ExpedicaoResumoDTO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
                .createNamedQuery(
                        "Expedicao.listarTodos",
                        Expedicao.class)
                .getResultList();
    }

    @Override
    public Expedicao buscarPorCodigo(String codigo) {
        try {
            return entityManager
                    .createNamedQuery(
                            "Expedicao.buscarPorCodigo",
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
                .createNamedQuery(
                        "Expedicao.listarPorCaverna",
                        Expedicao.class)
                .setParameter("cavernaId", cavernaId)
                .getResultList();
    }

    @Override
    public List<Expedicao> listarPorSituacao(SituacaoExpedicao situacao) {
        return entityManager
                .createNamedQuery(
                        "Expedicao.listarPorSituacao",
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

    @Override
    public List<ExpedicaoResumoDTO> listarResumoPorPeriodoESituacao(
            LocalDate inicio, LocalDate fim, SituacaoExpedicao situacao) {

        LocalDateTime inicioDoDia = inicio.atStartOfDay();
        LocalDateTime fimDoDia = fim.atTime(LocalTime.MAX);

        return entityManager
                .createNamedQuery(
                        "Expedicao.listarResumoPorPeriodoESituacao",
                        ExpedicaoResumoDTO.class)
                .setParameter("inicioDoDia", inicioDoDia)
                .setParameter("fimDoDia", fimDoDia)
                .setParameter("situacao", situacao)
                .getResultList();
    }
}
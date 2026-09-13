package com.furnadelampiao.service;

import com.furnadelampiao.Repository.ExpedicaoRepository;
import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.enums.SituacaoExpedicao;
import com.furnadelampiao.infra.TransacaoExecutor;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class ExpedicaoService {

    private final ExpedicaoRepository repository;
    private final EntityManager entityManager;

    public ExpedicaoService(ExpedicaoRepository repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    public void cadastrar(Expedicao expedicao) {
        validarExpedicao(expedicao);

        Expedicao existente = repository.buscarPorCodigo(expedicao.getCodigo());
        if (existente != null) {
            throw new IllegalArgumentException(
                    "Já existe uma expedição cadastrada com o código "
                            + expedicao.getCodigo());
        }

        if (expedicao.getSituacao() == null) {
            expedicao.setSituacao(SituacaoExpedicao.PLANEJADA);
        }

        TransacaoExecutor.executar(entityManager, () -> repository.salvar(expedicao));
    }

    public Expedicao buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        return repository.buscarPorId(id);
    }

    public List<Expedicao> listarTodos() {
        return repository.listarTodos();
    }

    public List<Expedicao> listarPorCaverna(Long cavernaId) {
        if (cavernaId == null) {
            throw new IllegalArgumentException("ID da caverna não pode ser nulo.");
        }
        return repository.listarPorCaverna(cavernaId);
    }

    public List<Expedicao> listarPorSituacao(SituacaoExpedicao situacao) {
        if (situacao == null) {
            throw new IllegalArgumentException("Situação não pode ser nula.");
        }
        return repository.listarPorSituacao(situacao);
    }

    public void atualizar(Expedicao expedicao) {
        validarExpedicao(expedicao);

        if (expedicao.getId() == null) {
            throw new IllegalArgumentException(
                    "Expedição precisa de ID para ser atualizada.");
        }

        Expedicao existente = repository.buscarPorCodigo(expedicao.getCodigo());
        if (existente != null && !existente.getId().equals(expedicao.getId())) {
            throw new IllegalArgumentException(
                    "Já existe outra expedição cadastrada com o código "
                            + expedicao.getCodigo());
        }

        TransacaoExecutor.executar(entityManager, () -> repository.atualizar(expedicao));
    }

    public void removerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        TransacaoExecutor.executar(entityManager, () -> repository.removerPorId(id));
    }

    private void validarExpedicao(Expedicao expedicao) {
        if (expedicao == null) {
            throw new IllegalArgumentException("Expedição não pode ser nula.");
        }
        if (expedicao.getCodigo() == null || expedicao.getCodigo().isBlank()) {
            throw new IllegalArgumentException("Código da expedição é obrigatório.");
        }
        if (expedicao.getTitulo() == null || expedicao.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Título da expedição é obrigatório.");
        }
        if (expedicao.getCaverna() == null) {
            throw new IllegalArgumentException("Expedição precisa estar associada a uma caverna.");
        }
        if (expedicao.getInicioPrevisto() == null || expedicao.getTerminoPrevisto() == null) {
            throw new IllegalArgumentException("Datas previstas de início e término são obrigatórias.");
        }
        if (!expedicao.getTerminoPrevisto().isAfter(expedicao.getInicioPrevisto())) {
            throw new IllegalArgumentException(
                    "A data de término prevista deve ser posterior à data de início prevista.");
        }
        if (expedicao.getQtdMaxParticipantes() <= 0) {
            throw new IllegalArgumentException(
                    "Quantidade máxima de participantes deve ser maior que zero.");
        }
        if (expedicao.getOrcamentoAprovado() != null
                && expedicao.getOrcamentoAprovado().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Orçamento aprovado não pode ser negativo.");
        }
        if (expedicao.getCustoRealizado() != null
                && expedicao.getCustoRealizado().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Custo realizado não pode ser negativo.");
        }
    }
}
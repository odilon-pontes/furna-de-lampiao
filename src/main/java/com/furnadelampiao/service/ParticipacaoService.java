package com.furnadelampiao.service;

import com.furnadelampiao.domain.Participacao;
import com.furnadelampiao.repository.ParticipacaoRepository;
import com.furnadelampiao.infra.TransacaoExecutor;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class ParticipacaoService {
    private final EntityManager entityManager;
    private final ParticipacaoRepository repository;

    public ParticipacaoService(EntityManager entityManager, ParticipacaoRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    public void cadastrar(Participacao participacao) {
        validarParticipacao(participacao);
        TransacaoExecutor.executar(entityManager, () -> repository.salvar(participacao));
    }

    public void atualizar(Participacao participacao) {
        validarParticipacao(participacao);
        if (participacao.getId() == null) {
            throw new IllegalArgumentException("participacao deve possuir ID para ser atualizado");
        }
        TransacaoExecutor.executar(entityManager, () -> repository.atualizar(participacao));
    }

    public void remover(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id não pode ser nulo.");
        }
        TransacaoExecutor.executar(entityManager, () -> {
            Participacao participacao = entityManager.find(Participacao.class, id);
            if (participacao != null) {
                entityManager.remove(participacao);
            }
        });
    }

    public List<Participacao> listarTodos() {
        return repository.listarTodos();
    }


    public List<Participacao> buscarPorPessoaId(Long id) {
        return repository.buscarPorPessoaId(id);
    }

    public List<Participacao> buscarPorExpedicaoId(Long id) {
        return repository.buscarPorExpedicaoId(id);
    }

    private void validarParticipacao(Participacao participacao) {
        if (participacao == null) {
            throw new IllegalArgumentException(
                    "Participacao não pode ser nulo.");
        }

        if (participacao.getPessoa() == null) {
            throw new IllegalArgumentException(
                    "Pessoa não pode ser nulo.");
        }

        if (participacao.getExpedicao() == null) {
            throw new IllegalArgumentException(
                    "Expedição não pode ser nulo.");
        }

        if (participacao.getDataConfirmacao() == null ||
                participacao.getDataConfirmacao().toString().isBlank()) {
            throw new IllegalArgumentException(
                    "Data de confirmação é obrigatório");
        }

        if (participacao.getPapelParticipante() == null ||
                participacao.getPapelParticipante().toString().isBlank()) {
            throw new IllegalArgumentException(
                    "Papel do participante é obrigatória");
        }

        if (participacao.getValorDiaria() != null &&
                participacao.getValorDiaria().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Valor diária não pode ser negativo");
        }

        if (participacao.getQtdPrevistaDias() != null &&
                participacao.getQtdPrevistaDias() < 0) {
            throw new IllegalArgumentException(
                    "Quantidade prevista em dias não pode ser negativo");
        }

        if (participacao.getPresencaConfirmada() == null) {
            participacao.setPresencaConfirmada(false);
        }
    }

    public Participacao buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "ID não pode ser nulo."
            );
        }

        return repository.buscarPorId(id);
    }
}

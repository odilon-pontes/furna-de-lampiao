package com.furnadelampiao.service;

import com.furnadelampiao.domain.Participacao;
import com.furnadelampiao.repository.ParticipacaoRepository;

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
        try {
            entityManager.getTransaction().begin();

            repository.salvar(participacao);

            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }

    }

    public void atualizar(Participacao participacao) {
        validarParticipacao(participacao);

        if (participacao.getId() == null) {
            throw new IllegalArgumentException(
                    "participacao deve possuir ID para ser atualizado");
        }

        try {
            entityManager.getTransaction().begin();

            repository.atualizar(participacao);

            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
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

    public void remover(Long id) {

    }
    private void validarParticipacao(Participacao participacao) {
        if (participacao == null) {
            throw new IllegalArgumentException(
                    "Participacao não pode ser nulo."
            );
        }

        if (participacao.getPessoa() == null) {
            throw new IllegalArgumentException(
                    "Pessoa não pode ser nulo."
            );
        }

        if (participacao.getExpedicao() == null) {
            throw new IllegalArgumentException(
                    "Expedição não pode ser nulo."
            );
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
                participacao.getQtdPrevistaDias()  < 0) {
            throw new IllegalArgumentException(
                    "Quantidade prevista em dias não pode ser negativo");
        }
    }
}

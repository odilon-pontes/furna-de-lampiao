package com.furnadelampiao.service;

import com.furnadelampiao.domain.AutorizacaoAmbiental;
import com.furnadelampiao.enums.SituacaoAutorizacao;
import com.furnadelampiao.infra.TransacaoExecutor;
import com.furnadelampiao.repository.AutorizacaoAmbientalRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class AutorizacaoAmbientalService {

    private final EntityManager entityManager;
    private final AutorizacaoAmbientalRepository repository;

    public AutorizacaoAmbientalService(
            EntityManager entityManager,
            AutorizacaoAmbientalRepository repository) {

        this.entityManager = entityManager;
        this.repository = repository;
    }

    public void cadastrar(AutorizacaoAmbiental autorizacao) {
        validarAutorizacao(autorizacao);

        if (autorizacao.getSituacao() == SituacaoAutorizacao.VIGENTE
                && repository.contarVigentesPorExpedicao(
                        autorizacao.getExpedicao().getId(),
                        null) > 0) {

            throw new IllegalArgumentException(
                    "A expedição já possui uma autorização ambiental vigente.");
        }

        TransacaoExecutor.executar(
                entityManager,
                () -> repository.salvar(autorizacao));
    }

    public AutorizacaoAmbiental buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "ID não pode ser nulo.");
        }

        return repository.buscarPorId(id);
    }

    public List<AutorizacaoAmbiental> listarTodos() {
        return repository.listarTodos();
    }

    public void atualizar(AutorizacaoAmbiental autorizacao) {
        validarAutorizacao(autorizacao);

        if (autorizacao.getId() == null) {
            throw new IllegalArgumentException(
                    "Autorização precisa de ID para ser atualizada.");
        }

        if (autorizacao.getSituacao() == SituacaoAutorizacao.VIGENTE
                && repository.contarVigentesPorExpedicao(
                        autorizacao.getExpedicao().getId(),
                        autorizacao.getId()) > 0) {

            throw new IllegalArgumentException(
                    "A expedição já possui uma autorização ambiental vigente.");
        }

        TransacaoExecutor.executar(
                entityManager,
                () -> repository.atualizar(autorizacao));
    }

    public void removerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "ID não pode ser nulo.");
        }

        TransacaoExecutor.executar(
                entityManager,
                () -> repository.removerPorId(id));
    }

    private void validarAutorizacao(AutorizacaoAmbiental autorizacao) {
        if (autorizacao == null) {
            throw new IllegalArgumentException(
                    "Autorização ambiental não pode ser nula.");
        }

        if (autorizacao.getOrgaoEmissor() == null
                || autorizacao.getOrgaoEmissor().isBlank()) {
            throw new IllegalArgumentException(
                    "Órgão emissor é obrigatório.");
        }

        if (autorizacao.getNum() == null) {
            throw new IllegalArgumentException("Número da autorização é obrigatório.");
        }

        if (autorizacao.getDataEmissao() == null) {
            throw new IllegalArgumentException(
                    "Data de emissão é obrigatória.");
        }

        if (autorizacao.getValidade() == null) {
            throw new IllegalArgumentException(
                    "Data de validade é obrigatória.");
        }

        if (autorizacao.getSituacao() == null) {
            throw new IllegalArgumentException(
                    "Situação da autorização é obrigatória.");
        }

        if (autorizacao.getExpedicao() == null
                || autorizacao.getExpedicao().getId() == null) {
            throw new IllegalArgumentException(
                    "Autorização precisa estar associada a uma expedição já cadastrada.");
        }
    }
}
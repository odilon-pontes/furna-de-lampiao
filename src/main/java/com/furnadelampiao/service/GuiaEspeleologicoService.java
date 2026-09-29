package com.furnadelampiao.service;

import com.furnadelampiao.domain.GuiaEspeleologico;
import com.furnadelampiao.enums.NivelCertificacao;
import com.furnadelampiao.infra.TransacaoExecutor;
import com.furnadelampiao.repository.GuiaEspeleologicoRepository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class GuiaEspeleologicoService {

    private final GuiaEspeleologicoRepository repository;
    private final EntityManager entityManager;

    public GuiaEspeleologicoService(
            EntityManager entityManager,
            GuiaEspeleologicoRepository repository) {

        this.entityManager = entityManager;
        this.repository = repository;
    }

    public void cadastrar(GuiaEspeleologico guia) {
        validarGuia(guia);

        TransacaoExecutor.executar(
                entityManager,
                () -> repository.salvar(guia)
        );
    }

    public GuiaEspeleologico buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "ID não pode ser nulo."
            );
        }

        return repository.buscarPorId(id);
    }

    public void atualizar(GuiaEspeleologico guia) {
        validarGuia(guia);

        if (guia.getId() == null) {
            throw new IllegalArgumentException(
                    "Guia espeleológico deve possuir ID para ser atualizado."
            );
        }

        GuiaEspeleologico existente =
                repository.buscarPorId(guia.getId());

        if (existente == null) {
            throw new IllegalArgumentException(
                    "Guia espeleológico não encontrado."
            );
        }

        TransacaoExecutor.executar(
                entityManager,
                () -> repository.atualizar(guia)
        );
    }

    public List<GuiaEspeleologico> listarTodos() {
        return repository.listarTodos();
    }

    public List<GuiaEspeleologico> buscarPorNivelCertificacao(
            NivelCertificacao nivelCertificacao) {

        if (nivelCertificacao == null) {
            throw new IllegalArgumentException(
                    "Nível de certificação não pode ser nulo."
            );
        }

        return repository.buscarPorNivelCertificacao(
                nivelCertificacao
        );
    }

    public List<GuiaEspeleologico> listarCertificacoesVencidas() {
        return repository.listarCertificacoesVencidas();
    }

    private void validarGuia(GuiaEspeleologico guia) {

        if (guia == null) {
            throw new IllegalArgumentException(
                    "Guia espeleológico não pode ser nulo."
            );
        }

        if (guia.getNome() == null ||
                guia.getNome().isBlank()) {

            throw new IllegalArgumentException(
                    "Nome do guia espeleológico é obrigatório."
            );
        }

        if (guia.getNumCredenciamento() == null ||
                guia.getNumCredenciamento().isBlank()) {

            throw new IllegalArgumentException(
                    "Número de credenciamento é obrigatório."
            );
        }

        if (guia.getNivelCertificacao() == null) {
            throw new IllegalArgumentException(
                    "Nível de certificação é obrigatório."
            );
        }

        if (guia.getDataValidadeCertificacao() == null) {
            throw new IllegalArgumentException(
                    "Data de validade da certificação é obrigatória."
            );
        }

        if (guia.getQtdExpedicoesConcluidas() == null ||
                guia.getQtdExpedicoesConcluidas() < 0) {

            throw new IllegalArgumentException(
                    "Quantidade de expedições concluídas não pode ser negativa."
            );
        }

        if (guia.getDataNasc() != null &&
                guia.getDataNasc().isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Data de nascimento não pode ser futura."
            );
        }

        if (guia.getDataValidadeCertificacao()
                .isBefore(guia.getDataNasc() != null
                        ? guia.getDataNasc()
                        : LocalDate.MIN)) {

            throw new IllegalArgumentException(
                    "Data de validade da certificação é inválida."
            );
        }
    }
}
package com.furnadelampiao.service;

import com.furnadelampiao.domain.ColetaCientifica;
import com.furnadelampiao.enums.MetodoEmpregado;
import com.furnadelampiao.enums.SituacaoValidacaoColeta;
import com.furnadelampiao.repository.ColetaCientificaRepository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ColetaCientificaService {

    private final EntityManager entityManager;
    private final ColetaCientificaRepository repository;

    public ColetaCientificaService(
            EntityManager entityManager,
            ColetaCientificaRepository repository) {

        this.entityManager = entityManager;
        this.repository = repository;
    }

    public void cadastrar(ColetaCientifica coleta) {
        validarColetaCientifica(coleta);

        try {
            entityManager.getTransaction().begin();

            repository.salvar(coleta);

            entityManager.getTransaction().commit();

        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    public void atualizar(ColetaCientifica coleta) {
        validarColetaCientifica(coleta);

        if (coleta.getId() == null) {
            throw new IllegalArgumentException(
                    "Coleta deve possuir ID para ser atualizada."
            );
        }

        try {
            entityManager.getTransaction().begin();

            repository.atualizar(coleta);

            entityManager.getTransaction().commit();

        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    public List<ColetaCientifica> listarTodos() {
        return repository.listarTodos();
    }

    public List<ColetaCientifica> buscarPorExpedicaoId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "ID da expedição não pode ser nulo."
            );
        }

        return repository.buscarPorExpedicaoId(id);
    }

    public List<ColetaCientifica> buscarPorSetorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "ID do setor não pode ser nulo."
            );
        }

        return repository.buscarPorSetorId(id);
    }

    public List<ColetaCientifica> buscarPorPesquisadorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "ID do pesquisador não pode ser nulo."
            );
        }

        return repository.buscarPorPesquisadorId(id);
    }

    public List<ColetaCientifica> buscarPorPeriodo(
            LocalDateTime inicio,
            LocalDateTime fim) {

        if (inicio == null || fim == null) {
            throw new IllegalArgumentException(
                    "Data inicial e final são obrigatórias."
            );
        }

        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException(
                    "A data inicial não pode ser posterior à data final."
            );
        }

        return repository.buscarPorPeriodo(inicio, fim);
    }

    public List<ColetaCientifica> buscarPorMetodoEmpregado(
            MetodoEmpregado metodo) {

        if (metodo == null) {
            throw new IllegalArgumentException(
                    "Método empregado não pode ser nulo."
            );
        }

        return repository.buscarPorMetodoEmpregado(metodo);
    }

    public List<ColetaCientifica> buscarPorSituacaoValidacao(
            SituacaoValidacaoColeta situacao) {

        if (situacao == null) {
            throw new IllegalArgumentException(
                    "Situação de validação não pode ser nula."
            );
        }

        return repository.buscarPorSituacaoValidacao(situacao);
    }

    private void validarColetaCientifica(ColetaCientifica coleta) {

        if (coleta == null) {
            throw new IllegalArgumentException(
                    "Coleta não pode ser nula."
            );
        }

        if (coleta.getDataHoraColeta() == null) {
            throw new IllegalArgumentException(
                    "Data e hora da coleta são obrigatórias."
            );
        }

        if (coleta.getDataHoraColeta().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Data e hora da coleta não podem ser futuras."
            );
        }

        if (coleta.getMetodoEmpregado() == null) {
            throw new IllegalArgumentException(
                    "Método empregado é obrigatório."
            );
        }

        if (coleta.getExpedicao() == null) {
            throw new IllegalArgumentException(
                    "Expedição é obrigatória."
            );
        }

        if (coleta.getPesquisador() == null) {
            throw new IllegalArgumentException(
                    "Pesquisador é obrigatório."
            );
        }

        if (coleta.getSetor() == null) {
            throw new IllegalArgumentException(
                    "Setor é obrigatório."
            );
        }

        if (coleta.getTemperatura() == null) {
            throw new IllegalArgumentException(
                    "Temperatura é obrigatória."
            );
        }

        if (coleta.getUmidadeRelativa() == null) {
            throw new IllegalArgumentException(
                    "Umidade relativa é obrigatória."
            );
        }

        if (coleta.getUmidadeRelativa().compareTo(BigDecimal.ZERO) < 0 ||
                coleta.getUmidadeRelativa().compareTo(
                        new BigDecimal("100")) > 0) {

            throw new IllegalArgumentException(
                    "Umidade relativa deve estar entre 0 e 100."
            );
        }

        if (coleta.getProfundidade() == null) {
            throw new IllegalArgumentException(
                    "Profundidade é obrigatória."
            );
        }

        if (coleta.getProfundidade().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Profundidade não pode ser negativa."
            );
        }

        if (coleta.getSituacaoValidacao() == null) {
            throw new IllegalArgumentException(
                    "Situação de validação é obrigatória."
            );
        }
    }
}
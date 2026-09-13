package com.furnadelampiao.service;

import com.furnadelampiao.repository.PesquisadorRepositoryJpa;
import com.furnadelampiao.domain.Pesquisador;
import com.furnadelampiao.enums.Titulacao;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class PesquisadorService {
    private final EntityManager entityManager;
    private final PesquisadorRepositoryJpa repository;

    public PesquisadorService(EntityManager entityManager, PesquisadorRepositoryJpa repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    public void cadastrar(Pesquisador pesquisador) {
        validarPesquisador(pesquisador);
        try {
            entityManager.getTransaction().begin();

            repository.salvar(pesquisador);

            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }

    }

    public void atualizar(Pesquisador pesquisador) {
        validarPesquisador(pesquisador);

        if (pesquisador.getId() == null) {
            throw new IllegalArgumentException(
                    "Pesquisador deve possuir ID para ser atualizado");
        }

        try {
            entityManager.getTransaction().begin();

            repository.atualizar(pesquisador);

            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }

    public List<Pesquisador> listarTodos() {
        return repository.listarTodos();
    }

    public List<Pesquisador> buscarPorTitulacao(Titulacao titulacao) {
        return repository.buscarPorTitulacao(titulacao);
    }
    private void validarPesquisador(Pesquisador pesquisador) {
        if (pesquisador == null) {
            throw new IllegalArgumentException(
                    "Pesquisador não pode ser nulo."
            );
        }

        if (pesquisador.getNome() == null || pesquisador.getNome().isBlank()){
            throw new IllegalArgumentException(
                    "Nome de pesquisador é obrigatório."
            );
        }

        if (pesquisador.getNumRegistroInstitucional() == null ||
                pesquisador.getNumRegistroInstitucional().isBlank()) {
            throw new IllegalArgumentException(
                    "Número de registro institucional é obrigatório");
        }

        if (pesquisador.getAreaPrincipalPesquisa() == null ||
                pesquisador.getAreaPrincipalPesquisa().isBlank()) {
            throw new IllegalArgumentException(
                    "Área principal de pesquisa é obrigatória");
        }

        if (pesquisador.getTitulacao() == null) {
            throw new IllegalArgumentException(
                    "Titulação é obrigatória");
        }

        if (pesquisador.getValorDiarioBolsa() != null &&
                pesquisador.getValorDiarioBolsa().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Valor diário da bolsa não pode ser negativo");
        }
    }
}

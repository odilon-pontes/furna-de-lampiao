package com.furnadelampiao.service;

import com.furnadelampiao.repository.PesquisadorRepository;
import com.furnadelampiao.domain.Pesquisador;
import com.furnadelampiao.enums.Titulacao;
import com.furnadelampiao.infra.TransacaoExecutor;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class PesquisadorService {
    private final EntityManager entityManager;
    private final PesquisadorRepository repository;

    public PesquisadorService(EntityManager entityManager, PesquisadorRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    public void cadastrar(Pesquisador pesquisador) {
        validarPesquisador(pesquisador);
        TransacaoExecutor.executar(entityManager, () -> repository.salvar(pesquisador));
    }

    public void atualizar(Pesquisador pesquisador) {
        validarPesquisador(pesquisador);
        if (pesquisador.getId() == null) {
            throw new IllegalArgumentException("Pesquisador deve possuir ID para ser atualizado");
        }
        TransacaoExecutor.executar(entityManager, () -> repository.atualizar(pesquisador));
    }

    public List<Pesquisador> listarTodos() {
        return repository.listarTodos();
    }

    public List<Pesquisador> buscarPorAreaPesquisa(String area) {
        return repository.buscarPorAreaPesquisa(area);
    }

    public List<Pesquisador> buscarPorTitulacao(Titulacao titulacao) {
        return repository.buscarPorTitulacao(titulacao);
    }

    private void validarPesquisador(Pesquisador pesquisador) {
        if (pesquisador == null) {
            throw new IllegalArgumentException(
                    "Pesquisador não pode ser nulo.");
        }

        if (pesquisador.getNome() == null || pesquisador.getNome().isBlank()) {
            throw new IllegalArgumentException(
                    "Nome de pesquisador é obrigatório.");
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

    public Pesquisador buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "ID não pode ser nulo."
            );
        }

        return repository.buscarPorId(id);
    }
}

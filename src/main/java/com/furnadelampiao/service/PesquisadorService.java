package com.furnadelampiao.service;

import com.furnadelampiao.Repository.PesquisadorRepositoryJpa;
import com.furnadelampiao.domain.Pesquisador;
import com.furnadelampiao.enums.Titulacao;
import com.furnadelampiao.infra.TransacaoExecutor;

import javax.persistence.EntityManager;
import java.util.List;

public class PesquisadorService {
    private final PesquisadorRepositoryJpa repository;
    private final EntityManager entityManager;

    public PesquisadorService(PesquisadorRepositoryJpa repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    public void cadastrar(Pesquisador pesquisador) {
        // regras específicas de pesquisador

        TransacaoExecutor.executar(entityManager, () -> repository.salvar(pesquisador));
    }

    public List<Pesquisador> listarTodos() {
        return repository.listarTodos();
    }

    public List<Pesquisador> buscarPorTitulacao(Titulacao titulacao) {
        return repository.buscarPorTitulacao(titulacao);
    }
}
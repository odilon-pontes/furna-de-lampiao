package com.furnadelampiao.service;

import com.furnadelampiao.Repository.GuiaEspeleologicoRepositoryJpa;
import com.furnadelampiao.domain.GuiaEspeleologico;
import com.furnadelampiao.infra.TransacaoExecutor;

import javax.persistence.EntityManager;
import java.util.List;

public class GuiaEspeleologicoService {
    private final GuiaEspeleologicoRepositoryJpa repository;
    private final EntityManager entityManager;

    public GuiaEspeleologicoService(GuiaEspeleologicoRepositoryJpa repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    public void cadastrar(GuiaEspeleologico guia) {
        // regras específicas do guia

        TransacaoExecutor.executar(entityManager, () -> repository.salvar(guia));
    }

    public List<GuiaEspeleologico> listarTodos() {
        return repository.listarTodos();
    }
}
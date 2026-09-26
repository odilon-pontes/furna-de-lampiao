package com.furnadelampiao.service;

import com.furnadelampiao.domain.PlanoSeguranca;
import com.furnadelampiao.infra.TransacaoExecutor;
import com.furnadelampiao.repository.PlanoSegurancaRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class PlanoSegurancaService {

    private final EntityManager entityManager;
    private final PlanoSegurancaRepository repository;

    public PlanoSegurancaService(
            EntityManager entityManager,
            PlanoSegurancaRepository repository) {

        this.entityManager = entityManager;
        this.repository = repository;
    }

    public void cadastrar(PlanoSeguranca planoSeguranca) {
        validarPlanoSeguranca(planoSeguranca);

        TransacaoExecutor.executar(
                entityManager,
                () -> repository.salvar(planoSeguranca));
    }

    public PlanoSeguranca buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }

        return repository.buscarPorId(id);
    }

    public List<PlanoSeguranca> listarTodos() {
        return repository.listarTodos();
    }

    public void atualizar(PlanoSeguranca planoSeguranca) {
        validarPlanoSeguranca(planoSeguranca);

        if (planoSeguranca.getId() == null) {
            throw new IllegalArgumentException(
                    "Plano de segurança precisa de ID para ser atualizado.");
        }

        TransacaoExecutor.executar(
                entityManager,
                () -> repository.atualizar(planoSeguranca));
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

    private void validarPlanoSeguranca(PlanoSeguranca planoSeguranca) {
        if (planoSeguranca == null) {
            throw new IllegalArgumentException(
                    "Plano de segurança não pode ser nulo.");
        }

        if (planoSeguranca.getTempoMaxSemComunicacao() == null) {
            throw new IllegalArgumentException(
                    "Tempo máximo sem comunicação é obrigatório.");
        }

        if (planoSeguranca.getTelefoneEmergencia() == null
                || planoSeguranca.getTelefoneEmergencia().isBlank()) {
            throw new IllegalArgumentException(
                    "Telefone de emergência é obrigatório.");
        }

        if (planoSeguranca.getNecessidadeEquipeMedica() == null) {
            planoSeguranca.setNecessidadeEquipeMedica(false);
        }
    }
}
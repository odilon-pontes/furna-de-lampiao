package com.furnadelampiao.service;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.enums.UnidadeFederativa;
import com.furnadelampiao.infra.TransacaoExecutor;
import com.furnadelampiao.repository.CavernaRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class CavernaService {

    private final EntityManager entityManager;
    private final CavernaRepository repository;

    public CavernaService(EntityManager entityManager, CavernaRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    public void cadastrar(Caverna caverna) {
        validarCaverna(caverna);

        Caverna existente = repository.buscarPorCodCadastroAmbiental(
                caverna.getCodCadastroAmbiental());
        if (existente != null) {
            throw new IllegalArgumentException(
                    "Já existe uma caverna cadastrada com o código ambiental "
                            + caverna.getCodCadastroAmbiental());
        }

        TransacaoExecutor.executar(entityManager, () -> repository.salvar(caverna));
    }

    public Caverna buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        return repository.buscarPorId(id);
    }

    public List<Caverna> listarTodos() {
        return repository.listarTodos();
    }

    public List<Caverna> listarPorUf(UnidadeFederativa uf) {
        return repository.listarPorUf(uf);
    }

    public List<Caverna> listarComAcessoPermitido() {
        return repository.listarComAcessoPermitido();
    }

    public void atualizar(Caverna caverna) {
        validarCaverna(caverna);

        if (caverna.getId() == null) {
            throw new IllegalArgumentException(
                    "Caverna precisa de ID para ser atualizada.");
        }

        Caverna existente = repository.buscarPorCodCadastroAmbiental(
                caverna.getCodCadastroAmbiental());
        if (existente != null && !existente.getId().equals(caverna.getId())) {
            throw new IllegalArgumentException(
                    "Já existe outra caverna cadastrada com o código ambiental "
                            + caverna.getCodCadastroAmbiental());
        }

        TransacaoExecutor.executar(entityManager, () -> repository.atualizar(caverna));
    }

    public void removerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        TransacaoExecutor.executar(entityManager, () -> repository.removerPorId(id));
    }

    private void validarCaverna(Caverna caverna) {
        if (caverna == null) {
            throw new IllegalArgumentException("Caverna não pode ser nula.");
        }
        if (caverna.getNomeOficial() == null || caverna.getNomeOficial().isBlank()) {
            throw new IllegalArgumentException("Nome oficial da caverna é obrigatório.");
        }
        if (caverna.getCodCadastroAmbiental() == null || caverna.getCodCadastroAmbiental().isBlank()) {
            throw new IllegalArgumentException("Código de cadastro ambiental é obrigatório.");
        }
        if (caverna.getUf() == null) {
            throw new IllegalArgumentException("UF da caverna é obrigatória.");
        }

        if (caverna.getCoordenadas() == null) {
            throw new IllegalArgumentException("Coordenadas da caverna são obrigatórias.");
        }

        if (caverna.getAcessoAtualmentePermitido() == null) {
            caverna.setAcessoAtualmentePermitido(false);
        }
    }
}
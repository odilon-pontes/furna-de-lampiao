package com.furnadelampiao.service;

import com.furnadelampiao.repository.PessoaRepository;
import com.furnadelampiao.domain.Pessoa;
import com.furnadelampiao.infra.TransacaoExecutor;

import javax.persistence.EntityManager;
import java.util.List;

public class PessoaService {
    private final EntityManager entityManager;
    private final PessoaRepository pessoaRepository;

    public PessoaService(EntityManager entityManager, PessoaRepository pessoaRepository) {
        this.entityManager = entityManager;
        this.pessoaRepository = pessoaRepository;
    }

    public void salvar(Pessoa p) {
        validarPessoa(p);
        TransacaoExecutor.executar(entityManager, () -> pessoaRepository.salvar(p));
    }

    public Pessoa buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        return pessoaRepository.buscarPorId(id);
    }

    public List<Pessoa> listarTodos() {
        return pessoaRepository.listarTodos();
    }

    public void atualizar(Pessoa p) {
        validarPessoa(p);
        if (p.getId() == null) {
            throw new IllegalArgumentException("Pessoa precisa de ID para ser atualizada.");
        }
        TransacaoExecutor.executar(entityManager, () -> pessoaRepository.atualizar(p));
    }

    public void removerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        TransacaoExecutor.executar(entityManager, () -> pessoaRepository.removerPorId(id));
    }

    private void validarPessoa(Pessoa p) {
        if (p == null) {
            throw new IllegalArgumentException(
                    "Pessoa não pode ser nula.");
        }

        if (p.getNome() == null || p.getNome().isBlank()) {
            throw new IllegalArgumentException(
                    "Nome de pessoa é obrigatório.");
        }

        if (p.getSituacaoAtiva() == null) {
            p.setSituacaoAtiva(true);
        }
    }
}
package com.furnadelampiao.service;


import com.furnadelampiao.Repository.PessoaRepository;
import com.furnadelampiao.domain.Pessoa;

import java.util.List;

public class PessoaService {
    private final PessoaRepository pessoaRepository;

    public PessoaService (PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public void salvar(Pessoa p) {
        pessoaRepository.salvar(p);
    }

    public Pessoa buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        return pessoaRepository.buscarPorId(id);
    }

    public List<Pessoa> listarTodos(){
        return pessoaRepository.listarTodos();
    }

    public void atualizar(Pessoa p) {
        validarPessoa(p);

        if (p.getId() == null) {
            throw new IllegalArgumentException(
                    "Pessoa precisa de ID para ser atualizada."
            );
        }
        pessoaRepository.atualizar(p);

    }

    public void removerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "ID não pode ser nulo."
            );
        }
        pessoaRepository.removerPorId(id);
    }

    private void validarPessoa(Pessoa p) {
        if (p == null) {
            throw new IllegalArgumentException(
                    "Pessoa não pode ser nula."
            );
        }

        if (p.getNome() == null || p.getNome().isBlank()){
            throw new IllegalArgumentException(
                    "Nome de pessoa é obrigatório."
            );
        }
    }
}

package com.furnadelampiao.service;

import com.furnadelampiao.Repository.PesquisadorRepository;
import com.furnadelampiao.Repository.PesquisadorRepositoryJpa;
import com.furnadelampiao.domain.Pesquisador;
import com.furnadelampiao.enums.Titulacao;

import java.util.List;

public class PesquisadorService {
    private final PesquisadorRepositoryJpa repository;

    public PesquisadorService(PesquisadorRepositoryJpa repository) {
        this.repository = repository;
    }

    public void cadastrar(Pesquisador pesquisador) {
        // regras específicas de pesquisador

        repository.salvar(pesquisador);
    }

    public List<Pesquisador> listarTodos() {
        return repository.listarTodos();
    }

    public List<Pesquisador> buscarPorTitulacao(Titulacao titulacao) {
        return repository.buscarPorTitulacao(titulacao);
    }
}

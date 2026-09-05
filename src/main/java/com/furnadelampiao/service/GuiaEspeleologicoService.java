package com.furnadelampiao.service;

import com.furnadelampiao.Repository.GuiaEspeleologicoRepository;
import com.furnadelampiao.Repository.GuiaEspeleologicoRepositoryJpa;
import com.furnadelampiao.domain.GuiaEspeleologico;

import java.util.List;

public class GuiaEspeleologicoService {
    private final GuiaEspeleologicoRepositoryJpa repository;

    public GuiaEspeleologicoService(GuiaEspeleologicoRepositoryJpa repository) {
        this.repository = repository;
    }

    public void cadastrar(GuiaEspeleologico guia) {
        // regras específicas do guia

        repository.salvar(guia);
    }

    public List<GuiaEspeleologico> listarTodos() {
        return repository.listarTodos();
    }
}

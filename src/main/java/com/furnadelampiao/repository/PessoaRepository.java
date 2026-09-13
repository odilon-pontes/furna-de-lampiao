package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Pessoa;

import javax.persistence.EntityManager;
import java.util.List;

public class PessoaRepository implements Repository<Pessoa, Long> {
    private final EntityManager entityManager;

    public PessoaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void salvar(Pessoa entidade) {
        entityManager.persist(entidade);
    }

    @Override
    public Pessoa buscarPorId(Long id) {
        return entityManager.find(Pessoa.class, id);
    }

    @Override
    public List<Pessoa> listarTodos() {
        return entityManager
                .createQuery("SELECT p FROM Pessoa p", Pessoa.class)
                .getResultList();
    }

    @Override
    public void atualizar(Pessoa entidade) {
        entityManager.merge(entidade);
    }

    @Override
    public void removerPorId(Long id) {

        Pessoa p = entityManager.find(Pessoa.class, id);

        if (p != null) {
            entityManager.remove(p);
        }
    }
}

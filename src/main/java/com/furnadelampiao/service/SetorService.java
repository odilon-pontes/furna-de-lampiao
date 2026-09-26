package com.furnadelampiao.service;

import com.furnadelampiao.repository.CavernaRepository;
import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.Setor;
import com.furnadelampiao.enums.NivelDificuldadeSetor;
import com.furnadelampiao.infra.TransacaoExecutor;
import com.furnadelampiao.repository.SetorRepository;
import com.furnadelampiao.repository.SetorRepositoryJpa;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class SetorService {

    private final EntityManager entityManager;
    private final SetorRepository repository;
    private final CavernaRepository cavernaRepository;

    public SetorService(EntityManager entityManager, SetorRepository repository, CavernaRepository cavernaRepository) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.cavernaRepository = cavernaRepository;
    }

    public void cadastrar(Setor setor) {
        validarSetor(setor);
        TransacaoExecutor.executar(entityManager, () -> repository.salvar(setor));
    }

    public Setor buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        return repository.buscarPorId(id);
    }

    public List<Setor> listarTodos() {
        return repository.listarTodos();
    }

    public List<Setor> listarPorCaverna(Long cavernaId) {
        if (cavernaId == null) {
            throw new IllegalArgumentException("ID da caverna não pode ser nulo.");
        }
        return repository.listarPorCaverna(cavernaId);
    }

    public List<Setor> listarPorNivelDificuldade(NivelDificuldadeSetor nivel) {
        if (nivel == null) {
            throw new IllegalArgumentException("Nível de dificuldade não pode ser nulo.");
        }
        return repository.listarPorNivelDificuldade(nivel);
    }

    public void atualizar(Setor setor) {
        validarSetor(setor);

        if (setor.getId() == null) {
            throw new IllegalArgumentException(
                    "Setor precisa de ID para ser atualizado.");
        }
        TransacaoExecutor.executar(entityManager, () -> repository.atualizar(setor));
    }

    public void removerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        TransacaoExecutor.executar(entityManager, () -> repository.removerPorId(id));
    }

    private void validarSetor(Setor setor) {
        if (setor == null) {
            throw new IllegalArgumentException("Setor não pode ser nulo.");
        }
        if (setor.getDenominacao() == null || setor.getDenominacao().isBlank()) {
            throw new IllegalArgumentException("Denominação do setor é obrigatória.");
        }
        if (setor.getNivelEstimadoDificuldade() == null) {
            throw new IllegalArgumentException("Nível estimado de dificuldade é obrigatório.");
        }
        if (setor.getCondicaoCorrente() == null) {
            throw new IllegalArgumentException("Condição corrente do setor é obrigatória.");
        }
        if (setor.getCaverna() == null || setor.getCaverna().getId() == null) {
            throw new IllegalArgumentException(
                    "Setor precisa estar associado a uma caverna já cadastrada.");
        }

        Caverna caverna = cavernaRepository.buscarPorId(setor.getCaverna().getId());
        if (caverna == null) {
            throw new IllegalArgumentException(
                    "Caverna associada (id=" + setor.getCaverna().getId() + ") não existe no banco.");
        }

        if (setor.getProfundidadeMaxima() != null
                && setor.getProfundidadeMaxima().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Profundidade máxima não pode ser negativa.");
        }
        if (setor.getExtensaoAproximada() != null
                && setor.getExtensaoAproximada().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Extensão aproximada não pode ser negativa.");
        }
        if (setor.getRiscoInundacao() != null
                && setor.getRiscoInundacao().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Risco de inundação não pode ser negativo.");
        }
    }
}
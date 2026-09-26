package com.furnadelampiao.service;

import com.furnadelampiao.domain.Amostra;
import com.furnadelampiao.repository.AmostraRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.List;

public class AmostraService {

    private final EntityManager entityManager;
    private final AmostraRepository amostraRepository;

    public AmostraService(
            EntityManager entityManager,
            AmostraRepository amostraRepository) {

        this.entityManager = entityManager;
        this.amostraRepository = amostraRepository;
    }

    public void cadastrar(Amostra amostra) {

        validarAmostra(amostra);

        Amostra existente = amostraRepository.buscarPorCodigoCampo(
                amostra.getCodCampo());

        if (existente != null) {
            throw new IllegalArgumentException(
                    "Já existe uma amostra com o código de campo: "
                            + amostra.getCodCampo());
        }

        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            amostraRepository.salvar(amostra);

            transaction.commit();

        } catch (RuntimeException e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }

    public Amostra buscarPorId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "O ID da amostra deve ser válido.");
        }

        return amostraRepository.buscarPorId(id);
    }

    public List<Amostra> listarTodos() {

        return amostraRepository.listarTodos();
    }

    public Amostra buscarPorCodigoCampo(String codCampo) {

        validarCodigoCampo(codCampo);

        return amostraRepository.buscarPorCodigoCampo(
                codCampo.trim());
    }

    public List<Amostra> buscarPorColetaCientificaId(
            Long coletaCientificaId) {

        if (coletaCientificaId == null
                || coletaCientificaId <= 0) {

            throw new IllegalArgumentException(
                    "O ID da coleta científica deve ser válido.");
        }

        return amostraRepository.buscarPorColetaCientificaId(
                coletaCientificaId);
    }

    public void atualizar(Amostra amostra) {

        if (amostra == null || amostra.getId() == null) {
            throw new IllegalArgumentException(
                    "A amostra e seu ID são obrigatórios.");
        }

        validarAmostra(amostra);

        Amostra existente = amostraRepository.buscarPorCodigoCampo(
                amostra.getCodCampo());

        if (existente != null
                && !existente.getId().equals(amostra.getId())) {

            throw new IllegalArgumentException(
                    "Já existe outra amostra com o código de campo: "
                            + amostra.getCodCampo());
        }

        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            amostraRepository.atualizar(amostra);

            transaction.commit();

        } catch (RuntimeException e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }

    public void removerPorId(Long id) {

        Amostra amostra = entityManager.find(Amostra.class, id);

        if (amostra == null) {
            throw new IllegalArgumentException(
                    "Amostra não encontrada para o ID: " + id);
        }

        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            amostraRepository.removerPorId(id);

            transaction.commit();

        } catch (RuntimeException e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }

    private void validarAmostra(Amostra amostra) {

        if (amostra == null) {
            throw new IllegalArgumentException(
                    "A amostra não pode ser nula.");
        }

        validarCodigoCampo(amostra.getCodCampo());

        if (amostra.getCategoriaAmostra() == null) {
            throw new IllegalArgumentException(
                    "A categoria da amostra é obrigatória.");
        }

        if (amostra.getVolume() == null
                || amostra.getVolume().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "O volume da amostra deve ser maior que zero.");
        }

        if (amostra.getUnidadeMedida() == null) {
            throw new IllegalArgumentException(
                    "A unidade de medida é obrigatória.");
        }

        if (amostra.getDataAcondicionamento() == null) {
            throw new IllegalArgumentException(
                    "A data de acondicionamento é obrigatória.");
        }

        if (amostra.getCondicaoAmostra() == null) {
            throw new IllegalArgumentException(
                    "A condição de conservação é obrigatória.");
        }

        if (amostra.getIndicacaoMaterialPerigoso() == null) {
            amostra.setIndicacaoMaterialPerigoso(false);
        }

        if (amostra.getColetaCientifica() == null) {
            throw new IllegalArgumentException(
                    "A coleta científica é obrigatória.");
        }
    }

    private void validarCodigoCampo(String codCampo) {

        if (codCampo == null || codCampo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O código de campo é obrigatório.");
        }
    }
}
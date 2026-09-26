package com.furnadelampiao.service;

import com.furnadelampiao.domain.Equipamento;
import com.furnadelampiao.enums.SituacaoOperacional;
import com.furnadelampiao.enums.TipoEquipamento;
import com.furnadelampiao.infra.TransacaoExecutor;
import com.furnadelampiao.repository.EquipamentoRepository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class EquipamentoService {

    private final EntityManager entityManager;
    private final EquipamentoRepository repository;

    public EquipamentoService(EntityManager entityManager, EquipamentoRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    public void cadastrar(Equipamento equipamento) {
        validarEquipamento(equipamento);

        Equipamento existente = repository.buscarPorCodPatrimonial(equipamento.getCodPatrimonial());
        if (existente != null) {
            throw new IllegalArgumentException(
                    "Já existe um equipamento cadastrado com o código patrimonial "
                            + equipamento.getCodPatrimonial());
        }

        if (equipamento.getSituacaoOperacional() == null) {
            equipamento.setSituacaoOperacional(SituacaoOperacional.DISPONIVEL);
        }

        TransacaoExecutor.executar(entityManager, () -> repository.salvar(equipamento));
    }

    public Equipamento buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        return repository.buscarPorId(id);
    }

    public List<Equipamento> listarTodos() {
        return repository.listarTodos();
    }

    public List<Equipamento> listarPorTipo(TipoEquipamento tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo não pode ser nulo.");
        }
        return repository.listarPorTipo(tipo);
    }

    public List<Equipamento> listarPorSituacaoOperacional(SituacaoOperacional situacao) {
        if (situacao == null) {
            throw new IllegalArgumentException("Situação operacional não pode ser nula.");
        }
        return repository.listarPorSituacaoOperacional(situacao);
    }

    public void atualizar(Equipamento equipamento) {
        validarEquipamento(equipamento);

        if (equipamento.getId() == null) {
            throw new IllegalArgumentException(
                    "Equipamento precisa de ID para ser atualizado.");
        }

        Equipamento existente = repository.buscarPorCodPatrimonial(equipamento.getCodPatrimonial());
        if (existente != null && !existente.getId().equals(equipamento.getId())) {
            throw new IllegalArgumentException(
                    "Já existe outro equipamento cadastrado com o código patrimonial "
                            + equipamento.getCodPatrimonial());
        }

        TransacaoExecutor.executar(entityManager, () -> repository.atualizar(equipamento));
    }

    public void removerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        TransacaoExecutor.executar(entityManager, () -> repository.removerPorId(id));
    }

    private void validarEquipamento(Equipamento equipamento) {
        if (equipamento == null) {
            throw new IllegalArgumentException("Equipamento não pode ser nulo.");
        }
        if (equipamento.getCodPatrimonial() == null || equipamento.getCodPatrimonial().isBlank()) {
            throw new IllegalArgumentException("Código patrimonial é obrigatório.");
        }
        if (equipamento.getNome() == null || equipamento.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do equipamento é obrigatório.");
        }
        if (equipamento.getTipo() == null) {
            throw new IllegalArgumentException("Tipo do equipamento é obrigatório.");
        }
        if (equipamento.getDataCompra() == null) {
            throw new IllegalArgumentException("Data de compra é obrigatória.");
        }
        if (equipamento.getDataCompra().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de compra não pode ser no futuro.");
        }
        if (equipamento.getDataUltimaManutencao() != null
                && equipamento.getDataUltimaManutencao().isBefore(equipamento.getDataCompra())) {
            throw new IllegalArgumentException(
                    "Data da última manutenção não pode ser anterior à data de compra.");
        }
        if (equipamento.getValorAquisicao() != null
                && equipamento.getValorAquisicao().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor de aquisição não pode ser negativo.");
        }

        if (equipamento.getIndicacaoCalibracao() == null) {
            equipamento.setIndicacaoCalibracao(false);
        }
    }
}
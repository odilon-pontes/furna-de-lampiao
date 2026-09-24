package com.furnadelampiao.service;

import com.furnadelampiao.domain.Movimentacao;
import com.furnadelampiao.repository.MovimentacaoRepository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MovimentacaoService {

    private final EntityManager entityManager;
    private final MovimentacaoRepository repository;

    public MovimentacaoService(
            EntityManager entityManager,
            MovimentacaoRepository repository
    ) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    public void cadastrar(Movimentacao movimentacao) {

        validarMovimentacao(movimentacao);

        verificarDisponibilidadeEquipamento(movimentacao);

        try {
            entityManager.getTransaction().begin();

            repository.salvar(movimentacao);

            entityManager.getTransaction().commit();

        } catch (RuntimeException e) {

            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    public Movimentacao buscarPorId(Long id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID da movimentação não pode ser nulo."
            );
        }

        return repository.buscarPorId(id);
    }

    public List<Movimentacao> listarTodos() {
        return repository.listarTodos();
    }

    public List<Movimentacao> buscarPorPessoaId(Long pessoaId) {

        if (pessoaId == null) {
            throw new IllegalArgumentException(
                    "ID da pessoa não pode ser nulo."
            );
        }

        return repository.buscarPorPessoaId(pessoaId);
    }

    public List<Movimentacao> buscarPorExpedicaoId(Long expedicaoId) {

        if (expedicaoId == null) {
            throw new IllegalArgumentException(
                    "ID da expedição não pode ser nulo."
            );
        }

        return repository.buscarPorExpedicaoId(expedicaoId);
    }

    public List<Movimentacao> buscarPorEquipamentoId(Long equipamentoId) {

        if (equipamentoId == null) {
            throw new IllegalArgumentException(
                    "ID do equipamento não pode ser nulo."
            );
        }

        return repository.buscarPorEquipamentoId(equipamentoId);
    }

    private void validarMovimentacao(Movimentacao movimentacao) {

        if (movimentacao == null) {
            throw new IllegalArgumentException(
                    "Movimentação não pode ser nula."
            );
        }

        if (movimentacao.getDataHoraRetirada() == null) {
            throw new IllegalArgumentException(
                    "Data e hora da retirada são obrigatórias."
            );
        }

        if (movimentacao.getDataPrevisaoDevolucao() == null) {
            throw new IllegalArgumentException(
                    "Data prevista de devolução é obrigatória."
            );
        }

        if (movimentacao.getEstadoSaida() == null) {
            throw new IllegalArgumentException(
                    "Estado de saída é obrigatório."
            );
        }

        if (movimentacao.getExpedicao() == null) {
            throw new IllegalArgumentException(
                    "Expedição é obrigatória."
            );
        }

        if (movimentacao.getEquipamento() == null) {
            throw new IllegalArgumentException(
                    "Equipamento é obrigatório."
            );
        }

        if (movimentacao.getPessoa() == null) {
            throw new IllegalArgumentException(
                    "Pessoa responsável é obrigatória."
            );
        }

        validarDatas(movimentacao);

        validarCustoAvaria(movimentacao);
    }

    private void validarDatas(Movimentacao movimentacao) {

        LocalDateTime dataHoraRetirada =
                movimentacao.getDataHoraRetirada();

        LocalDate dataPrevisaoDevolucao =
                movimentacao.getDataPrevisaoDevolucao();

        LocalDate dataDevolucao =
                movimentacao.getDataDevolucao();

        LocalDate dataRetirada =
                dataHoraRetirada.toLocalDate();

        if (dataPrevisaoDevolucao.isBefore(dataRetirada)) {
            throw new IllegalArgumentException(
                    "A previsão de devolução não pode ser anterior à retirada."
            );
        }

        if (dataDevolucao != null &&
                dataDevolucao.isBefore(dataRetirada)) {

            throw new IllegalArgumentException(
                    "A devolução não pode ser anterior à retirada."
            );
        }
    }

    private void validarCustoAvaria(Movimentacao movimentacao) {

        BigDecimal custoAvaria = movimentacao.getCustoAvaria();

        if (custoAvaria != null &&
                custoAvaria.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Custo da avaria não pode ser negativo."
            );
        }
    }

    private void verificarDisponibilidadeEquipamento(
            Movimentacao movimentacao
    ) {

        Long equipamentoId =
                movimentacao.getEquipamento().getId();

        if (equipamentoId == null) {
            throw new IllegalArgumentException(
                    "O equipamento deve possuir ID."
            );
        }

        boolean possuiMovimentacaoAtiva =
                repository.existeMovimentacaoAtivaPorEquipamentoId(
                        equipamentoId
                );

        if (possuiMovimentacaoAtiva) {
            throw new IllegalArgumentException(
                    "O equipamento já está associado a uma movimentação ativa."
            );
        }
    }
}
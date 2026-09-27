package com.furnadelampiao.service;

import com.furnadelampiao.domain.Relatorio;
import com.furnadelampiao.repository.RelatorioRepository;
import com.furnadelampiao.infra.TransacaoExecutor;

import javax.persistence.EntityManager;
import java.util.List;

public class RelatorioService {

    private final EntityManager entityManager;
    private final RelatorioRepository relatorioRepository;

    public RelatorioService(
            EntityManager entityManager,
            RelatorioRepository relatorioRepository) {

        this.entityManager = entityManager;
        this.relatorioRepository = relatorioRepository;
    }

    public void cadastrar(Relatorio relatorio) {

        validar(relatorio);

        if (relatorio.getExpedicao() == null) {
            throw new IllegalArgumentException(
                    "A expedição do relatório é obrigatória.");
        }

        Relatorio existente = relatorioRepository.buscarPorExpedicaoId(
                relatorio.getExpedicao().getId());

        if (existente != null) {
            throw new IllegalArgumentException(
                    "A expedição já possui um relatório.");
        }

        TransacaoExecutor.executar(entityManager, () -> relatorioRepository.salvar(relatorio));
    }

    public Relatorio buscarPorId(Long id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "O ID do relatório é obrigatório.");
        }

        return relatorioRepository.buscarPorId(id);
    }

    public Relatorio buscarPorExpedicaoId(Long expedicaoId) {

        if (expedicaoId == null) {
            throw new IllegalArgumentException(
                    "O ID da expedição é obrigatório.");
        }

        return relatorioRepository.buscarPorExpedicaoId(expedicaoId);
    }

    public List<Relatorio> listarTodos() {
        return relatorioRepository.listarTodos();
    }

    public void atualizar(Relatorio relatorio) {

        if (relatorio == null || relatorio.getId() == null) {
            throw new IllegalArgumentException(
                    "Relatório inválido.");
        }

        validar(relatorio);

        Relatorio existente = relatorioRepository.buscarPorId(relatorio.getId());

        if (existente == null) {
            throw new IllegalArgumentException(
                    "Relatório não encontrado.");
        }

        /*
         * Se a expedição estiver sendo alterada, verifica
         * se ela já possui outro relatório.
         */
        if (relatorio.getExpedicao() == null) {
            throw new IllegalArgumentException(
                    "A expedição do relatório é obrigatória.");
        }

        Relatorio relatorioDaExpedicao = relatorioRepository.buscarPorExpedicaoId(
                relatorio.getExpedicao().getId());

        if (relatorioDaExpedicao != null
                && !relatorioDaExpedicao.getId().equals(relatorio.getId())) {

            throw new IllegalArgumentException(
                    "A expedição já possui outro relatório.");
        }

        TransacaoExecutor.executar(entityManager, () -> relatorioRepository.atualizar(relatorio));
    }

    public void remover(Long id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "O ID do relatório é obrigatório.");
        }

        Relatorio relatorio = relatorioRepository.buscarPorId(id);

        if (relatorio == null) {
            throw new IllegalArgumentException(
                    "Relatório não encontrado.");
        }

        TransacaoExecutor.executar(entityManager, () -> relatorioRepository.removerPorId(id));
    }

    public byte[] buscarArquivo(Long id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "O ID do relatório é obrigatório.");
        }

        Relatorio relatorio = relatorioRepository.buscarPorId(id);

        if (relatorio == null) {
            throw new IllegalArgumentException(
                    "Relatório não encontrado.");
        }

        return relatorio.getArquivoCompleto();
    }

    private void validar(Relatorio relatorio) {

        if (relatorio == null) {
            throw new IllegalArgumentException(
                    "O relatório não pode ser nulo.");
        }

        if (relatorio.getTitulo() == null
                || relatorio.getTitulo().isBlank()) {

            throw new IllegalArgumentException(
                    "O título do relatório é obrigatório.");
        }

        if (relatorio.getResumo() == null
                || relatorio.getResumo().isBlank()) {

            throw new IllegalArgumentException(
                    "O resumo do relatório é obrigatório.");
        }

        if (relatorio.getDataSubmissao() == null) {
            throw new IllegalArgumentException(
                    "A data de submissão é obrigatória.");
        }

        if (relatorio.getNumeroTotalPaginas() == null
                || relatorio.getNumeroTotalPaginas() <= 0) {

            throw new IllegalArgumentException(
                    "O número total de páginas deve ser maior que zero.");
        }

        if (relatorio.getSituacaoRelatorioFinal() == null) {
            throw new IllegalArgumentException(
                    "A situação de aprovação é obrigatória.");
        }

        if (relatorio.getArquivoCompleto() == null
                || relatorio.getArquivoCompleto().length == 0) {

            throw new IllegalArgumentException(
                    "O arquivo completo do relatório é obrigatório.");
        }

        if (relatorio.getPublicacaoAutorizada() == null) {
            relatorio.setPublicacaoAutorizada(false);
        }
    }
}

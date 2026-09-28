package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Movimentacao;

import java.util.List;

public interface MovimentacaoRepository extends Repository<Movimentacao, Long> {
    List<Movimentacao> buscarPorPessoaId(Long id);

    List<Movimentacao> buscarPorExpedicaoId(Long id);

    List<Movimentacao> buscarPorEquipamentoId(Long id);

    boolean existeMovimentacaoAtivaPorEquipamentoId(Long id);
}

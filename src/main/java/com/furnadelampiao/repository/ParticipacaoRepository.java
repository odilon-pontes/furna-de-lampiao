package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Participacao;

import java.util.List;

public interface ParticipacaoRepository extends Repository<Participacao, Long> {
    List<Participacao> buscarPorPessoaId(Long id);

    List<Participacao> buscarPorExpedicaoId(Long id);
}

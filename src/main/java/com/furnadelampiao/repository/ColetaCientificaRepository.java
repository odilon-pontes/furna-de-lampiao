package com.furnadelampiao.repository;

import com.furnadelampiao.domain.ColetaCientifica;
import com.furnadelampiao.enums.MetodoEmpregado;
import com.furnadelampiao.enums.SituacaoValidacaoColeta;

import java.time.LocalDateTime;
import java.util.List;

public interface ColetaCientificaRepository
        extends Repository<ColetaCientifica, Long> {

    List<ColetaCientifica> buscarPorPesquisadorId(Long pesquisadorId);

    List<ColetaCientifica> buscarPorExpedicaoId(Long expedicaoId);

    List<ColetaCientifica> buscarPorSetorId(Long setorId);

    List<ColetaCientifica> buscarPorSituacaoValidacao(
            SituacaoValidacaoColeta situacao);

    List<ColetaCientifica> buscarPorMetodoEmpregado(
            MetodoEmpregado metodo);

    List<ColetaCientifica> buscarPorPeriodo(
            LocalDateTime inicio,
            LocalDateTime fim);
}

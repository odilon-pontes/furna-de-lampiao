package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.enums.SituacaoExpedicao;
import com.furnadelampiao.dto.ExpedicaoResumoDTO;
import java.time.LocalDate;

import java.util.List;

public interface ExpedicaoRepository extends Repository<Expedicao, Long> {

    Expedicao buscarPorCodigo(String codigo);

    List<Expedicao> listarPorCaverna(Long cavernaId);

    List<Expedicao> listarPorSituacao(SituacaoExpedicao situacao);

    List<ExpedicaoResumoDTO> listarResumoPorPeriodoESituacao(
            LocalDate inicio, LocalDate fim, SituacaoExpedicao situacao);
}
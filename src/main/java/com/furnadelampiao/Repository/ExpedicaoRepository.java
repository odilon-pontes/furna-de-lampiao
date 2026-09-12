package com.furnadelampiao.Repository;

import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.enums.SituacaoExpedicao;

import java.util.List;

public interface ExpedicaoRepository extends Repository<Expedicao, Long> {

    Expedicao buscarPorCodigo(String codigo);

    List<Expedicao> listarPorCaverna(Long cavernaId);

    List<Expedicao> listarPorSituacao(SituacaoExpedicao situacao);
}
package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Amostra;

import java.util.List;

public interface AmostraRepository extends  Repository<Amostra, Long> {

    List<Amostra> buscarPorColetaCientificaId(Long coletaCientificaId);

    Amostra buscarPorCodigoCampo(String codCampo);
}
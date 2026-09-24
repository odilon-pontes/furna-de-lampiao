package com.furnadelampiao.repository;

import com.furnadelampiao.domain.AutorizacaoAmbiental;

public interface AutorizacaoAmbientalRepository
        extends Repository<AutorizacaoAmbiental, Long> {

    long contarVigentesPorExpedicao(Long expedicaoId, Long autorizacaoId);
}
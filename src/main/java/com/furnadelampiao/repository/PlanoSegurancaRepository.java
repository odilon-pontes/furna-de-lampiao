package com.furnadelampiao.repository;

import com.furnadelampiao.domain.PlanoSeguranca;

public interface PlanoSegurancaRepository extends Repository<PlanoSeguranca, Long> {

    byte[] buscarMapaRotaPorId(Long id);
}
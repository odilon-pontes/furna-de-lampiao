package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Relatorio;

public interface RelatorioRepository extends Repository<Relatorio, Long>{
    Relatorio buscarPorExpedicaoId(Long expedicaoId);

    byte[] buscarArquivoPorRelatorioId(Long id);
}

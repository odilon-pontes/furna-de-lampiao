package com.furnadelampiao.repository;

import com.furnadelampiao.domain.GuiaEspeleologico;
import com.furnadelampiao.enums.NivelCertificacao;

import java.util.List;

public interface GuiaEspeleologicoRepository extends Repository<GuiaEspeleologico, Long> {

    List<GuiaEspeleologico> buscarPorNivelCertificacao(NivelCertificacao nivel);

    List<GuiaEspeleologico> listarCertificacoesVencidas();
}

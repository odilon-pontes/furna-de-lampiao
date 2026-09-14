package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Pesquisador;
import com.furnadelampiao.enums.Titulacao;

import java.util.List;

public interface PesquisadorRepository extends  Repository<Pesquisador, Long> {

    List<Pesquisador> buscarPorTitulacao(Titulacao titulacao);

    List<Pesquisador> buscarPorAreaPesquisa(String area);
}

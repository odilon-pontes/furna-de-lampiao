package com.furnadelampiao.Repository;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.enums.UnidadeFederativa;

import java.util.List;

public interface CavernaRepository extends Repository<Caverna, Long> {

    Caverna buscarPorCodCadastroAmbiental(String codCadastroAmbiental);

    List<Caverna> listarPorUf(UnidadeFederativa uf);

    List<Caverna> listarComAcessoPermitido();
}
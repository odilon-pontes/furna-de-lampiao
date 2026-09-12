package com.furnadelampiao.Repository;

import com.furnadelampiao.domain.Setor;
import com.furnadelampiao.enums.NivelDificuldadeSetor;

import java.util.List;

public interface SetorRepository extends Repository<Setor, Long> {

    List<Setor> listarPorCaverna(Long cavernaId);

    List<Setor> listarPorNivelDificuldade(NivelDificuldadeSetor nivel);
}
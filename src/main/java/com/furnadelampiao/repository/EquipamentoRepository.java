package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Equipamento;
import com.furnadelampiao.enums.SituacaoOperacional;
import com.furnadelampiao.enums.TipoEquipamento;

import java.util.List;
import java.time.LocalDate;

public interface EquipamentoRepository extends Repository<Equipamento, Long> {

    Equipamento buscarPorCodPatrimonial(String codPatrimonial);

    List<Equipamento> listarPorTipo(TipoEquipamento tipo);

    List<Equipamento> listarPorSituacaoOperacional(SituacaoOperacional situacao);

    List<Equipamento> listarDisponiveisEntre(LocalDate inicio, LocalDate fim);
}
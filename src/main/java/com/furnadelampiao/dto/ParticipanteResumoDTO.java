package com.furnadelampiao.dto;

import com.furnadelampiao.enums.PapelParticipante;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ParticipanteResumoDTO {

    private final Long pessoaId;
    private final String nome;
    private final PapelParticipante papel;
    private final LocalDate dataConfirmacao;
    private final Boolean presencaConfirmada;

    public ParticipanteResumoDTO(Long pessoaId, String nome, PapelParticipante papel,
            LocalDate dataConfirmacao, Boolean presencaConfirmada) {
        this.pessoaId = pessoaId;
        this.nome = nome;
        this.papel = papel;
        this.dataConfirmacao = dataConfirmacao;
        this.presencaConfirmada = presencaConfirmada;
    }
}
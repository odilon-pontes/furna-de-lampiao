package com.furnadelampiao.dto;

import com.furnadelampiao.enums.SituacaoExpedicao;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ExpedicaoDetalheDTO {

    private final Long id;
    private final String codigo;
    private final String titulo;
    private final String objetivo;
    private final String nomeCaverna;
    private final LocalDateTime inicioPrevisto;
    private final LocalDateTime terminoPrevisto;
    private final BigDecimal orcamentoAprovado;
    private final BigDecimal custoRealizado;
    private final Integer qtdMaxParticipantes;
    private final SituacaoExpedicao situacao;
    private final Boolean cancelamentoEmergencial;
    private List<ParticipanteResumoDTO> participantes = new ArrayList<>();

    public ExpedicaoDetalheDTO(Long id, String codigo, String titulo, String objetivo,
            String nomeCaverna, LocalDateTime inicioPrevisto,
            LocalDateTime terminoPrevisto, BigDecimal orcamentoAprovado,
            BigDecimal custoRealizado, Integer qtdMaxParticipantes,
            SituacaoExpedicao situacao, Boolean cancelamentoEmergencial) {
        this.id = id;
        this.codigo = codigo;
        this.titulo = titulo;
        this.objetivo = objetivo;
        this.nomeCaverna = nomeCaverna;
        this.inicioPrevisto = inicioPrevisto;
        this.terminoPrevisto = terminoPrevisto;
        this.orcamentoAprovado = orcamentoAprovado;
        this.custoRealizado = custoRealizado;
        this.qtdMaxParticipantes = qtdMaxParticipantes;
        this.situacao = situacao;
        this.cancelamentoEmergencial = cancelamentoEmergencial;
    }

    public void setParticipantes(List<ParticipanteResumoDTO> participantes) {
        this.participantes = participantes;
    }
}
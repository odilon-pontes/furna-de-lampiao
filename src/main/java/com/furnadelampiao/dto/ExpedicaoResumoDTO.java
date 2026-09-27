package com.furnadelampiao.dto;

import com.furnadelampiao.enums.SituacaoExpedicao;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExpedicaoResumoDTO {

    private final String codigo;
    private final String titulo;
    private final String nomeCaverna;
    private final LocalDateTime inicioPrevisto;
    private final LocalDateTime terminoPrevisto;
    private final SituacaoExpedicao situacao;

    public ExpedicaoResumoDTO(String codigo, String titulo, String nomeCaverna,
                               LocalDateTime inicioPrevisto, LocalDateTime terminoPrevisto,
                               SituacaoExpedicao situacao) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.nomeCaverna = nomeCaverna;
        this.inicioPrevisto = inicioPrevisto;
        this.terminoPrevisto = terminoPrevisto;
        this.situacao = situacao;
    }
}
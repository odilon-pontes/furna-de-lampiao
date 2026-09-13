package com.furnadelampiao.domain;

import com.furnadelampiao.enums.SituacaoExpedicao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expedicao", uniqueConstraints = @UniqueConstraint(name = "uk_expedicao_codigo", columnNames = "codigo"))

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expedicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, length = 30)
    private String codigo;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "objetivo", length = 500)
    private String objetivo;

    @Column(name = "inicio_previsto", nullable = false)
    private LocalDateTime inicioPrevisto;

    @Column(name = "termino_previsto", nullable = false)
    private LocalDateTime terminoPrevisto;

    @Column(name = "orcamento_aprovado", precision = 12, scale = 2)
    private BigDecimal orcamentoAprovado;

    @Column(name = "custo_realizado", precision = 12, scale = 2)
    private BigDecimal custoRealizado;

    @Column(name = "qtd_max_participantes", nullable = false)
    private int qtdMaxParticipantes;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false, length = 20)
    private SituacaoExpedicao situacao;

    @Builder.Default
    @Column(name = "cancelamento_emergencial", nullable = false)
    private boolean cancelamentoEmergencial = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caverna_id", nullable = false)
    private Caverna caverna;
}
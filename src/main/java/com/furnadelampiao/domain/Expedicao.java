package com.furnadelampiao.domain;

import com.furnadelampiao.enums.SituacaoExpedicao;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_expedicao", uniqueConstraints = @UniqueConstraint(name = "uk_expedicao_codigo", columnNames = "codigo"))

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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plano_seguranca_id", nullable = false, unique = true)
    private PlanoSeguranca planoSeguranca;

    @OneToMany(mappedBy = "expedicao", fetch = FetchType.LAZY)
    private List<AutorizacaoAmbiental> autorizacoesAmbientais;
}
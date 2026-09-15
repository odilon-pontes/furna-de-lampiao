package com.furnadelampiao.domain;

import com.furnadelampiao.enums.MetodoEmpregado;
import com.furnadelampiao.enums.SituacaoValidacaoColeta;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColetaCientifica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora_coleta", nullable = false)
    private LocalDateTime dataHoraColeta;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_empregado", nullable = false, length = 30)
    private MetodoEmpregado metodoEmpregado;

    @Column(name = "descricao_ponto", length = 500)
    private String descricaoPonto;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal temperatura;

    @Column(name = "umidade_relativa", nullable = false, precision = 5, scale = 2)
    private BigDecimal umidadeRelativa;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal profundidade;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao_validacao", nullable = false, length = 30)
    private SituacaoValidacaoColeta situacaoValidacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pesquisador_id", nullable = false)
    private Pesquisador pesquisador;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "expedicao_id", nullable = false)
    private Expedicao expedicao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "setor_id", nullable = false)
    private Setor setor;

}

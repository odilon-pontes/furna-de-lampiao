package com.furnadelampiao.domain;

import com.furnadelampiao.enums.EstadoRetorno;
import com.furnadelampiao.enums.EstadoSaida;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_movimentacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movimentacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dt_hora_retirada", nullable = false)
    private LocalDateTime dataHoraRetirada;

    @Column(name = "dt_previsao_devolucao", nullable = false)
    private LocalDate dataPrevisaoDevolucao;

    @Column(name = "dt_devolucao")
    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_saida", nullable = false, length = 30)
    private EstadoSaida estadoSaida;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_retorno", length = 30)
    private EstadoRetorno estadoRetorno;

    @Column(name = "custo_avaria", precision = 10, scale = 2)
    private BigDecimal custoAvaria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "expedicao_id", nullable = false)
    private Expedicao expedicao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;
}

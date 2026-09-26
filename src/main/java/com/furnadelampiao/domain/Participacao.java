package com.furnadelampiao.domain;

import com.furnadelampiao.enums.PapelParticipante;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_participacao", uniqueConstraints = {
                @UniqueConstraint(name = "uk_participacao_pessoa_expedicao", columnNames = { "pessoa_id",
                                "expedicao_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participacao {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "pessoa_id", nullable = false)
        private Pessoa pessoa;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "expedicao_id", nullable = false)
        private Expedicao expedicao;

        @Enumerated(EnumType.STRING)
        @Column(name = "papel_participante", nullable = false, length = 30)
        private PapelParticipante papelParticipante;

        @Column(name = "data_confirmacao", nullable = false)
        private LocalDate dataConfirmacao;

        @Column(name = "valor_diaria", nullable = false, precision = 12, scale = 2)
        private BigDecimal valorDiaria;

        @Column(name = "qtd_prevista_dias", nullable = false)
        private Integer qtdPrevistaDias;

        @Builder.Default
        @Column(name = "presenca_confirmada", nullable = false)
        private Boolean presencaConfirmada = false;

        @Column(name = "observacoes", length = 500)
        private String observacoes;
}
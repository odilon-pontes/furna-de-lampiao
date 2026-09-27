package com.furnadelampiao.domain;

import com.furnadelampiao.enums.SituacaoRelatorioFinal;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_relatorio", uniqueConstraints = {
                @UniqueConstraint(columnNames = "expedicao_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Relatorio {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 50)
        private String titulo;

        @Column(columnDefinition = "TEXT")
        private String resumo;

        @Column(name = "dt_submissao", nullable = false)
        private LocalDateTime dataSubmissao;

        @Column(name = "numero_paginas", nullable = false)
        private Integer numeroTotalPaginas;

        @Enumerated(EnumType.STRING)
        @Column(name = "situacao", nullable = false, length = 30)
        private SituacaoRelatorioFinal situacaoRelatorioFinal;

        @Lob
        @Basic(fetch = FetchType.LAZY)
        @Column(name = "arquivo_completo", nullable = false)
        private byte[] arquivoCompleto;

        @Builder.Default
        @Column(name = "publicacao_autorizada", nullable = false)
        private Boolean publicacaoAutorizada = false;

        @OneToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "expedicao_id", nullable = false, unique = true)
        private Expedicao expedicao;
}

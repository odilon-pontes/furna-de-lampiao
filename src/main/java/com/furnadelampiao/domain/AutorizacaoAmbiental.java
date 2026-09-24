package com.furnadelampiao.domain;

import com.furnadelampiao.enums.SituacaoAutorizacao;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "autorizacao_ambiental")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutorizacaoAmbiental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num", nullable = false)
    private int num;

    @Column(name = "orgao_emissor", nullable = false, length = 200)
    private String orgaoEmissor;

    @Column(name = "data_emissao", nullable = false)
    private LocalDate dataEmissao;

    @Column(name = "validade", nullable = false)
    private LocalDate validade;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false, length = 20)
    private SituacaoAutorizacao situacao;

    @Column(name = "observacoes", length = 1000)
    private String observacoes;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "arquivo_pdf_assinado")
    private byte[] arquivoPdfAssinado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "expedicao_id", nullable = false)
    private Expedicao expedicao;
}
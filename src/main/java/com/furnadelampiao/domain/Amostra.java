package com.furnadelampiao.domain;

import com.furnadelampiao.enums.CategoriaAmostra;
import com.furnadelampiao.enums.CondicaoConservacaoAmostra;
import com.furnadelampiao.enums.UnidadeMedida;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_amostra")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Amostra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cod_campo", nullable = false, unique = true)
    private String codCampo;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_amostra", nullable = false, length = 30)
    private CategoriaAmostra categoriaAmostra;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal volume;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidade_medida", length = 30, nullable = false)
    private UnidadeMedida unidadeMedida;

    @Column(name = "dt_acondicionamento", nullable = false)
    private LocalDateTime dataAcondicionamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "condicao_conservacao", length = 30, nullable = false)
    private CondicaoConservacaoAmostra condicaoAmostra;

    @Builder.Default
    @Column(name = "indicacao_material_perigoso", nullable = false)
    private Boolean indicacaoMaterialPerigoso = false;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "fotografia")
    private byte[] fotografia;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coleta_cientifica_id", nullable = false)
    private ColetaCientifica coletaCientifica;
}

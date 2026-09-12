package com.furnadelampiao.domain;

import com.furnadelampiao.enums.CondicaoSetor;
import com.furnadelampiao.enums.NivelDificuldadeSetor;
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
import java.math.BigDecimal;


@Entity
@Table(name = "setor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Setor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "denominacao", nullable = false, length = 100)
    private String denominacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_estimado_dificuldade", nullable = false, length = 20)
    private NivelDificuldadeSetor nivelEstimadoDificuldade;

    @Column(name = "profundidade_maxima", precision = 7, scale = 2)
    private BigDecimal profundidadeMaxima;

    @Column(name = "extensao_aproximada", precision = 10, scale = 2)
    private BigDecimal extensaoAproximada;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "risco_inundacao", precision = 5, scale = 2)
    private BigDecimal riscoInundacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "condicao_corrente", nullable = false, length = 20)
    private CondicaoSetor condicaoCorrente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caverna_id", nullable = false)
    private Caverna caverna;
}
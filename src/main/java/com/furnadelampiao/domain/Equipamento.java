package com.furnadelampiao.domain;

import com.furnadelampiao.enums.SituacaoOperacional;
import com.furnadelampiao.enums.TipoEquipamento;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "equipamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cod_patrimonial", nullable = false, unique = true, length = 30)
    private String codPatrimonial;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 30)
    private TipoEquipamento tipo;

    @Column(name = "fabricante", length = 100)
    private String fabricante;

    @Column(name = "valor_aquisicao", precision = 12, scale = 2)
    private BigDecimal valorAquisicao;

    @Column(name = "data_compra", nullable = false)
    private LocalDate dataCompra;

    @Column(name = "data_ultima_manutencao")
    private LocalDate dataUltimaManutencao;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao_operacional", nullable = false, length = 30)
    private SituacaoOperacional situacaoOperacional;

    @Builder.Default
    @Column(name = "indicacao_calibracao", nullable = false)
    private boolean indicacaoCalibracao = false;
}
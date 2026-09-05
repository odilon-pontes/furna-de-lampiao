package com.furnadelampiao.domain;

import com.furnadelampiao.enums.Titulacao;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Pesquisador extends Pessoa{
    @Column(name = "num_registro_institucional", nullable = false, unique = true)
    private String numRegistroInstitucional;

    @Column(name = "area_principal_pesquisa", nullable = false)
    private String areaPrincipalPesquisa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Titulacao titulacao;

    @Column(name = "valor_diario_bolsa", precision = 10, scale = 2)
    private BigDecimal valorDiarioBolsa;

    @Column(name = "qtd_expedicoes_concluidas")
    private Integer qtdExpedicoesConcluidas;
}

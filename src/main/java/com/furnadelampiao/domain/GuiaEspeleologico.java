package com.furnadelampiao.domain;

import com.furnadelampiao.enums.NivelCertificacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GuiaEspeleologico extends Pessoa {
    @Column(name = "num_credenciamento", nullable = false, unique = true)
    private String numCredenciamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_certificacao", nullable = false)
    private NivelCertificacao nivelCertificacao;

    @Column(name = "data_validade_certificacao", nullable = false)
    private LocalDate dataValidadeCertificacao;
}

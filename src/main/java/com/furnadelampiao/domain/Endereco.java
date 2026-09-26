package com.furnadelampiao.domain;

import com.furnadelampiao.enums.UnidadeFederativa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Column;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {
    @Column(name = "logradouro", length = 50)
    private String logradouro;

    @Column(name = "numero", length = 6)
    private String numero;

    @Column(name = "complemento", length = 40)
    private String complemento;

    @Column(name = "bairro", length = 30)
    private String bairro;

    @Column(name = "cidade", length = 30)
    private String cidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "uf", length = 2)
    private UnidadeFederativa uf;

    @Column(name = "cep", length = 9)
    private String cep;
}

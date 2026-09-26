package com.furnadelampiao.domain;

import com.furnadelampiao.enums.UnidadeFederativa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Column;

@Embeddable
@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "uf", length = 2)
    private UnidadeFederativa uf;

    private String cep;
}

package com.furnadelampiao.enums;

public enum UnidadeMedida {

    UNIDADE("Unidade", "un"),
    QUILOGRAMA("Quilograma", "kg"),
    GRAMA("Grama", "g"),
    LITRO("Litro", "L"),
    MILILITRO("Mililitro", "mL"),
    METRO("Metro", "m"),
    CENTIMETRO("Centímetro", "cm"),
    METRO_QUADRADO("Metro quadrado", "m²"),
    METRO_CUBICO("Metro cúbico", "m³");

    private final String descricao;
    private final String simbolo;

    UnidadeMedida(String descricao, String simbolo) {
        this.descricao = descricao;
        this.simbolo = simbolo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getSimbolo() {
        return simbolo;
    }
}
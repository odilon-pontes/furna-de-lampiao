package com.furnadelampiao.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;


@Embeddable
@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor
public class Localizacao {

    @Column(name = "latitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "datum_geodesico", nullable = false, length = 20)
    private String datumGeodesico;
}
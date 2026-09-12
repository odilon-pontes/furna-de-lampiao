package com.furnadelampiao.domain;

import com.furnadelampiao.enums.UnidadeFederativa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "caverna", uniqueConstraints = @UniqueConstraint(name = "uk_caverna_cod_cadastro_ambiental", columnNames = "cod_cadastro_ambiental"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Caverna {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "nome_oficial", nullable = false, length = 150)
        private String nomeOficial;

        @Column(name = "cod_cadastro_ambiental", nullable = false, length = 50)
        private String codCadastroAmbiental;

        @Column(name = "municipio", nullable = false, length = 100)
        private String municipio;

        @Enumerated(EnumType.STRING)
        @Column(name = "uf", nullable = false, length = 2)
        private UnidadeFederativa uf;

        @Embedded
        private Localizacao coordenadas;

        @Column(name = "altitude", precision = 7, scale = 2)
        private BigDecimal altitude;

        @Column(name = "extensao", precision = 10, scale = 2)
        private BigDecimal extensao;

        @Column(name = "data_ultima_inspecao")
        private LocalDate dataUltimaInspecao;

        @Builder.Default
        @Column(name = "acesso_atualmente_permitido", nullable = false)
        private boolean acessoAtualmentePermitido = false;

        @Builder.Default
        @OneToMany(mappedBy = "caverna", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
        private List<Setor> setores = new ArrayList<>();

        public void adicionarSetor(Setor setor) {
                setores.add(setor);
                setor.setCaverna(this);
        }

        public void removerSetor(Setor setor) {
                setores.remove(setor);
                setor.setCaverna(null);
        }
}
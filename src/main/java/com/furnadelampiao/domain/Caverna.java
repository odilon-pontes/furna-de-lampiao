package com.furnadelampiao.domain;

import com.furnadelampiao.enums.UnidadeFederativa;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_caverna", uniqueConstraints = @UniqueConstraint(name = "uk_caverna_cod_cadastro_ambiental", columnNames = "cod_cadastro_ambiental"))
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
        private Boolean acessoAtualmentePermitido = false;

        @Setter(AccessLevel.NONE)
        @Builder.Default
        @OneToMany(mappedBy = "caverna", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
        private List<Setor> setores = new ArrayList<>();

        public void adicionarSetor(Setor setor) {
                if (setor == null) {
                        throw new IllegalArgumentException("Setor não pode ser nulo.");
                }
                setores.add(setor);
                setor.setCaverna(this);
        }

        public void removerSetor(Setor setor) {
                if (setor == null) {
                        throw new IllegalArgumentException("Setor não pode ser nulo.");
                }
                setores.remove(setor);
                setor.setCaverna(null);
        }
}
package com.furnadelampiao.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_plano_seguranca")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanoSeguranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "plano_seguranca_procedimento_evacuacao",
            joinColumns = @JoinColumn(name = "plano_seguranca_id"))
    @OrderColumn(name = "ordem")
    @Column(name = "procedimento", nullable = false, length = 500)
    private List<String> procedimentosEvacuacao = new ArrayList<>();

    @Embedded
    private Localizacao pontoExternoEncontro;

    @Column(name = "tempo_max_sem_comunicacao_min", nullable = false)
    private Integer tempoMaxSemComunicacao;

    @Column(name = "telefone_emergencia", nullable = false, length = 20)
    private String telefoneEmergencia;

    @Builder.Default
    @Column(name = "necessidade_equipe_medica", nullable = false)
    private boolean necessidadeEquipeMedica = false;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "mapa_rota")
    private byte[] mapaRota;

    @Setter(AccessLevel.PACKAGE)
    @OneToOne(mappedBy = "planoSeguranca", fetch = FetchType.LAZY)
    private Expedicao expedicao;
}

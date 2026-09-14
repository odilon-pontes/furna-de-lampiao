package com.furnadelampiao.seed;

import com.furnadelampiao.domain.*;
import com.furnadelampiao.enums.*;
import com.furnadelampiao.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Popula o banco com dados de exemplo para desenvolvimento/demonstração.
 */
public class DatabaseSeeder {

    private final PessoaService pessoaService;
    private final PesquisadorService pesquisadorService;
    private final GuiaEspeleologicoService guiaEspeleologicoService;
    private final CavernaService cavernaService;
    private final ExpedicaoService expedicaoService;
    private final SetorService setorService;
    private final List<Caverna> cavernas = new ArrayList<>() ;

    public DatabaseSeeder(PessoaService pessoaService,
                          PesquisadorService pesquisadorService,
                          GuiaEspeleologicoService guiaEspeleologicoService,
                          CavernaService cavernaService,
                          ExpedicaoService expedicaoService,
                          SetorService setorService) {
        this.pessoaService = pessoaService;
        this.pesquisadorService = pesquisadorService;
        this.guiaEspeleologicoService = guiaEspeleologicoService;
        this.cavernaService = cavernaService;
        this.expedicaoService = expedicaoService;
        this.setorService = setorService;
    }

    public void seedAll() {
        seedPessoas();
        seedPesquisadores();
        seedGuiasEspeleologicos();
        seedCavernas();
        seedExpedicoes();
        seedSetores();
    }

    private void seedPessoas() {
        if (!pessoaService.listarTodos().isEmpty()) {
            System.out.println("[seed] Pessoa já possui registros — pulando.");
            return;
        }

        pessoaService.salvar(Pessoa.builder()
                .nome("Maria Amorim")
                .cpf("10293847561")
                .dataNasc(LocalDate.of(1990, 3, 12))
                .email("maria.amorim@example.com")
                .telefone("83998999900")
                .endereco(endereco("Av. Dom Pedro II", "3", null, "Torre", "João Pessoa", UnidadeFederativa.PB, "58040-020"))
                .build());

        pessoaService.salvar(Pessoa.builder()
                .nome("Rafael Nogueira")
                .cpf("22233344455")
                .dataNasc(LocalDate.of(1988, 11, 2))
                .email("rafael.nogueira@example.com")
                .telefone("83988776655")
                .endereco(endereco("Rua das Trincheiras", "120", "apto 302", "Trincheiras", "Campina Grande", UnidadeFederativa.PB, "58400-365"))
                .build());

        System.out.println("[seed] 2 registros de Pessoa criados.");
    }

    private void seedPesquisadores() {
        if (!pesquisadorService.listarTodos().isEmpty()) {
            System.out.println("[seed] Pesquisador já possui registros — pulando.");
            return;
        }

        pesquisadorService.cadastrar(Pesquisador.builder()
                .nome("João Silva")
                .cpf("12345678900")
                .dataNasc(LocalDate.of(1985, 7, 21))
                .email("joao.silva@example.com")
                .telefone("83998999999")
                .endereco(endereco("Rua João Pessoa", "45", null, "Centro", "João Pessoa", UnidadeFederativa.PB, "58010-040"))
                .numRegistroInstitucional("PES-001")
                .areaPrincipalPesquisa("Espeleologia")
                .titulacao(Titulacao.DOUTORADO)
                .valorDiarioBolsa(new BigDecimal("250.00"))
                .build());

        pesquisadorService.cadastrar(Pesquisador.builder()
                .nome("Beatriz Lins")
                .cpf("33344455566")
                .dataNasc(LocalDate.of(1992, 4, 9))
                .email("beatriz.lins@example.com")
                .telefone("83997665544")
                .endereco(endereco("Rua Maciel Pinheiro", "210", null, "Varadouro", "João Pessoa", UnidadeFederativa.PB, "58010-490"))
                .numRegistroInstitucional("PES-002")
                .areaPrincipalPesquisa("Biologia de cavernas")
                .titulacao(Titulacao.MESTRADO)
                .valorDiarioBolsa(new BigDecimal("180.00"))
                .build());

        pesquisadorService.cadastrar(Pesquisador.builder()
                .nome("Eduardo Farias")
                .cpf("44455566677")
                .dataNasc(LocalDate.of(1979, 1, 30))
                .email("eduardo.farias@example.com")
                .telefone("83996655443")
                .endereco(endereco("Av. Epitácio Pessoa", "800", "bloco B", "Bairro dos Estados", "João Pessoa", UnidadeFederativa.PB, "58030-000"))
                .numRegistroInstitucional("PES-003")
                .areaPrincipalPesquisa("Geologia")
                .titulacao(Titulacao.POS_DOUTORADO)
                .valorDiarioBolsa(new BigDecimal("320.00"))
                .build());

        System.out.println("[seed] 3 registros de Pesquisador criados.");
    }

    private void seedGuiasEspeleologicos() {
        if (!guiaEspeleologicoService.listarTodos().isEmpty()) {
            System.out.println("[seed] GuiaEspeleologico já possui registros — pulando.");
            return;
        }

        guiaEspeleologicoService.cadastrar(GuiaEspeleologico.builder()
                .nome("Carlos Eduardo")
                .cpf("12345678990")
                .dataNasc(LocalDate.of(1985, 7, 20))
                .email("carlos.eduardo@example.com")
                .telefone("83999999999")
                .endereco(endereco("Rua Diogo Velho", "58", null, "Cruz das Armas", "João Pessoa", UnidadeFederativa.PB, "58085-020"))
                .numCredenciamento("GUIA-001")
                .nivelCertificacao(NivelCertificacao.NIVEL_II)
                .dataValidadeCertificacao(LocalDate.of(2027, 12, 31))
                .qtdExpedicoesConcluidas(15)
                .build());

        guiaEspeleologicoService.cadastrar(GuiaEspeleologico.builder()
                .nome("Fernanda Costa")
                .cpf("55566677788")
                .dataNasc(LocalDate.of(1994, 9, 5))
                .email("fernanda.costa@example.com")
                .telefone("83995544332")
                .endereco(endereco("Rua Peregrino de Carvalho", "77", null, "Jaguaribe", "João Pessoa", UnidadeFederativa.PB, "58015-430"))
                .numCredenciamento("GUIA-002")
                .nivelCertificacao(NivelCertificacao.NIVEL_III)
                .dataValidadeCertificacao(LocalDate.of(2026, 6, 15))
                .qtdExpedicoesConcluidas(32)
                .build());

        System.out.println("[seed] 2 registros de GuiaEspeleologico criados.");
    }

    private void seedCavernas() {
        if (!cavernaService.listarTodos().isEmpty()) {
            System.out.println("[seed] Expedição já possui registros — pulando.");
            return;
        }
        cavernas.add(Caverna.builder()
                .nomeOficial("Caverna do Diabo")
                .codCadastroAmbiental("CAV-001")
                .municipio("Eldorado")
                .uf(UnidadeFederativa.SP)
                .coordenadas(new Localizacao(
                        new BigDecimal("-24.5278"),
                        new BigDecimal("-48.6986"),
                        "SIRGAS2000"
                ))
                .altitude(new BigDecimal("540.00"))
                .extensao(new BigDecimal("6320.50"))
                .dataUltimaInspecao(LocalDate.of(2026, 3, 10))
                .acessoAtualmentePermitido(true)
                .build());

        cavernas.add(Caverna.builder()
                .nomeOficial("Caverna Santana")
                .codCadastroAmbiental("CAV-002")
                .municipio("Iporanga")
                .uf(UnidadeFederativa.SP)
                .coordenadas(new Localizacao(
                        new BigDecimal("-24.5167"),
                        new BigDecimal("-48.6992"),
                        "SIRGAS2000"
                ))
                .altitude(new BigDecimal("680.00"))
                .extensao(new BigDecimal("5040.75"))
                .dataUltimaInspecao(LocalDate.of(2026, 5, 20))
                .acessoAtualmentePermitido(true)
                .build());
        for (Caverna c : cavernas) {
            cavernaService.cadastrar(c);
        }
        System.out.println("[seed] 2 registros de Caverna criados.");

    }
    private void seedExpedicoes() {
        if (!expedicaoService.listarTodos().isEmpty()) {
            System.out.println("[seed] Expedição já possui registros — pulando.");
            return;
        }

        expedicaoService.cadastrar(Expedicao.builder()
                .codigo("EXP-2026-001")
                .titulo("Mapeamento inicial da Furna de Lampião")
                .objetivo("Levantamento topográfico e coleta de amostras minerais")
                .inicioPrevisto(LocalDateTime.of(2026, 10, 1, 8, 0))
                .terminoPrevisto(LocalDateTime.of(2026, 10, 3, 18, 0))
                .orcamentoAprovado(new BigDecimal("15000.00"))
                .qtdMaxParticipantes(6)
                .situacao(SituacaoExpedicao.PLANEJADA)
                .cancelamentoEmergencial(false)
                .caverna(cavernas.get(0))
                .build());

        expedicaoService.cadastrar(Expedicao.builder()
                .codigo("EXP-2026-002")
                .titulo("Exploração da Caverna Santana")
                .objetivo("Exploração espeleológica e levantamento de novas galerias")
                .inicioPrevisto(LocalDateTime.of(2026, 11, 10, 7, 30))
                .terminoPrevisto(LocalDateTime.of(2026, 11, 12, 17, 0))
                .orcamentoAprovado(new BigDecimal("12000.00"))
                .qtdMaxParticipantes(8)
                .situacao(SituacaoExpedicao.PLANEJADA)
                .cancelamentoEmergencial(false)
                .caverna(cavernas.get(1))
                .build());

        expedicaoService.cadastrar(Expedicao.builder()
                .codigo("EXP-2026-003")
                .titulo("Inspeção da Gruta da Pratinha")
                .objetivo("Avaliação das condições de acesso e levantamento ambiental")
                .inicioPrevisto(LocalDateTime.of(2026, 12, 5, 9, 0))
                .terminoPrevisto(LocalDateTime.of(2026, 12, 5, 16, 0))
                .orcamentoAprovado(new BigDecimal("7500.00"))
                .qtdMaxParticipantes(5)
                .situacao(SituacaoExpedicao.PLANEJADA)
                .cancelamentoEmergencial(false)
                .caverna(cavernas.get(0))
                .build());

        System.out.println("[seed] 3 registros de Expedição criados.");

    }
    private void seedSetores() {
        if (!setorService.listarTodos().isEmpty()) {
            System.out.println("[seed] Expedição já possui registros — pulando.");
            return;
        }

        setorService.cadastrar(Setor.builder()
                .denominacao("Galeria Principal")
                .nivelEstimadoDificuldade(NivelDificuldadeSetor.MODERADO)
                .profundidadeMaxima(new BigDecimal("12.00"))
                .extensaoAproximada(new BigDecimal("150.00"))
                .descricao("Trecho de entrada, com passagem alta e piso irregular")
                .riscoInundacao(new BigDecimal("10.00"))
                .condicaoCorrente(CondicaoSetor.DISPONIVEL)
                .caverna(cavernas.get(0))
                .build());

        setorService.cadastrar(Setor.builder()
                .denominacao("Galeria das Águas")
                .nivelEstimadoDificuldade(NivelDificuldadeSetor.ALTO)
                .profundidadeMaxima(new BigDecimal("28.50"))
                .extensaoAproximada(new BigDecimal("320.00"))
                .descricao("Galeria com trechos estreitos e presença de fluxo de água")
                .riscoInundacao(new BigDecimal("35.00"))
                .condicaoCorrente(CondicaoSetor.DISPONIVEL)
                .caverna(cavernas.get(1))
                .build());

        setorService.cadastrar(Setor.builder()
                .denominacao("Salão dos Cristais")
                .nivelEstimadoDificuldade(NivelDificuldadeSetor.BAIXO)
                .profundidadeMaxima(new BigDecimal("8.00"))
                .extensaoAproximada(new BigDecimal("210.00"))
                .descricao("Salão amplo com formações minerais e acesso facilitado")
                .riscoInundacao(new BigDecimal("5.00"))
                .condicaoCorrente(CondicaoSetor.DISPONIVEL)
                .caverna(cavernas.get(1))
                .build());

        System.out.println("[seed] 3 registros de Setor criados.");

    }
    private Endereco endereco(String logradouro, String numero, String complemento,
                              String bairro, String cidade, UnidadeFederativa uf, String cep) {
        return new Endereco(logradouro, numero, complemento, bairro, cidade, uf, cep);
    }


}
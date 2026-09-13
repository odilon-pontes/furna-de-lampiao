package com.furnadelampiao.seed;

import com.furnadelampiao.domain.Endereco;
import com.furnadelampiao.domain.GuiaEspeleologico;
import com.furnadelampiao.domain.Pesquisador;
import com.furnadelampiao.domain.Pessoa;
import com.furnadelampiao.enums.NivelCertificacao;
import com.furnadelampiao.enums.Titulacao;
import com.furnadelampiao.enums.UnidadeFederativa;
import com.furnadelampiao.service.GuiaEspeleologicoService;
import com.furnadelampiao.service.PesquisadorService;
import com.furnadelampiao.service.PessoaService;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Popula o banco com dados de exemplo para desenvolvimento/demonstração.
 */
public class DatabaseSeeder {

    private final PessoaService pessoaService;
    private final PesquisadorService pesquisadorService;
    private final GuiaEspeleologicoService guiaEspeleologicoService;

    public DatabaseSeeder(PessoaService pessoaService,
                          PesquisadorService pesquisadorService,
                          GuiaEspeleologicoService guiaEspeleologicoService) {
        this.pessoaService = pessoaService;
        this.pesquisadorService = pesquisadorService;
        this.guiaEspeleologicoService = guiaEspeleologicoService;
    }

    public void seedAll() {
        seedPessoas();
        seedPesquisadores();
        seedGuiasEspeleologicos();
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

    private Endereco endereco(String logradouro, String numero, String complemento,
                              String bairro, String cidade, UnidadeFederativa uf, String cep) {
        return new Endereco(logradouro, numero, complemento, bairro, cidade, uf, cep);
    }
}
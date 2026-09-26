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
        private final ParticipacaoService participacaoService;
        private final ColetaCientificaService coletaCientificaService;
        private final EquipamentoService equipamentoService;
        private final PlanoSegurancaService planoSegurancaService;
        private final MovimentacaoService movimentacaoService;
        private final AmostraService amostraService;
        private final AutorizacaoAmbientalService autorizacaoAmbientalService;

        private final List<Pessoa> pessoas = new ArrayList<>();
        private final List<Pesquisador> pesquisadores = new ArrayList<>();
        private final List<GuiaEspeleologico> guias = new ArrayList<>();
        private final List<Caverna> cavernas = new ArrayList<>();
        private final List<Setor> setores = new ArrayList<>();
        private final List<Expedicao> expedicoes = new ArrayList<>();
        private final List<Participacao> participacoes = new ArrayList<>();
        private final List<ColetaCientifica> coletas = new ArrayList<>();
        private final List<Equipamento> equipamentos = new ArrayList<>();
        private final List<PlanoSeguranca> planosSeguranca = new ArrayList<>();
        private final List<Movimentacao> movimentacoes = new ArrayList<>();
        private final List<Amostra> amostras = new ArrayList<>();
        private final List<AutorizacaoAmbiental> autorizacoesAmbientais = new ArrayList<>();

        public DatabaseSeeder(PessoaService pessoaService,
                        PesquisadorService pesquisadorService,
                        GuiaEspeleologicoService guiaEspeleologicoService,
                        CavernaService cavernaService,
                        ExpedicaoService expedicaoService,
                        SetorService setorService,
                        ParticipacaoService participacaoService,
                        ColetaCientificaService coletaCientificaService,
                        EquipamentoService equipamentoService,
                        PlanoSegurancaService planoSegurancaService,
                        MovimentacaoService movimentacaoService,
                        AmostraService amostraService,
                        AutorizacaoAmbientalService autorizacaoAmbientalService) {
                this.pessoaService = pessoaService;
                this.pesquisadorService = pesquisadorService;
                this.guiaEspeleologicoService = guiaEspeleologicoService;
                this.cavernaService = cavernaService;
                this.expedicaoService = expedicaoService;
                this.setorService = setorService;
                this.participacaoService = participacaoService;
                this.coletaCientificaService = coletaCientificaService;
                this.equipamentoService = equipamentoService;
                this.planoSegurancaService = planoSegurancaService;
                this.movimentacaoService = movimentacaoService;
                this.amostraService = amostraService;
                this.autorizacaoAmbientalService = autorizacaoAmbientalService;
        }

        public void seedAll() {
                seedPessoas();
                seedPesquisadores();
                seedGuiasEspeleologicos();
                seedCavernas();
                seedSetores();
                seedPlanosSeguranca();
                seedExpedicoes();
                seedExpedicaoSetores();
                seedAutorizacoesAmbientais();
                seedParticipacoes();
                seedColetas();
                seedEquipamentos();
                seedMovimentacoes();
                seedAmostras();
        }

        private void seedPessoas() {
                if (!pessoaService.listarTodos().isEmpty()) {
                        System.out.println("[seed] Pessoa já possui registros — pulando.");
                        return;
                }

                pessoas.add(Pessoa.builder()
                                .nome("Maria Amorim")
                                .cpf("10293847561")
                                .dataNasc(LocalDate.of(1990, 3, 12))
                                .email("maria.amorim@example.com")
                                .telefone("83998999900")
                                .endereco(endereco("Av. Dom Pedro II", "3", null, "Torre",
                                                "João Pessoa", UnidadeFederativa.PB, "58040-020"))
                                .build());

                pessoas.add(Pessoa.builder()
                                .nome("Rafael Nogueira")
                                .cpf("22233344450")
                                .dataNasc(LocalDate.of(1988, 11, 2))
                                .email("rafael.nogueira@example.com")
                                .telefone("83988776655")
                                .endereco(endereco("Rua das Trincheiras", "120", "apto 302",
                                                "Trincheiras", "Campina Grande", UnidadeFederativa.PB, "58400-365"))
                                .build());

                pessoas.add(Pessoa.builder()
                                .nome("Lucas Ferreira")
                                .cpf("34567890122")
                                .dataNasc(LocalDate.of(1995, 6, 18))
                                .email("lucas.ferreira@example.com")
                                .telefone("83991234567")
                                .endereco(endereco("Rua Epitácio Pessoa", "250", null,
                                                "Centro", "João Pessoa", UnidadeFederativa.PB, "58013-420"))
                                .build());

                pessoas.add(Pessoa.builder()
                                .nome("Ana Beatriz Lima")
                                .cpf("45678901233")
                                .dataNasc(LocalDate.of(1993, 8, 27))
                                .email("ana.lima@example.com")
                                .telefone("83992345678")
                                .endereco(endereco("Rua José Américo", "88", null,
                                                "Bancários", "João Pessoa", UnidadeFederativa.PB, "58051-110"))
                                .build());

                pessoas.add(Pessoa.builder()
                                .nome("Pedro Henrique")
                                .cpf("56789012344")
                                .dataNasc(LocalDate.of(1987, 2, 14))
                                .email("pedro.henrique@example.com")
                                .telefone("83993456789")
                                .endereco(endereco("Av. Floriano Peixoto", "410", null,
                                                "Centro", "Campina Grande", UnidadeFederativa.PB, "58400-165"))
                                .build());

                pessoas.add(Pessoa.builder()
                                .nome("Juliana Martins")
                                .cpf("67890123455")
                                .dataNasc(LocalDate.of(1991, 10, 9))
                                .email("juliana.martins@example.com")
                                .telefone("83994567890")
                                .endereco(endereco("Rua Manoel Deodato", "155", "casa 2",
                                                "Torre", "João Pessoa", UnidadeFederativa.PB, "58040-180"))
                                .build());

                pessoas.add(Pessoa.builder()
                                .nome("André Oliveira")
                                .cpf("78901234566")
                                .dataNasc(LocalDate.of(1984, 12, 22))
                                .email("andre.oliveira@example.com")
                                .telefone("83995678901")
                                .endereco(endereco("Rua João Machado", "90", null,
                                                "Centro", "João Pessoa", UnidadeFederativa.PB, "58010-250"))
                                .build());

                pessoas.add(Pessoa.builder()
                                .nome("Camila Rodrigues")
                                .cpf("89012345677")
                                .dataNasc(LocalDate.of(1996, 5, 3))
                                .email("camila.rodrigues@example.com")
                                .telefone("83996789012")
                                .endereco(endereco("Rua Bancário Sérgio Guerra", "320", null,
                                                "Bancários", "João Pessoa", UnidadeFederativa.PB, "58051-520"))
                                .build());

                pessoas.add(Pessoa.builder()
                                .nome("Thiago Almeida")
                                .cpf("90123456788")
                                .dataNasc(LocalDate.of(1989, 9, 16))
                                .email("thiago.almeida@example.com")
                                .telefone("83997890123")
                                .endereco(endereco("Rua Aprígio Veloso", "600", null,
                                                "Universitário", "Campina Grande", UnidadeFederativa.PB, "58429-900"))
                                .build());

                pessoas.add(Pessoa.builder()
                                .nome("Larissa Santos")
                                .cpf("01234567899")
                                .dataNasc(LocalDate.of(1997, 1, 25))
                                .email("larissa.santos@example.com")
                                .telefone("83998901234")
                                .endereco(endereco("Rua Duque de Caxias", "175", null,
                                                "Centro", "João Pessoa", UnidadeFederativa.PB, "58010-821"))
                                .build());

                for (Pessoa p : pessoas) {
                        pessoaService.salvar(p);
                }

                System.out.println("[seed] 10 registros de Pessoa criados.");
        }

        private void seedPesquisadores() {
                if (!pesquisadorService.listarTodos().isEmpty()) {
                        System.out.println("[seed] Pesquisador já possui registros — pulando.");
                        return;
                }

                pesquisadores.add(Pesquisador.builder()
                                .nome("João Silva")
                                .cpf("12345678900")
                                .dataNasc(LocalDate.of(1985, 7, 21))
                                .email("joao.silva@example.com")
                                .telefone("83998999999")
                                .endereco(endereco("Rua João Pessoa", "45", null, "Centro",
                                                "João Pessoa", UnidadeFederativa.PB, "58010-040"))
                                .numRegistroInstitucional("PES-001")
                                .areaPrincipalPesquisa("Espeleologia")
                                .titulacao(Titulacao.DOUTORADO)
                                .valorDiarioBolsa(new BigDecimal("250.00"))
                                .build());

                pesquisadores.add(Pesquisador.builder()
                                .nome("Beatriz Lins")
                                .cpf("33344455566")
                                .dataNasc(LocalDate.of(1992, 4, 9))
                                .email("beatriz.lins@example.com")
                                .telefone("83997665544")
                                .endereco(endereco("Rua Maciel Pinheiro", "210", null, "Varadouro",
                                                "João Pessoa", UnidadeFederativa.PB, "58010-490"))
                                .numRegistroInstitucional("PES-002")
                                .areaPrincipalPesquisa("Biologia de cavernas")
                                .titulacao(Titulacao.MESTRADO)
                                .valorDiarioBolsa(new BigDecimal("180.00"))
                                .build());

                pesquisadores.add(Pesquisador.builder()
                                .nome("Eduardo Farias")
                                .cpf("44455566677")
                                .dataNasc(LocalDate.of(1979, 1, 30))
                                .email("eduardo.farias@example.com")
                                .telefone("83996655443")
                                .endereco(endereco("Av. Epitácio Pessoa", "800", "bloco B",
                                                "Bairro dos Estados", "João Pessoa", UnidadeFederativa.PB, "58030-000"))
                                .numRegistroInstitucional("PES-003")
                                .areaPrincipalPesquisa("Geologia")
                                .titulacao(Titulacao.POS_DOUTORADO)
                                .valorDiarioBolsa(new BigDecimal("320.00"))
                                .build());

                pesquisadores.add(Pesquisador.builder()
                                .nome("Mariana Costa")
                                .cpf("55667788990")
                                .dataNasc(LocalDate.of(1988, 5, 14))
                                .email("mariana.costa@example.com")
                                .telefone("83991112233")
                                .endereco(endereco("Rua das Acácias", "120", null, "Manaíra",
                                                "João Pessoa", UnidadeFederativa.PB, "58038-170"))
                                .numRegistroInstitucional("PES-004")
                                .areaPrincipalPesquisa("Arqueologia")
                                .titulacao(Titulacao.DOUTORADO)
                                .valorDiarioBolsa(new BigDecimal("290.00"))
                                .build());

                pesquisadores.add(Pesquisador.builder()
                                .nome("Felipe Andrade")
                                .cpf("66778899001")
                                .dataNasc(LocalDate.of(1990, 3, 28))
                                .email("felipe.andrade@example.com")
                                .telefone("83992223344")
                                .endereco(endereco("Rua Almeida Barreto", "75", null, "Centro",
                                                "Campina Grande", UnidadeFederativa.PB, "58400-250"))
                                .numRegistroInstitucional("PES-005")
                                .areaPrincipalPesquisa("Geografia")
                                .titulacao(Titulacao.MESTRADO)
                                .valorDiarioBolsa(new BigDecimal("210.00"))
                                .build());

                pesquisadores.add(Pesquisador.builder()
                                .nome("Patrícia Gomes")
                                .cpf("77889900112")
                                .dataNasc(LocalDate.of(1994, 11, 6))
                                .email("patricia.gomes@example.com")
                                .telefone("83993334455")
                                .endereco(endereco("Rua João Cabral", "190", null, "Tambauzinho",
                                                "João Pessoa", UnidadeFederativa.PB, "58042-180"))
                                .numRegistroInstitucional("PES-006")
                                .areaPrincipalPesquisa("Paleontologia")
                                .titulacao(Titulacao.MESTRADO)
                                .valorDiarioBolsa(new BigDecimal("195.00"))
                                .build());

                pesquisadores.add(Pesquisador.builder()
                                .nome("Ricardo Martins")
                                .cpf("88990011223")
                                .dataNasc(LocalDate.of(1982, 8, 19))
                                .email("ricardo.martins@example.com")
                                .telefone("83994445566")
                                .endereco(endereco("Av. Getúlio Vargas", "350", null, "Centro",
                                                "Campina Grande", UnidadeFederativa.PB, "58400-052"))
                                .numRegistroInstitucional("PES-007")
                                .areaPrincipalPesquisa("Geologia ambiental")
                                .titulacao(Titulacao.DOUTORADO)
                                .valorDiarioBolsa(new BigDecimal("275.00"))
                                .build());

                pesquisadores.add(Pesquisador.builder()
                                .nome("Renata Alves")
                                .cpf("99001122334")
                                .dataNasc(LocalDate.of(1993, 2, 11))
                                .email("renata.alves@example.com")
                                .telefone("83995556677")
                                .endereco(endereco("Rua Professora Alice", "64", null, "Bancários",
                                                "João Pessoa", UnidadeFederativa.PB, "58051-400"))
                                .numRegistroInstitucional("PES-008")
                                .areaPrincipalPesquisa("Ecologia")
                                .titulacao(Titulacao.MESTRADO)
                                .valorDiarioBolsa(new BigDecimal("185.00"))
                                .build());

                pesquisadores.add(Pesquisador.builder()
                                .nome("Gustavo Ribeiro")
                                .cpf("10112233445")
                                .dataNasc(LocalDate.of(1986, 6, 24))
                                .email("gustavo.ribeiro@example.com")
                                .telefone("83996667788")
                                .endereco(endereco("Rua Floriano Peixoto", "280", null, "Centro",
                                                "João Pessoa", UnidadeFederativa.PB, "58010-090"))
                                .numRegistroInstitucional("PES-009")
                                .areaPrincipalPesquisa("Cartografia")
                                .titulacao(Titulacao.POS_DOUTORADO)
                                .valorDiarioBolsa(new BigDecimal("350.00"))
                                .build());

                pesquisadores.add(Pesquisador.builder()
                                .nome("Aline Carvalho")
                                .cpf("21223344556")
                                .dataNasc(LocalDate.of(1991, 12, 7))
                                .email("aline.carvalho@example.com")
                                .telefone("83997778899")
                                .endereco(endereco("Rua Monsenhor Walfredo", "110", null, "Centro",
                                                "João Pessoa", UnidadeFederativa.PB, "58010-480"))
                                .numRegistroInstitucional("PES-010")
                                .areaPrincipalPesquisa("Conservação ambiental")
                                .titulacao(Titulacao.DOUTORADO)
                                .valorDiarioBolsa(new BigDecimal("300.00"))
                                .build());

                for (Pesquisador p : pesquisadores) {
                        pesquisadorService.cadastrar(p);
                }

                System.out.println("[seed] 10 registros de Pesquisador criados.");
        }

        private void seedGuiasEspeleologicos() {
                if (!guiaEspeleologicoService.listarTodos().isEmpty()) {
                        System.out.println("[seed] GuiaEspeleologico já possui registros — pulando.");
                        return;
                }

                guias.add(GuiaEspeleologico.builder()
                                .nome("Carlos Eduardo")
                                .cpf("12345678990")
                                .dataNasc(LocalDate.of(1985, 7, 20))
                                .email("carlos.eduardo@example.com")
                                .telefone("83999999999")
                                .endereco(endereco("Rua Diogo Velho", "58", null, "Cruz das Armas",
                                                "João Pessoa", UnidadeFederativa.PB, "58085-020"))
                                .numCredenciamento("GUIA-001")
                                .nivelCertificacao(NivelCertificacao.NIVEL_II)
                                .dataValidadeCertificacao(LocalDate.of(2027, 12, 31))
                                .qtdExpedicoesConcluidas(15)
                                .build());

                guias.add(GuiaEspeleologico.builder()
                                .nome("Fernanda Costa")
                                .cpf("55566677788")
                                .dataNasc(LocalDate.of(1994, 9, 5))
                                .email("fernanda.costa@example.com")
                                .telefone("83995544332")
                                .endereco(endereco("Rua Peregrino de Carvalho", "77", null, "Jaguaribe",
                                                "João Pessoa", UnidadeFederativa.PB, "58015-430"))
                                .numCredenciamento("GUIA-002")
                                .nivelCertificacao(NivelCertificacao.NIVEL_III)
                                .dataValidadeCertificacao(LocalDate.of(2026, 6, 15))
                                .qtdExpedicoesConcluidas(32)
                                .build());

                guias.add(GuiaEspeleologico.builder()
                                .nome("Marcelo Santos")
                                .cpf("66677788899")
                                .dataNasc(LocalDate.of(1987, 4, 17))
                                .email("marcelo.santos@example.com")
                                .telefone("83991122334")
                                .endereco(endereco("Rua da Aurora", "145", null, "Centro",
                                                "João Pessoa", UnidadeFederativa.PB, "58010-190"))
                                .numCredenciamento("GUIA-003")
                                .nivelCertificacao(NivelCertificacao.NIVEL_I)
                                .dataValidadeCertificacao(LocalDate.of(2027, 8, 20))
                                .qtdExpedicoesConcluidas(9)
                                .build());

                guias.add(GuiaEspeleologico.builder()
                                .nome("Juliana Freire")
                                .cpf("77788899900")
                                .dataNasc(LocalDate.of(1991, 1, 29))
                                .email("juliana.freire@example.com")
                                .telefone("83992233445")
                                .endereco(endereco("Rua Beira Rio", "220", null, "Altiplano",
                                                "João Pessoa", UnidadeFederativa.PB, "58046-180"))
                                .numCredenciamento("GUIA-004")
                                .nivelCertificacao(NivelCertificacao.NIVEL_II)
                                .dataValidadeCertificacao(LocalDate.of(2028, 2, 10))
                                .qtdExpedicoesConcluidas(21)
                                .build());

                guias.add(GuiaEspeleologico.builder()
                                .nome("Roberto Lima")
                                .cpf("88899900011")
                                .dataNasc(LocalDate.of(1983, 10, 12))
                                .email("roberto.lima@example.com")
                                .telefone("83993344556")
                                .endereco(endereco("Rua Carlos Gomes", "310", null, "Centro",
                                                "Campina Grande", UnidadeFederativa.PB, "58400-230"))
                                .numCredenciamento("GUIA-005")
                                .nivelCertificacao(NivelCertificacao.NIVEL_III)
                                .dataValidadeCertificacao(LocalDate.of(2028, 5, 18))
                                .qtdExpedicoesConcluidas(45)
                                .build());

                guias.add(GuiaEspeleologico.builder()
                                .nome("Camila Duarte")
                                .cpf("99900011122")
                                .dataNasc(LocalDate.of(1996, 7, 8))
                                .email("camila.duarte@example.com")
                                .telefone("83994455667")
                                .endereco(endereco("Rua das Palmeiras", "90", null, "Manaíra",
                                                "João Pessoa", UnidadeFederativa.PB, "58038-300"))
                                .numCredenciamento("GUIA-006")
                                .nivelCertificacao(NivelCertificacao.NIVEL_I)
                                .dataValidadeCertificacao(LocalDate.of(2027, 11, 5))
                                .qtdExpedicoesConcluidas(7)
                                .build());

                guias.add(GuiaEspeleologico.builder()
                                .nome("Daniel Barbosa")
                                .cpf("00011122233")
                                .dataNasc(LocalDate.of(1989, 5, 23))
                                .email("daniel.barbosa@example.com")
                                .telefone("83995566778")
                                .endereco(endereco("Rua São Miguel", "170", null, "Torre",
                                                "João Pessoa", UnidadeFederativa.PB, "58040-300"))
                                .numCredenciamento("GUIA-007")
                                .nivelCertificacao(NivelCertificacao.NIVEL_II)
                                .dataValidadeCertificacao(LocalDate.of(2028, 1, 15))
                                .qtdExpedicoesConcluidas(27)
                                .build());

                guias.add(GuiaEspeleologico.builder()
                                .nome("Priscila Moura")
                                .cpf("11122233344")
                                .dataNasc(LocalDate.of(1992, 11, 30))
                                .email("priscila.moura@example.com")
                                .telefone("83996677889")
                                .endereco(endereco("Rua do Sol", "210", null, "Centro",
                                                "João Pessoa", UnidadeFederativa.PB, "58010-100"))
                                .numCredenciamento("GUIA-008")
                                .nivelCertificacao(NivelCertificacao.NIVEL_III)
                                .dataValidadeCertificacao(LocalDate.of(2029, 3, 12))
                                .qtdExpedicoesConcluidas(38)
                                .build());

                guias.add(GuiaEspeleologico.builder()
                                .nome("Henrique Ramos")
                                .cpf("22233344455")
                                .dataNasc(LocalDate.of(1986, 3, 15))
                                .email("henrique.ramos@example.com")
                                .telefone("83997788990")
                                .endereco(endereco("Rua das Flores", "130", null, "Centro",
                                                "Iporanga", UnidadeFederativa.SP, "18330-000"))
                                .numCredenciamento("GUIA-009")
                                .nivelCertificacao(NivelCertificacao.NIVEL_II)
                                .dataValidadeCertificacao(LocalDate.of(2028, 9, 30))
                                .qtdExpedicoesConcluidas(29)
                                .build());

                guias.add(GuiaEspeleologico.builder()
                                .nome("Vanessa Oliveira")
                                .cpf("33344455560")
                                .dataNasc(LocalDate.of(1995, 8, 21))
                                .email("vanessa.oliveira@example.com")
                                .telefone("83998899001")
                                .endereco(endereco("Rua Principal", "55", null, "Centro",
                                                "Eldorado", UnidadeFederativa.SP, "11960-000"))
                                .numCredenciamento("GUIA-010")
                                .nivelCertificacao(NivelCertificacao.NIVEL_I)
                                .dataValidadeCertificacao(LocalDate.of(2027, 10, 22))
                                .qtdExpedicoesConcluidas(12)
                                .build());

                for (GuiaEspeleologico g : guias) {
                        guiaEspeleologicoService.cadastrar(g);
                }

                System.out.println("[seed] 10 registros de GuiaEspeleologico criados.");
        }

        private void seedCavernas() {
                if (!cavernaService.listarTodos().isEmpty()) {
                        System.out.println("[seed] Caverna já possui registros — pulando.");
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
                                                "SIRGAS2000"))
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
                                                "SIRGAS2000"))
                                .altitude(new BigDecimal("680.00"))
                                .extensao(new BigDecimal("5040.75"))
                                .dataUltimaInspecao(LocalDate.of(2026, 5, 20))
                                .acessoAtualmentePermitido(true)
                                .build());

                cavernas.add(Caverna.builder()
                                .nomeOficial("Gruta da Pratinha")
                                .codCadastroAmbiental("CAV-003")
                                .municipio("Iraquara")
                                .uf(UnidadeFederativa.BA)
                                .coordenadas(new Localizacao(
                                                new BigDecimal("-12.3833"),
                                                new BigDecimal("-41.5667"),
                                                "SIRGAS2000"))
                                .altitude(new BigDecimal("620.00"))
                                .extensao(new BigDecimal("1200.30"))
                                .dataUltimaInspecao(LocalDate.of(2026, 4, 8))
                                .acessoAtualmentePermitido(false)
                                .build());

                cavernas.add(Caverna.builder()
                                .nomeOficial("Gruta Azul")
                                .codCadastroAmbiental("CAV-004")
                                .municipio("Bonito")
                                .uf(UnidadeFederativa.MS)
                                .coordenadas(new Localizacao(
                                                new BigDecimal("-21.1261"),
                                                new BigDecimal("-56.4828"),
                                                "SIRGAS2000"))
                                .altitude(new BigDecimal("350.00"))
                                .extensao(new BigDecimal("890.40"))
                                .dataUltimaInspecao(LocalDate.of(2026, 2, 17))
                                .acessoAtualmentePermitido(true)
                                .build());

                cavernas.add(Caverna.builder()
                                .nomeOficial("Caverna do Lago Azul")
                                .codCadastroAmbiental("CAV-005")
                                .municipio("Bonito")
                                .uf(UnidadeFederativa.MS)
                                .coordenadas(new Localizacao(
                                                new BigDecimal("-21.1378"),
                                                new BigDecimal("-56.5792"),
                                                "SIRGAS2000"))
                                .altitude(new BigDecimal("340.00"))
                                .extensao(new BigDecimal("720.80"))
                                .dataUltimaInspecao(LocalDate.of(2026, 1, 25))
                                .acessoAtualmentePermitido(true)
                                .build());

                cavernas.add(Caverna.builder()
                                .nomeOficial("Gruta da Torrinha")
                                .codCadastroAmbiental("CAV-006")
                                .municipio("Iraquara")
                                .uf(UnidadeFederativa.BA)
                                .coordenadas(new Localizacao(
                                                new BigDecimal("-12.3167"),
                                                new BigDecimal("-41.6167"),
                                                "SIRGAS2000"))
                                .altitude(new BigDecimal("690.00"))
                                .extensao(new BigDecimal("4100.25"))
                                .dataUltimaInspecao(LocalDate.of(2026, 6, 12))
                                .acessoAtualmentePermitido(true)
                                .build());

                cavernas.add(Caverna.builder()
                                .nomeOficial("Gruta Lapa Doce")
                                .codCadastroAmbiental("CAV-007")
                                .municipio("Iraquara")
                                .uf(UnidadeFederativa.BA)
                                .coordenadas(new Localizacao(
                                                new BigDecimal("-12.3500"),
                                                new BigDecimal("-41.5667"),
                                                "SIRGAS2000"))
                                .altitude(new BigDecimal("640.00"))
                                .extensao(new BigDecimal("8500.00"))
                                .dataUltimaInspecao(LocalDate.of(2026, 5, 3))
                                .acessoAtualmentePermitido(true)
                                .build());

                cavernas.add(Caverna.builder()
                                .nomeOficial("Caverna da Onça")
                                .codCadastroAmbiental("CAV-008")
                                .municipio("Pains")
                                .uf(UnidadeFederativa.MG)
                                .coordenadas(new Localizacao(
                                                new BigDecimal("-20.3700"),
                                                new BigDecimal("-45.6700"),
                                                "SIRGAS2000"))
                                .altitude(new BigDecimal("720.00"))
                                .extensao(new BigDecimal("2750.60"))
                                .dataUltimaInspecao(LocalDate.of(2026, 7, 9))
                                .acessoAtualmentePermitido(false)
                                .build());

                cavernas.add(Caverna.builder()
                                .nomeOficial("Gruta do Janelão")
                                .codCadastroAmbiental("CAV-009")
                                .municipio("Januária")
                                .uf(UnidadeFederativa.MG)
                                .coordenadas(new Localizacao(
                                                new BigDecimal("-15.4700"),
                                                new BigDecimal("-44.3600"),
                                                "SIRGAS2000"))
                                .altitude(new BigDecimal("510.00"))
                                .extensao(new BigDecimal("4600.90"))
                                .dataUltimaInspecao(LocalDate.of(2026, 6, 28))
                                .acessoAtualmentePermitido(true)
                                .build());

                cavernas.add(Caverna.builder()
                                .nomeOficial("Gruta da Mangabeira")
                                .codCadastroAmbiental("CAV-010")
                                .municipio("Ituaçu")
                                .uf(UnidadeFederativa.BA)
                                .coordenadas(new Localizacao(
                                                new BigDecimal("-13.8100"),
                                                new BigDecimal("-41.3000"),
                                                "SIRGAS2000"))
                                .altitude(new BigDecimal("580.00"))
                                .extensao(new BigDecimal("3900.45"))
                                .dataUltimaInspecao(LocalDate.of(2026, 7, 20))
                                .acessoAtualmentePermitido(true)
                                .build());

                for (Caverna caverna : cavernas) {
                        cavernaService.cadastrar(caverna);
                }

                System.out.println("[seed] 10 registros de Caverna criados.");
        }

        private void seedSetores() {
                if (!setorService.listarTodos().isEmpty()) {
                        System.out.println("[seed] Setor já possui registros — pulando.");
                        return;
                }

                setores.add(Setor.builder()
                                .denominacao("Galeria Principal")
                                .nivelEstimadoDificuldade(NivelDificuldadeSetor.MODERADO)
                                .profundidadeMaxima(new BigDecimal("12.00"))
                                .extensaoAproximada(new BigDecimal("150.00"))
                                .descricao("Trecho de entrada, com passagem alta e piso irregular")
                                .riscoInundacao(new BigDecimal("10.00"))
                                .condicaoCorrente(CondicaoSetor.DISPONIVEL)
                                .caverna(cavernas.get(0))
                                .build());

                setores.add(Setor.builder()
                                .denominacao("Galeria das Águas")
                                .nivelEstimadoDificuldade(NivelDificuldadeSetor.ALTO)
                                .profundidadeMaxima(new BigDecimal("28.50"))
                                .extensaoAproximada(new BigDecimal("320.00"))
                                .descricao("Galeria com trechos estreitos e presença de fluxo de água")
                                .riscoInundacao(new BigDecimal("35.00"))
                                .condicaoCorrente(CondicaoSetor.DISPONIVEL)
                                .caverna(cavernas.get(1))
                                .build());

                setores.add(Setor.builder()
                                .denominacao("Salão dos Cristais")
                                .nivelEstimadoDificuldade(NivelDificuldadeSetor.BAIXO)
                                .profundidadeMaxima(new BigDecimal("8.00"))
                                .extensaoAproximada(new BigDecimal("210.00"))
                                .descricao("Salão amplo com formações minerais e acesso facilitado")
                                .riscoInundacao(new BigDecimal("5.00"))
                                .condicaoCorrente(CondicaoSetor.DISPONIVEL)
                                .caverna(cavernas.get(2))
                                .build());

                setores.add(Setor.builder()
                                .denominacao("Galeria Azul")
                                .nivelEstimadoDificuldade(NivelDificuldadeSetor.MODERADO)
                                .profundidadeMaxima(new BigDecimal("18.00"))
                                .extensaoAproximada(new BigDecimal("275.00"))
                                .descricao("Galeria com formações calcárias e trechos parcialmente alagados")
                                .riscoInundacao(new BigDecimal("20.00"))
                                .condicaoCorrente(CondicaoSetor.DISPONIVEL)
                                .caverna(cavernas.get(3))
                                .build());

                setores.add(Setor.builder()
                                .denominacao("Salão Subterrâneo")
                                .nivelEstimadoDificuldade(NivelDificuldadeSetor.BAIXO)
                                .profundidadeMaxima(new BigDecimal("10.50"))
                                .extensaoAproximada(new BigDecimal("190.00"))
                                .descricao("Área ampla utilizada para observação e estudos ambientais")
                                .riscoInundacao(new BigDecimal("8.00"))
                                .condicaoCorrente(CondicaoSetor.DISPONIVEL)
                                .caverna(cavernas.get(4))
                                .build());

                setores.add(Setor.builder()
                                .denominacao("Galeria Profunda")
                                .nivelEstimadoDificuldade(NivelDificuldadeSetor.ALTO)
                                .profundidadeMaxima(new BigDecimal("42.00"))
                                .extensaoAproximada(new BigDecimal("480.00"))
                                .descricao("Trecho profundo com passagens estreitas e terreno acidentado")
                                .riscoInundacao(new BigDecimal("40.00"))
                                .condicaoCorrente(CondicaoSetor.DISPONIVEL)
                                .caverna(cavernas.get(5))
                                .build());

                setores.add(Setor.builder()
                                .denominacao("Galeria das Estalactites")
                                .nivelEstimadoDificuldade(NivelDificuldadeSetor.MODERADO)
                                .profundidadeMaxima(new BigDecimal("22.00"))
                                .extensaoAproximada(new BigDecimal("360.00"))
                                .descricao("Galeria com grande concentração de formações minerais")
                                .riscoInundacao(new BigDecimal("15.00"))
                                .condicaoCorrente(CondicaoSetor.DISPONIVEL)
                                .caverna(cavernas.get(6))
                                .build());

                setores.add(Setor.builder()
                                .denominacao("Galeria da Onça")
                                .nivelEstimadoDificuldade(NivelDificuldadeSetor.ALTO)
                                .profundidadeMaxima(new BigDecimal("35.00"))
                                .extensaoAproximada(new BigDecimal("410.00"))
                                .descricao("Trecho de difícil acesso com piso irregular e baixa iluminação natural")
                                .riscoInundacao(new BigDecimal("30.00"))
                                .condicaoCorrente(CondicaoSetor.DISPONIVEL)
                                .caverna(cavernas.get(7))
                                .build());

                setores.add(Setor.builder()
                                .denominacao("Salão do Janelão")
                                .nivelEstimadoDificuldade(NivelDificuldadeSetor.BAIXO)
                                .profundidadeMaxima(new BigDecimal("15.00"))
                                .extensaoAproximada(new BigDecimal("520.00"))
                                .descricao("Grande salão com abertura natural e boa circulação de ar")
                                .riscoInundacao(new BigDecimal("7.00"))
                                .condicaoCorrente(CondicaoSetor.DISPONIVEL)
                                .caverna(cavernas.get(8))
                                .build());

                setores.add(Setor.builder()
                                .denominacao("Galeria da Mangabeira")
                                .nivelEstimadoDificuldade(NivelDificuldadeSetor.MODERADO)
                                .profundidadeMaxima(new BigDecimal("25.00"))
                                .extensaoAproximada(new BigDecimal("390.00"))
                                .descricao("Galeria com trechos inclinados e formações minerais preservadas")
                                .riscoInundacao(new BigDecimal("18.00"))
                                .condicaoCorrente(CondicaoSetor.DISPONIVEL)
                                .caverna(cavernas.get(9))
                                .build());

                for (Setor s : setores) {
                        setorService.cadastrar(s);
                }

                System.out.println("[seed] 10 registros de Setor criados.");
        }

        private void seedPlanosSeguranca() {
                if (!planoSegurancaService.listarTodos().isEmpty()) {
                        System.out.println("[seed] Plano de Segurança já possui registros — pulando.");
                        return;
                }

                planosSeguranca.add(PlanoSeguranca.builder()
                                .procedimentosEvacuacao(List.of(
                                                "Evacuar pela entrada principal da caverna.",
                                                "Manter o grupo reunido durante a evacuação.",
                                                "Comunicar a equipe de apoio."))
                                .pontoExternoEncontro(new Localizacao(
                                                new BigDecimal("-7.1195"),
                                                new BigDecimal("-34.8450"),
                                                "SIRGAS2000"))
                                .tempoMaxSemComunicacao(30)
                                .telefoneEmergencia("193")
                                .necessidadeEquipeMedica(false)
                                .build());

                planosSeguranca.add(PlanoSeguranca.builder()
                                .procedimentosEvacuacao(List.of(
                                                "Retornar pela rota previamente mapeada.",
                                                "Realizar conferência dos participantes.",
                                                "Acionar equipe de emergência se necessário."))
                                .pontoExternoEncontro(new Localizacao(
                                                new BigDecimal("-7.2300"),
                                                new BigDecimal("-35.8800"),
                                                "SIRGAS2000"))
                                .tempoMaxSemComunicacao(20)
                                .telefoneEmergencia("193")
                                .necessidadeEquipeMedica(true)
                                .build());

                planosSeguranca.add(PlanoSeguranca.builder()
                                .procedimentosEvacuacao(List.of(
                                                "Interromper imediatamente a atividade.",
                                                "Retornar pelo acesso sinalizado.",
                                                "Informar qualquer ocorrência ao responsável."))
                                .pontoExternoEncontro(new Localizacao(
                                                new BigDecimal("-12.3830"),
                                                new BigDecimal("-41.5660"),
                                                "SIRGAS2000"))
                                .tempoMaxSemComunicacao(30)
                                .telefoneEmergencia("193")
                                .necessidadeEquipeMedica(false)
                                .build());

                planosSeguranca.add(PlanoSeguranca.builder()
                                .procedimentosEvacuacao(List.of(
                                                "Abandonar a área de pesquisa.",
                                                "Seguir a rota de saída indicada.",
                                                "Reunir todos os participantes no ponto externo."))
                                .pontoExternoEncontro(new Localizacao(
                                                new BigDecimal("-21.1260"),
                                                new BigDecimal("-56.4820"),
                                                "SIRGAS2000"))
                                .tempoMaxSemComunicacao(15)
                                .telefoneEmergencia("193")
                                .necessidadeEquipeMedica(true)
                                .build());

                planosSeguranca.add(PlanoSeguranca.builder()
                                .procedimentosEvacuacao(List.of(
                                                "Suspender a atividade.",
                                                "Retornar pela rota de acesso.",
                                                "Realizar conferência da equipe."))
                                .pontoExternoEncontro(new Localizacao(
                                                new BigDecimal("-21.1370"),
                                                new BigDecimal("-56.5790"),
                                                "SIRGAS2000"))
                                .tempoMaxSemComunicacao(20)
                                .telefoneEmergencia("193")
                                .necessidadeEquipeMedica(false)
                                .build());

                planosSeguranca.add(PlanoSeguranca.builder()
                                .procedimentosEvacuacao(List.of(
                                                "Retornar imediatamente pela galeria principal.",
                                                "Manter comunicação entre os integrantes.",
                                                "Acionar equipe externa em caso de emergência."))
                                .pontoExternoEncontro(new Localizacao(
                                                new BigDecimal("-12.3160"),
                                                new BigDecimal("-41.6160"),
                                                "SIRGAS2000"))
                                .tempoMaxSemComunicacao(15)
                                .telefoneEmergencia("193")
                                .necessidadeEquipeMedica(true)
                                .build());

                planosSeguranca.add(PlanoSeguranca.builder()
                                .procedimentosEvacuacao(List.of(
                                                "Interromper o levantamento.",
                                                "Seguir a sinalização de saída.",
                                                "Reunir a equipe no ponto externo."))
                                .pontoExternoEncontro(new Localizacao(
                                                new BigDecimal("-12.3500"),
                                                new BigDecimal("-41.5660"),
                                                "SIRGAS2000"))
                                .tempoMaxSemComunicacao(25)
                                .telefoneEmergencia("193")
                                .necessidadeEquipeMedica(false)
                                .build());

                planosSeguranca.add(PlanoSeguranca.builder()
                                .procedimentosEvacuacao(List.of(
                                                "Suspender imediatamente a atividade.",
                                                "Retornar pela rota segura.",
                                                "Solicitar apoio da equipe de emergência."))
                                .pontoExternoEncontro(new Localizacao(
                                                new BigDecimal("-20.3700"),
                                                new BigDecimal("-45.6700"),
                                                "SIRGAS2000"))
                                .tempoMaxSemComunicacao(15)
                                .telefoneEmergencia("193")
                                .necessidadeEquipeMedica(true)
                                .build());

                planosSeguranca.add(PlanoSeguranca.builder()
                                .procedimentosEvacuacao(List.of(
                                                "Retornar pela rota principal.",
                                                "Verificar a presença de todos os participantes.",
                                                "Comunicar o encerramento da atividade."))
                                .pontoExternoEncontro(new Localizacao(
                                                new BigDecimal("-15.4700"),
                                                new BigDecimal("-44.3600"),
                                                "SIRGAS2000"))
                                .tempoMaxSemComunicacao(30)
                                .telefoneEmergencia("193")
                                .necessidadeEquipeMedica(false)
                                .build());

                planosSeguranca.add(PlanoSeguranca.builder()
                                .procedimentosEvacuacao(List.of(
                                                "Interromper a atividade.",
                                                "Seguir a rota de saída indicada.",
                                                "Comunicar qualquer ocorrência à coordenação."))
                                .pontoExternoEncontro(new Localizacao(
                                                new BigDecimal("-13.8100"),
                                                new BigDecimal("-41.3000"),
                                                "SIRGAS2000"))
                                .tempoMaxSemComunicacao(25)
                                .telefoneEmergencia("193")
                                .necessidadeEquipeMedica(false)
                                .build());

                for (PlanoSeguranca plano : planosSeguranca) {
                        planoSegurancaService.cadastrar(plano);
                }

                System.out.println("[seed] 10 registros de Plano de Segurança criados.");
        }

        private void seedExpedicoes() {
                if (!expedicaoService.listarTodos().isEmpty()) {
                        System.out.println("[seed] Expedição já possui registros — pulando.");
                        return;
                }

                expedicoes.add(Expedicao.builder()
                                .codigo("EXP-2026-001")
                                .custoRealizado(new BigDecimal("15000.00"))
                                .titulo("Mapeamento inicial da Furna de Lampião")
                                .objetivo("Levantamento topográfico e coleta de amostras minerais")
                                .inicioPrevisto(LocalDateTime.of(2026, 10, 1, 8, 0))
                                .terminoPrevisto(LocalDateTime.of(2026, 10, 3, 18, 0))
                                .orcamentoAprovado(new BigDecimal("15000.00"))
                                .qtdMaxParticipantes(6)
                                .situacao(SituacaoExpedicao.PLANEJADA)
                                .cancelamentoEmergencial(false)
                                .caverna(cavernas.get(0))
                                .planoSeguranca(planosSeguranca.get(0))
                                .build());

                expedicoes.add(Expedicao.builder()
                                .codigo("EXP-2026-002")
                                .custoRealizado(new BigDecimal("10000.00"))
                                .titulo("Exploração da Caverna Santana")
                                .objetivo("Exploração espeleológica e levantamento de novas galerias")
                                .inicioPrevisto(LocalDateTime.of(2026, 11, 10, 7, 30))
                                .terminoPrevisto(LocalDateTime.of(2026, 11, 12, 17, 0))
                                .orcamentoAprovado(new BigDecimal("12000.00"))
                                .qtdMaxParticipantes(8)
                                .situacao(SituacaoExpedicao.PLANEJADA)
                                .cancelamentoEmergencial(false)
                                .caverna(cavernas.get(1))
                                .planoSeguranca(planosSeguranca.get(1))
                                .build());

                expedicoes.add(Expedicao.builder()
                                .codigo("EXP-2026-003")
                                .custoRealizado(new BigDecimal("7000.00"))
                                .titulo("Inspeção da Gruta da Pratinha")
                                .objetivo("Avaliação das condições de acesso e levantamento ambiental")
                                .inicioPrevisto(LocalDateTime.of(2026, 12, 5, 9, 0))
                                .terminoPrevisto(LocalDateTime.of(2026, 12, 5, 16, 0))
                                .orcamentoAprovado(new BigDecimal("7500.00"))
                                .qtdMaxParticipantes(5)
                                .situacao(SituacaoExpedicao.PLANEJADA)
                                .cancelamentoEmergencial(false)
                                .caverna(cavernas.get(2))
                                .planoSeguranca(planosSeguranca.get(2))
                                .build());

                expedicoes.add(Expedicao.builder()
                                .codigo("EXP-2026-004")
                                .custoRealizado(new BigDecimal("8000.00"))
                                .titulo("Estudo da Gruta Azul")
                                .objetivo("Análise geológica e registro fotográfico das formações")
                                .inicioPrevisto(LocalDateTime.of(2026, 10, 15, 8, 0))
                                .terminoPrevisto(LocalDateTime.of(2026, 10, 16, 17, 0))
                                .orcamentoAprovado(new BigDecimal("9000.00"))
                                .qtdMaxParticipantes(7)
                                .situacao(SituacaoExpedicao.PLANEJADA)
                                .cancelamentoEmergencial(false)
                                .caverna(cavernas.get(3))
                                .planoSeguranca(planosSeguranca.get(3))
                                .build());

                expedicoes.add(Expedicao.builder()
                                .codigo("EXP-2026-005")
                                .custoRealizado(new BigDecimal("5000.00"))
                                .titulo("Monitoramento do Lago Azul")
                                .objetivo("Monitoramento ambiental e análise da qualidade da água")
                                .inicioPrevisto(LocalDateTime.of(2026, 10, 22, 9, 0))
                                .terminoPrevisto(LocalDateTime.of(2026, 10, 23, 16, 0))
                                .orcamentoAprovado(new BigDecimal("6800.00"))
                                .qtdMaxParticipantes(5)
                                .situacao(SituacaoExpedicao.PLANEJADA)
                                .cancelamentoEmergencial(false)
                                .caverna(cavernas.get(4))
                                .planoSeguranca(planosSeguranca.get(4))
                                .build());

                expedicoes.add(Expedicao.builder()
                                .codigo("EXP-2026-006")
                                .custoRealizado(new BigDecimal("18000.00"))
                                .titulo("Levantamento topográfico da Torrinha")
                                .objetivo("Mapeamento das galerias e identificação de novos acessos")
                                .inicioPrevisto(LocalDateTime.of(2026, 11, 3, 7, 0))
                                .terminoPrevisto(LocalDateTime.of(2026, 11, 6, 18, 0))
                                .orcamentoAprovado(new BigDecimal("18000.00"))
                                .qtdMaxParticipantes(10)
                                .situacao(SituacaoExpedicao.PLANEJADA)
                                .cancelamentoEmergencial(false)
                                .caverna(cavernas.get(5))
                                .planoSeguranca(planosSeguranca.get(5))
                                .build());

                expedicoes.add(Expedicao.builder()
                                .codigo("EXP-2026-007")
                                .custoRealizado(new BigDecimal("13000.00"))
                                .titulo("Exploração da Lapa Doce")
                                .objetivo("Estudo das formações minerais e levantamento espeleológico")
                                .inicioPrevisto(LocalDateTime.of(2026, 11, 18, 8, 0))
                                .terminoPrevisto(LocalDateTime.of(2026, 11, 20, 17, 0))
                                .orcamentoAprovado(new BigDecimal("13500.00"))
                                .qtdMaxParticipantes(8)
                                .situacao(SituacaoExpedicao.PLANEJADA)
                                .cancelamentoEmergencial(false)
                                .caverna(cavernas.get(6))
                                .planoSeguranca(planosSeguranca.get(6))
                                .build());

                expedicoes.add(Expedicao.builder()
                                .codigo("EXP-2026-008")
                                .custoRealizado(new BigDecimal("7500.00"))
                                .titulo("Inspeção da Caverna da Onça")
                                .objetivo("Avaliação estrutural e análise das condições de segurança")
                                .inicioPrevisto(LocalDateTime.of(2026, 12, 1, 8, 30))
                                .terminoPrevisto(LocalDateTime.of(2026, 12, 2, 17, 0))
                                .orcamentoAprovado(new BigDecimal("8200.00"))
                                .qtdMaxParticipantes(6)
                                .situacao(SituacaoExpedicao.PLANEJADA)
                                .cancelamentoEmergencial(false)
                                .caverna(cavernas.get(7))
                                .planoSeguranca(planosSeguranca.get(7))
                                .build());

                expedicoes.add(Expedicao.builder()
                                .codigo("EXP-2026-009")
                                .custoRealizado(new BigDecimal("15000.00"))
                                .titulo("Mapeamento da Gruta do Janelão")
                                .objetivo("Levantamento cartográfico e registro das galerias")
                                .inicioPrevisto(LocalDateTime.of(2026, 12, 8, 7, 30))
                                .terminoPrevisto(LocalDateTime.of(2026, 12, 10, 18, 0))
                                .orcamentoAprovado(new BigDecimal("16000.00"))
                                .qtdMaxParticipantes(9)
                                .situacao(SituacaoExpedicao.PLANEJADA)
                                .cancelamentoEmergencial(false)
                                .caverna(cavernas.get(8))
                                .planoSeguranca(planosSeguranca.get(8))
                                .build());

                expedicoes.add(Expedicao.builder()
                                .codigo("EXP-2026-010")
                                .custoRealizado(new BigDecimal("10000.00"))
                                .titulo("Avaliação da Gruta da Mangabeira")
                                .objetivo("Inventário ambiental e avaliação das condições de conservação")
                                .inicioPrevisto(LocalDateTime.of(2026, 12, 15, 8, 0))
                                .terminoPrevisto(LocalDateTime.of(2026, 12, 17, 17, 0))
                                .orcamentoAprovado(new BigDecimal("11000.00"))
                                .qtdMaxParticipantes(7)
                                .situacao(SituacaoExpedicao.PLANEJADA)
                                .cancelamentoEmergencial(false)
                                .caverna(cavernas.get(9))
                                .planoSeguranca(planosSeguranca.get(9))
                                .build());

                for (Expedicao e : expedicoes) {
                        expedicaoService.cadastrar(e);
                }

                System.out.println("[seed] 10 registros de Expedição criados.");
        }

        private void seedExpedicaoSetores() {
                for (int i = 0; i < expedicoes.size() && i < setores.size(); i++) {
                        expedicaoService.associarSetor(
                                        expedicoes.get(i).getId(),
                                        setores.get(i).getId());
                }

                System.out.println("[seed] Associações Expedição-Setor criadas.");
        }

        private void seedAutorizacoesAmbientais() {
                if (!autorizacaoAmbientalService.listarTodos().isEmpty()) {
                        System.out.println("[seed] Autorização Ambiental já possui registros — pulando.");
                        return;
                }

                autorizacoesAmbientais.add(AutorizacaoAmbiental.builder()
                                .num(1001)
                                .orgaoEmissor("IBAMA")
                                .dataEmissao(LocalDate.of(2026, 9, 1))
                                .validade(LocalDate.of(2027, 9, 1))
                                .situacao(SituacaoAutorizacao.VIGENTE)
                                .observacoes("Autorização ambiental vigente.")
                                .expedicao(expedicoes.get(0))
                                .build());

                autorizacoesAmbientais.add(AutorizacaoAmbiental.builder()
                                .num(1002)
                                .orgaoEmissor("ICMBio")
                                .dataEmissao(LocalDate.of(2026, 8, 15))
                                .validade(LocalDate.of(2027, 8, 15))
                                .situacao(SituacaoAutorizacao.VIGENTE)
                                .observacoes("Autorização para pesquisa espeleológica.")
                                .expedicao(expedicoes.get(1))
                                .build());

                autorizacoesAmbientais.add(AutorizacaoAmbiental.builder()
                                .num(1003)
                                .orgaoEmissor("IBAMA")
                                .dataEmissao(LocalDate.of(2026, 7, 10))
                                .validade(LocalDate.of(2026, 12, 31))
                                .situacao(SituacaoAutorizacao.EXPIRADA)
                                .observacoes("Autorização encerrada por término da validade.")
                                .expedicao(expedicoes.get(2))
                                .build());

                autorizacoesAmbientais.add(AutorizacaoAmbiental.builder()
                                .num(1004)
                                .orgaoEmissor("ICMBio")
                                .dataEmissao(LocalDate.of(2026, 6, 20))
                                .validade(LocalDate.of(2026, 12, 20))
                                .situacao(SituacaoAutorizacao.NEGADA)
                                .observacoes("Solicitação não aprovada pelo órgão ambiental.")
                                .expedicao(expedicoes.get(3))
                                .build());

                autorizacoesAmbientais.add(AutorizacaoAmbiental.builder()
                                .num(1005)
                                .orgaoEmissor("IBAMA")
                                .dataEmissao(LocalDate.of(2026, 9, 10))
                                .validade(LocalDate.of(2027, 9, 10))
                                .situacao(SituacaoAutorizacao.PENDENTE)
                                .observacoes("Aguardando análise do órgão ambiental.")
                                .expedicao(expedicoes.get(4))
                                .build());

                autorizacoesAmbientais.add(AutorizacaoAmbiental.builder()
                                .num(1006)
                                .orgaoEmissor("ICMBio")
                                .dataEmissao(LocalDate.of(2026, 9, 12))
                                .validade(LocalDate.of(2027, 9, 12))
                                .situacao(SituacaoAutorizacao.VIGENTE)
                                .observacoes("Autorização ambiental vigente.")
                                .expedicao(expedicoes.get(5))
                                .build());

                for (AutorizacaoAmbiental autorizacao : autorizacoesAmbientais) {
                        autorizacaoAmbientalService.cadastrar(autorizacao);
                }

                System.out.println("[seed] 6 registros de Autorização Ambiental criados.");
        }

        private void seedParticipacoes() {
                participacoes.add(
                                Participacao.builder()
                                                .pessoa(pessoas.get(0))
                                                .expedicao(expedicoes.get(0))
                                                .papelParticipante(PapelParticipante.PESQUISADOR)
                                                .dataConfirmacao(LocalDate.of(2026, 10, 1))
                                                .valorDiaria(new BigDecimal("350.00"))
                                                .qtdPrevistaDias(3)
                                                .presencaConfirmada(true)
                                                .observacoes("Participação confirmada para levantamento ambiental.")
                                                .build());

                participacoes.add(
                                Participacao.builder()
                                                .pessoa(pessoas.get(1))
                                                .expedicao(expedicoes.get(0))
                                                .papelParticipante(PapelParticipante.GUIA_ESPELEOLOGICO)
                                                .dataConfirmacao(LocalDate.of(2026, 10, 2))
                                                .valorDiaria(new BigDecimal("280.00"))
                                                .qtdPrevistaDias(3)
                                                .presencaConfirmada(true)
                                                .observacoes("Guia responsável pelo acesso à caverna.")
                                                .build());

                participacoes.add(
                                Participacao.builder()
                                                .pessoa(pessoas.get(2))
                                                .expedicao(expedicoes.get(1))
                                                .papelParticipante(PapelParticipante.PESQUISADOR)
                                                .dataConfirmacao(LocalDate.of(2026, 10, 5))
                                                .valorDiaria(new BigDecimal("400.00"))
                                                .qtdPrevistaDias(4)
                                                .presencaConfirmada(true)
                                                .observacoes("Responsável pela análise geológica.")
                                                .build());

                participacoes.add(
                                Participacao.builder()
                                                .pessoa(pessoas.get(3))
                                                .expedicao(expedicoes.get(1))
                                                .papelParticipante(PapelParticipante.GUIA_ESPELEOLOGICO)
                                                .dataConfirmacao(LocalDate.of(2026, 10, 6))
                                                .valorDiaria(new BigDecimal("300.00"))
                                                .qtdPrevistaDias(4)
                                                .presencaConfirmada(false)
                                                .observacoes("Aguardando confirmação da presença.")
                                                .build());

                participacoes.add(
                                Participacao.builder()
                                                .pessoa(pessoas.get(4))
                                                .expedicao(expedicoes.get(2))
                                                .papelParticipante(PapelParticipante.PESQUISADOR)
                                                .dataConfirmacao(LocalDate.of(2026, 10, 10))
                                                .valorDiaria(new BigDecimal("375.00"))
                                                .qtdPrevistaDias(5)
                                                .presencaConfirmada(true)
                                                .observacoes("Pesquisador responsável pelo inventário da fauna.")
                                                .build());

                participacoes.add(
                                Participacao.builder()
                                                .pessoa(pessoas.get(5))
                                                .expedicao(expedicoes.get(3))
                                                .papelParticipante(PapelParticipante.AUXILIAR)
                                                .dataConfirmacao(LocalDate.of(2026, 10, 12))
                                                .valorDiaria(new BigDecimal("275.00"))
                                                .qtdPrevistaDias(2)
                                                .presencaConfirmada(true)
                                                .observacoes("Experiência prévia na região da expedição.")
                                                .build());

                participacoes.add(
                                Participacao.builder()
                                                .pessoa(pessoas.get(6))
                                                .expedicao(expedicoes.get(4))
                                                .papelParticipante(PapelParticipante.COORDENADOR)
                                                .dataConfirmacao(LocalDate.of(2026, 10, 15))
                                                .valorDiaria(new BigDecimal("420.00"))
                                                .qtdPrevistaDias(6)
                                                .presencaConfirmada(false)
                                                .observacoes("Participação condicionada à confirmação do cronograma.")
                                                .build());

                participacoes.add(
                                Participacao.builder()
                                                .pessoa(pessoas.get(7))
                                                .expedicao(expedicoes.get(5))
                                                .papelParticipante(PapelParticipante.FOTOGRAFO)
                                                .dataConfirmacao(LocalDate.of(2026, 10, 18))
                                                .valorDiaria(new BigDecimal("290.00"))
                                                .qtdPrevistaDias(3)
                                                .presencaConfirmada(true)
                                                .observacoes("Responsável pela orientação durante a exploração.")
                                                .build());

                participacoes.add(
                                Participacao.builder()
                                                .pessoa(pessoas.get(8))
                                                .expedicao(expedicoes.get(6))
                                                .papelParticipante(PapelParticipante.TECNICO)
                                                .dataConfirmacao(LocalDate.of(2026, 10, 20))
                                                .valorDiaria(new BigDecimal("390.00"))
                                                .qtdPrevistaDias(4)
                                                .presencaConfirmada(true)
                                                .observacoes("Responsável pela coleta de amostras.")
                                                .build());

                participacoes.add(
                                Participacao.builder()
                                                .pessoa(pessoas.get(9))
                                                .expedicao(expedicoes.get(7))
                                                .papelParticipante(PapelParticipante.TECNICO)
                                                .dataConfirmacao(LocalDate.of(2026, 10, 22))
                                                .valorDiaria(new BigDecimal("310.00"))
                                                .qtdPrevistaDias(5)
                                                .presencaConfirmada(false)
                                                .observacoes("Participante aguardando confirmação logística.")
                                                .build());

                for (Participacao p : participacoes) {
                        participacaoService.cadastrar(p);
                }

                System.out.println("[seed] 10 registros de Participação criados.");
        }

        private void seedColetas() {
                coletas.add(
                                ColetaCientifica.builder()
                                                .dataHoraColeta(LocalDateTime.of(2026, 8, 10, 9, 30))
                                                .metodoEmpregado(MetodoEmpregado.COLETA_MANUAL)
                                                .descricaoPonto("Entrada principal da caverna, próximo à área de vegetação.")
                                                .temperatura(new BigDecimal("22.50"))
                                                .umidadeRelativa(new BigDecimal("78.50"))
                                                .profundidade(new BigDecimal("5.20"))
                                                .observacoes("Presença de sedimentos e pequenas espécies de invertebrados.")
                                                .situacaoValidacao(SituacaoValidacaoColeta.VALIDADA)
                                                .pesquisador(pesquisadores.get(0))
                                                .expedicao(expedicoes.get(0))
                                                .setor(setores.get(0))
                                                .build());

                coletas.add(
                                ColetaCientifica.builder()
                                                .dataHoraColeta(LocalDateTime.of(2026, 8, 10, 11, 15))
                                                .metodoEmpregado(MetodoEmpregado.ARMADILHA)
                                                .descricaoPonto("Galeria lateral com baixa luminosidade e solo úmido.")
                                                .temperatura(new BigDecimal("19.80"))
                                                .umidadeRelativa(new BigDecimal("86.20"))
                                                .profundidade(new BigDecimal("18.75"))
                                                .observacoes("Armadilha instalada para monitoramento da fauna cavernícola.")
                                                .situacaoValidacao(SituacaoValidacaoColeta.PENDENTE)
                                                .pesquisador(pesquisadores.get(1))
                                                .expedicao(expedicoes.get(0))
                                                .setor(setores.get(1))
                                                .build());

                coletas.add(
                                ColetaCientifica.builder()
                                                .dataHoraColeta(LocalDateTime.of(2026, 8, 15, 8, 45))
                                                .metodoEmpregado(MetodoEmpregado.AMOSTRAGEM_DE_SOLO)
                                                .descricaoPonto("Salão principal com presença de formações calcárias.")
                                                .temperatura(new BigDecimal("18.30"))
                                                .umidadeRelativa(new BigDecimal("91.40"))
                                                .profundidade(new BigDecimal("32.10"))
                                                .observacoes("Foram observadas formações de estalactites e estalagmites.")
                                                .situacaoValidacao(SituacaoValidacaoColeta.VALIDADA)
                                                .pesquisador(pesquisadores.get(2))
                                                .expedicao(expedicoes.get(1))
                                                .setor(setores.get(2))
                                                .build());

                coletas.add(
                                ColetaCientifica.builder()
                                                .dataHoraColeta(LocalDateTime.of(2026, 8, 15, 14, 20))
                                                .metodoEmpregado(MetodoEmpregado.AMOSTRAGEM_DE_AGUA)
                                                .descricaoPonto("Pequeno curso de água localizado no interior da caverna.")
                                                .temperatura(new BigDecimal("17.90"))
                                                .umidadeRelativa(new BigDecimal("94.75"))
                                                .profundidade(new BigDecimal("41.60"))
                                                .observacoes("Água com aspecto transparente e fluxo moderado.")
                                                .situacaoValidacao(SituacaoValidacaoColeta.PENDENTE)
                                                .pesquisador(pesquisadores.get(3))
                                                .expedicao(expedicoes.get(1))
                                                .setor(setores.get(3))
                                                .build());

                coletas.add(
                                ColetaCientifica.builder()
                                                .dataHoraColeta(LocalDateTime.of(2026, 8, 22, 10, 0))
                                                .metodoEmpregado(MetodoEmpregado.ARMADILHA_FOTOGRAFICA)
                                                .descricaoPonto("Área de deposição de sedimentos próxima ao curso de água.")
                                                .temperatura(new BigDecimal("18.75"))
                                                .umidadeRelativa(new BigDecimal("89.30"))
                                                .profundidade(new BigDecimal("27.40"))
                                                .observacoes("Amostra coletada para análise granulométrica.")
                                                .situacaoValidacao(SituacaoValidacaoColeta.VALIDADA)
                                                .pesquisador(pesquisadores.get(4))
                                                .expedicao(expedicoes.get(2))
                                                .setor(setores.get(4))
                                                .build());

                coletas.add(
                                ColetaCientifica.builder()
                                                .dataHoraColeta(LocalDateTime.of(2026, 8, 22, 15, 35))
                                                .metodoEmpregado(MetodoEmpregado.ARMADILHA_FOTOGRAFICA)
                                                .descricaoPonto("Galeria estreita com evidências de atividade de morcegos.")
                                                .temperatura(new BigDecimal("16.40"))
                                                .umidadeRelativa(new BigDecimal("96.10"))
                                                .profundidade(new BigDecimal("53.80"))
                                                .observacoes("Registros fotográficos realizados sem interferência no ambiente.")
                                                .situacaoValidacao(SituacaoValidacaoColeta.VALIDADA)
                                                .pesquisador(pesquisadores.get(5))
                                                .expedicao(expedicoes.get(2))
                                                .setor(setores.get(5))
                                                .build());

                coletas.add(
                                ColetaCientifica.builder()
                                                .dataHoraColeta(LocalDateTime.of(2026, 8, 29, 9, 10))
                                                .metodoEmpregado(MetodoEmpregado.REDE_DE_NEVOA)
                                                .descricaoPonto("Área próxima ao teto da galeria, utilizada para captura de fauna.")
                                                .temperatura(new BigDecimal("20.10"))
                                                .umidadeRelativa(new BigDecimal("83.60"))
                                                .profundidade(new BigDecimal("14.25"))
                                                .observacoes("Captura realizada temporariamente para identificação da espécie.")
                                                .situacaoValidacao(SituacaoValidacaoColeta.REJEITADA)
                                                .pesquisador(pesquisadores.get(6))
                                                .expedicao(expedicoes.get(3))
                                                .setor(setores.get(6))
                                                .build());

                coletas.add(
                                ColetaCientifica.builder()
                                                .dataHoraColeta(LocalDateTime.of(2026, 8, 29, 13, 50))
                                                .metodoEmpregado(MetodoEmpregado.COLETA_MANUAL)
                                                .descricaoPonto("Trecho profundo da galeria com solo argiloso.")
                                                .temperatura(new BigDecimal("15.85"))
                                                .umidadeRelativa(new BigDecimal("97.20"))
                                                .profundidade(new BigDecimal("67.50"))
                                                .observacoes("Amostra de material orgânico coletada para análise laboratorial.")
                                                .situacaoValidacao(SituacaoValidacaoColeta.PENDENTE)
                                                .pesquisador(pesquisadores.get(7))
                                                .expedicao(expedicoes.get(3))
                                                .setor(setores.get(7))
                                                .build());

                coletas.add(
                                ColetaCientifica.builder()
                                                .dataHoraColeta(LocalDateTime.of(2026, 9, 5, 8, 25))
                                                .metodoEmpregado(MetodoEmpregado.AMOSTRAGEM_DE_SOLO)
                                                .descricaoPonto("Área de solo compacto localizada em uma galeria secundária.")
                                                .temperatura(new BigDecimal("17.25"))
                                                .umidadeRelativa(new BigDecimal("92.80"))
                                                .profundidade(new BigDecimal("38.90"))
                                                .observacoes("Coleta realizada em três pontos distintos para comparação.")
                                                .situacaoValidacao(SituacaoValidacaoColeta.VALIDADA)
                                                .pesquisador(pesquisadores.get(8))
                                                .expedicao(expedicoes.get(4))
                                                .setor(setores.get(8))
                                                .build());

                coletas.add(
                                ColetaCientifica.builder()
                                                .dataHoraColeta(LocalDateTime.of(2026, 9, 5, 16, 40))
                                                .metodoEmpregado(MetodoEmpregado.OBSERVACAO_DIRETA)
                                                .descricaoPonto("Câmara subterrânea com elevada umidade e ausência de iluminação natural.")
                                                .temperatura(new BigDecimal("14.60"))
                                                .umidadeRelativa(new BigDecimal("98.45"))
                                                .profundidade(new BigDecimal("82.30"))
                                                .observacoes("Ambiente apresenta condições favoráveis à ocorrência de organismos troglóbios.")
                                                .situacaoValidacao(SituacaoValidacaoColeta.PENDENTE)
                                                .pesquisador(pesquisadores.get(9))
                                                .expedicao(expedicoes.get(4))
                                                .setor(setores.get(9))
                                                .build());

                for (ColetaCientifica c : coletas) {
                        coletaCientificaService.cadastrar(c);
                }

                System.out.println("[seed] 10 registros de Coletas criados.");
        }

        private void seedEquipamentos() {
                if (!equipamentoService.listarTodos().isEmpty()) {
                        System.out.println("[seed] Equipamento já possui registros — pulando.");
                        return;
                }

                equipamentos.add(Equipamento.builder()
                                .codPatrimonial("EQP-001")
                                .nome("Capacete com luz frontal")
                                .tipo(TipoEquipamento.CAPACETE)
                                .fabricante("Petzl")
                                .valorAquisicao(new BigDecimal("450.00"))
                                .dataCompra(LocalDate.of(2024, 3, 10))
                                .dataUltimaManutencao(LocalDate.of(2026, 6, 1))
                                .situacaoOperacional(SituacaoOperacional.DISPONIVEL)
                                .indicacaoCalibracao(false)
                                .build());

                equipamentos.add(Equipamento.builder()
                                .codPatrimonial("EQP-002")
                                .nome("Corda estática 60m")
                                .tipo(TipoEquipamento.CORDA)
                                .fabricante("Singing Rock")
                                .valorAquisicao(new BigDecimal("890.00"))
                                .dataCompra(LocalDate.of(2023, 11, 5))
                                .dataUltimaManutencao(LocalDate.of(2026, 5, 20))
                                .situacaoOperacional(SituacaoOperacional.EM_USO)
                                .indicacaoCalibracao(false)
                                .build());

                equipamentos.add(Equipamento.builder()
                                .codPatrimonial("EQP-003")
                                .nome("Detector de gases portátil")
                                .tipo(TipoEquipamento.DETECTOR_DE_GASES)
                                .fabricante("Dräger")
                                .valorAquisicao(new BigDecimal("3200.00"))
                                .dataCompra(LocalDate.of(2025, 1, 15))
                                .dataUltimaManutencao(LocalDate.of(2026, 8, 1))
                                .situacaoOperacional(SituacaoOperacional.DISPONIVEL)
                                .indicacaoCalibracao(true)
                                .build());

                equipamentos.add(Equipamento.builder()
                                .codPatrimonial("EQP-004")
                                .nome("Câmera de ação subaquática")
                                .tipo(TipoEquipamento.CAMERA)
                                .fabricante("GoPro")
                                .valorAquisicao(new BigDecimal("2100.00"))
                                .dataCompra(LocalDate.of(2024, 7, 22))
                                .dataUltimaManutencao(null)
                                .situacaoOperacional(SituacaoOperacional.EM_MANUTENCAO)
                                .indicacaoCalibracao(false)
                                .build());

                equipamentos.add(Equipamento.builder()
                                .codPatrimonial("EQP-005")
                                .nome("GPS de navegação")
                                .tipo(TipoEquipamento.GPS)
                                .fabricante("Garmin")
                                .valorAquisicao(new BigDecimal("1350.00"))
                                .dataCompra(LocalDate.of(2022, 9, 30))
                                .dataUltimaManutencao(LocalDate.of(2026, 4, 10))
                                .situacaoOperacional(SituacaoOperacional.DISPONIVEL)
                                .indicacaoCalibracao(true)
                                .build());

                equipamentos.add(Equipamento.builder()
                                .codPatrimonial("EQP-006")
                                .nome("Lanterna de mão reforçada")
                                .tipo(TipoEquipamento.LANTERNA)
                                .fabricante("Fenix")
                                .valorAquisicao(new BigDecimal("280.00"))
                                .dataCompra(LocalDate.of(2025, 4, 18))
                                .dataUltimaManutencao(null)
                                .situacaoOperacional(SituacaoOperacional.DANIFICADO)
                                .indicacaoCalibracao(false)
                                .build());

                equipamentos.add(Equipamento.builder()
                                .codPatrimonial("EQP-007")
                                .nome("Lanterna solar")
                                .tipo(TipoEquipamento.LANTERNA)
                                .fabricante("Fenix")
                                .valorAquisicao(new BigDecimal("280.00"))
                                .dataCompra(LocalDate.of(2025, 4, 18))
                                .dataUltimaManutencao(null)
                                .situacaoOperacional(SituacaoOperacional.DANIFICADO)
                                .indicacaoCalibracao(false)
                                .build());

                equipamentos.add(Equipamento.builder()
                                .codPatrimonial("EQP-008")
                                .nome("Câmera de ação noturna")
                                .tipo(TipoEquipamento.CAMERA)
                                .fabricante("GoPro")
                                .valorAquisicao(new BigDecimal("2800.00"))
                                .dataCompra(LocalDate.of(2025, 4, 18))
                                .dataUltimaManutencao(null)
                                .situacaoOperacional(SituacaoOperacional.DISPONIVEL)
                                .indicacaoCalibracao(false)
                                .build());

                equipamentos.add(Equipamento.builder()
                                .codPatrimonial("EQP-009")
                                .nome("GPS de navegação subaquática")
                                .tipo(TipoEquipamento.GPS)
                                .fabricante("Garmin")
                                .valorAquisicao(new BigDecimal("1350.00"))
                                .dataCompra(LocalDate.of(2022, 9, 30))
                                .dataUltimaManutencao(LocalDate.of(2026, 4, 10))
                                .situacaoOperacional(SituacaoOperacional.DISPONIVEL)
                                .indicacaoCalibracao(true)
                                .build());

                equipamentos.add(Equipamento.builder()
                                .codPatrimonial("EQP-010")
                                .nome("Capacete de proteção")
                                .tipo(TipoEquipamento.GPS)
                                .fabricante("Garmin")
                                .valorAquisicao(new BigDecimal("1350.00"))
                                .dataCompra(LocalDate.of(2022, 9, 30))
                                .dataUltimaManutencao(LocalDate.of(2026, 4, 10))
                                .situacaoOperacional(SituacaoOperacional.DISPONIVEL)
                                .indicacaoCalibracao(true)
                                .build());

                for (Equipamento e : equipamentos) {
                        equipamentoService.cadastrar(e);
                }

                System.out.println("[seed] 10 registros de Equipamento criados.");
        }

        private void seedMovimentacoes() {
                if (!movimentacaoService.listarTodos().isEmpty()) {
                        System.out.println("[seed] Movimentação já possui registros — pulando.");
                        return;
                }

                EstadoSaida estadoSaida = EstadoSaida.PENDENTE;
                EstadoRetorno estadoRetorno = EstadoRetorno.PENDENTE;

                movimentacoes.add(Movimentacao.builder()
                                .dataHoraRetirada(LocalDateTime.of(2026, 8, 1, 8, 30))
                                .dataPrevisaoDevolucao(LocalDate.of(2026, 8, 5))
                                .dataDevolucao(LocalDate.of(2026, 8, 5))
                                .estadoSaida(estadoSaida)
                                .estadoRetorno(estadoRetorno)
                                .custoAvaria(new BigDecimal("0.00"))
                                .expedicao(expedicoes.get(0))
                                .equipamento(equipamentos.get(0))
                                .pessoa(pessoas.get(0))
                                .build());

                movimentacoes.add(Movimentacao.builder()
                                .dataHoraRetirada(LocalDateTime.of(2026, 8, 3, 9, 0))
                                .dataPrevisaoDevolucao(LocalDate.of(2026, 8, 8))
                                .dataDevolucao(LocalDate.of(2026, 8, 8))
                                .estadoSaida(estadoSaida)
                                .estadoRetorno(estadoRetorno)
                                .custoAvaria(new BigDecimal("150.00"))
                                .expedicao(expedicoes.get(1))
                                .equipamento(equipamentos.get(1))
                                .pessoa(pessoas.get(1))
                                .build());

                movimentacoes.add(Movimentacao.builder()
                                .dataHoraRetirada(LocalDateTime.of(2026, 8, 10, 7, 45))
                                .dataPrevisaoDevolucao(LocalDate.of(2026, 8, 15))
                                .dataDevolucao(LocalDate.of(2026, 8, 14))
                                .estadoSaida(estadoSaida)
                                .estadoRetorno(estadoRetorno)
                                .custoAvaria(new BigDecimal("0.00"))
                                .expedicao(expedicoes.get(2))
                                .equipamento(equipamentos.get(2))
                                .pessoa(pessoas.get(2))
                                .build());

                movimentacoes.add(Movimentacao.builder()
                                .dataHoraRetirada(LocalDateTime.of(2026, 8, 12, 10, 15))
                                .dataPrevisaoDevolucao(LocalDate.of(2026, 8, 18))
                                .dataDevolucao(LocalDate.of(2026, 8, 18))
                                .estadoSaida(estadoSaida)
                                .estadoRetorno(estadoRetorno)
                                .custoAvaria(new BigDecimal("75.50"))
                                .expedicao(expedicoes.get(3))
                                .equipamento(equipamentos.get(3))
                                .pessoa(pessoas.get(3))
                                .build());

                movimentacoes.add(Movimentacao.builder()
                                .dataHoraRetirada(LocalDateTime.of(2026, 8, 20, 8, 0))
                                .dataPrevisaoDevolucao(LocalDate.of(2026, 8, 25))
                                .dataDevolucao(LocalDate.of(2026, 8, 24))
                                .estadoSaida(estadoSaida)
                                .estadoRetorno(estadoRetorno)
                                .custoAvaria(new BigDecimal("0.00"))
                                .expedicao(expedicoes.get(4))
                                .equipamento(equipamentos.get(4))
                                .pessoa(pessoas.get(4))
                                .build());

                movimentacoes.add(Movimentacao.builder()
                                .dataHoraRetirada(LocalDateTime.of(2026, 9, 1, 9, 30))
                                .dataPrevisaoDevolucao(LocalDate.of(2026, 9, 7))
                                .estadoSaida(estadoSaida)
                                .estadoRetorno(null)
                                .custoAvaria(null)
                                .expedicao(expedicoes.get(0))
                                .equipamento(equipamentos.get(5))
                                .pessoa(pessoas.get(5))
                                .build());

                movimentacoes.add(Movimentacao.builder()
                                .dataHoraRetirada(LocalDateTime.of(2026, 9, 3, 8, 45))
                                .dataPrevisaoDevolucao(LocalDate.of(2026, 9, 10))
                                .estadoSaida(estadoSaida)
                                .estadoRetorno(null)
                                .custoAvaria(null)
                                .expedicao(expedicoes.get(1))
                                .equipamento(equipamentos.get(6))
                                .pessoa(pessoas.get(6))
                                .build());

                movimentacoes.add(Movimentacao.builder()
                                .dataHoraRetirada(LocalDateTime.of(2026, 9, 5, 10, 0))
                                .dataPrevisaoDevolucao(LocalDate.of(2026, 9, 12))
                                .estadoSaida(estadoSaida)
                                .estadoRetorno(null)
                                .custoAvaria(null)
                                .expedicao(expedicoes.get(2))
                                .equipamento(equipamentos.get(7))
                                .pessoa(pessoas.get(7))
                                .build());

                movimentacoes.add(Movimentacao.builder()
                                .dataHoraRetirada(LocalDateTime.of(2026, 9, 8, 7, 30))
                                .dataPrevisaoDevolucao(LocalDate.of(2026, 9, 15))
                                .estadoSaida(estadoSaida)
                                .estadoRetorno(null)
                                .custoAvaria(null)
                                .expedicao(expedicoes.get(3))
                                .equipamento(equipamentos.get(8))
                                .pessoa(pessoas.get(8))
                                .build());

                movimentacoes.add(Movimentacao.builder()
                                .dataHoraRetirada(LocalDateTime.of(2026, 9, 10, 9, 15))
                                .dataPrevisaoDevolucao(LocalDate.of(2026, 9, 17))
                                .estadoSaida(estadoSaida)
                                .estadoRetorno(null)
                                .custoAvaria(null)
                                .expedicao(expedicoes.get(4))
                                .equipamento(equipamentos.get(9))
                                .pessoa(pessoas.get(9))
                                .build());

                for (Movimentacao movimentacao : movimentacoes) {
                        movimentacaoService.cadastrar(movimentacao);
                }

                System.out.println("[seed] 10 registros de Equipamento criados.");
        }

        private void seedAmostras() {

                if (!amostraService.listarTodos().isEmpty()) {
                        System.out.println(
                                        "[seed] Amostra já possui registros — pulando.");
                        return;
                }

                if (coletas.isEmpty()) {
                        throw new IllegalStateException(
                                        "Não existem coletas científicas para associar às amostras.");
                }

                amostras.add(Amostra.builder()
                                .codCampo("AM-001")
                                .categoriaAmostra(CategoriaAmostra.GEOLOGICA)
                                .volume(new BigDecimal("250.00"))
                                .unidadeMedida(UnidadeMedida.MILILITRO)
                                .dataAcondicionamento(LocalDateTime.of(
                                                2026, 8, 1, 9, 0))
                                .condicaoAmostra(CondicaoConservacaoAmostra.EXCELENTE)
                                .indicacaoMaterialPerigoso(false)
                                .observacoes("Amostra de água coletada na entrada da caverna.")
                                .coletaCientifica(coletas.get(0))
                                .build());

                amostras.add(Amostra.builder()
                                .codCampo("AM-002")
                                .categoriaAmostra(CategoriaAmostra.MINERAL)
                                .volume(new BigDecimal("500.00"))
                                .unidadeMedida(UnidadeMedida.MILILITRO)
                                .dataAcondicionamento(LocalDateTime.of(
                                                2026, 8, 1, 9, 30))
                                .condicaoAmostra(CondicaoConservacaoAmostra.REGULAR)
                                .indicacaoMaterialPerigoso(false)
                                .observacoes("Amostra de água coletada em região subterrânea.")
                                .coletaCientifica(coletas.get(0))
                                .build());

                amostras.add(Amostra.builder()
                                .codCampo("AM-003")
                                .categoriaAmostra(CategoriaAmostra.ARQUEOLOGICA)
                                .volume(new BigDecimal("150.00"))
                                .unidadeMedida(UnidadeMedida.UNIDADE)
                                .dataAcondicionamento(LocalDateTime.of(
                                                2026, 8, 2, 10, 0))
                                .condicaoAmostra(CondicaoConservacaoAmostra.DANIFICADA)
                                .indicacaoMaterialPerigoso(false)
                                .observacoes("Amostra de sedimento do interior da caverna.")
                                .coletaCientifica(coletas.get(1))
                                .build());

                amostras.add(Amostra.builder()
                                .codCampo("AM-004")
                                .categoriaAmostra(CategoriaAmostra.GEOLOGICA)
                                .volume(new BigDecimal("75.00"))
                                .unidadeMedida(UnidadeMedida.LITRO)
                                .dataAcondicionamento(LocalDateTime.of(
                                                2026, 8, 2, 10, 30))
                                .condicaoAmostra(CondicaoConservacaoAmostra.EXCELENTE)
                                .indicacaoMaterialPerigoso(false)
                                .observacoes("Amostra de solo para análise laboratorial.")
                                .coletaCientifica(coletas.get(1))
                                .build());

                amostras.add(Amostra.builder()
                                .codCampo("AM-005")
                                .categoriaAmostra(CategoriaAmostra.OUTRA)
                                .volume(new BigDecimal("100.00"))
                                .unidadeMedida(UnidadeMedida.MILILITRO)
                                .dataAcondicionamento(LocalDateTime.of(
                                                2026, 8, 3, 8, 0))
                                .condicaoAmostra(CondicaoConservacaoAmostra.REGULAR)
                                .indicacaoMaterialPerigoso(true)
                                .observacoes("Amostra identificada com possível material perigoso.")
                                .coletaCientifica(coletas.get(2))
                                .build());

                amostras.add(Amostra.builder()
                                .codCampo("AM-006")
                                .categoriaAmostra(CategoriaAmostra.BIOLOGICA)
                                .volume(new BigDecimal("300.00"))
                                .unidadeMedida(UnidadeMedida.MILILITRO)
                                .dataAcondicionamento(LocalDateTime.of(
                                                2026, 8, 3, 8, 30))
                                .condicaoAmostra(CondicaoConservacaoAmostra.BOA)
                                .indicacaoMaterialPerigoso(false)
                                .observacoes("Amostra de água para análise química.")
                                .coletaCientifica(coletas.get(2))
                                .build());

                amostras.add(Amostra.builder()
                                .codCampo("AM-007")
                                .categoriaAmostra(CategoriaAmostra.OUTRA)
                                .volume(new BigDecimal("200.00"))
                                .unidadeMedida(UnidadeMedida.QUILOGRAMA)
                                .dataAcondicionamento(LocalDateTime.of(
                                                2026, 8, 4, 11, 0))
                                .condicaoAmostra(CondicaoConservacaoAmostra.DANIFICADA)
                                .indicacaoMaterialPerigoso(false)
                                .observacoes("Amostra mineral coletada em formação rochosa.")
                                .coletaCientifica(coletas.get(3))
                                .build());

                amostras.add(Amostra.builder()
                                .codCampo("AM-008")
                                .categoriaAmostra(CategoriaAmostra.ARQUEOLOGICA)
                                .volume(new BigDecimal("50.00"))
                                .unidadeMedida(UnidadeMedida.MILILITRO)
                                .dataAcondicionamento(LocalDateTime.of(
                                                2026, 8, 4, 11, 30))
                                .condicaoAmostra(CondicaoConservacaoAmostra.EXCELENTE)
                                .indicacaoMaterialPerigoso(false)
                                .observacoes("Amostra líquida para avaliação microbiológica.")
                                .coletaCientifica(coletas.get(3))
                                .build());

                amostras.add(Amostra.builder()
                                .codCampo("AM-009")
                                .categoriaAmostra(CategoriaAmostra.MINERAL)
                                .volume(new BigDecimal("125.00"))
                                .unidadeMedida(UnidadeMedida.METRO)
                                .dataAcondicionamento(LocalDateTime.of(
                                                2026, 8, 5, 13, 0))
                                .condicaoAmostra(CondicaoConservacaoAmostra.RUIM)
                                .indicacaoMaterialPerigoso(false)
                                .observacoes("Amostra de material orgânico.")
                                .coletaCientifica(coletas.get(4))
                                .build());

                amostras.add(Amostra.builder()
                                .codCampo("AM-010")
                                .categoriaAmostra(CategoriaAmostra.BIOLOGICA)
                                .volume(new BigDecimal("400.00"))
                                .unidadeMedida(UnidadeMedida.MILILITRO)
                                .dataAcondicionamento(LocalDateTime.of(
                                                2026, 8, 5, 13, 30))
                                .condicaoAmostra(CondicaoConservacaoAmostra.BOA)
                                .indicacaoMaterialPerigoso(false)
                                .observacoes("Amostra de água coletada para comparação.")
                                .coletaCientifica(coletas.get(4))
                                .build());

                for (Amostra amostra : amostras) {
                        amostraService.cadastrar(amostra);
                }

                System.out.println(
                                "[seed] " + amostras.size() + " registros de Amostra criados.");
        }

        private Endereco endereco(String logradouro, String numero, String complemento,
                        String bairro, String cidade, UnidadeFederativa uf, String cep) {
                return new Endereco(logradouro, numero, complemento, bairro, cidade, uf, cep);
        }

}
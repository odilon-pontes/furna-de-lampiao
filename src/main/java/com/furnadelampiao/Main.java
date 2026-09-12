package com.furnadelampiao;

import com.furnadelampiao.Repository.GuiaEspeleologicoRepositoryJpa;
import com.furnadelampiao.Repository.PesquisadorRepositoryJpa;
import com.furnadelampiao.Repository.PessoaRepository;
import com.furnadelampiao.Repository.CavernaRepositoryJpa;
import com.furnadelampiao.Repository.ExpedicaoRepositoryJpa;
import com.furnadelampiao.Repository.SetorRepositoryJpa;
import com.furnadelampiao.domain.*;
import com.furnadelampiao.enums.NivelCertificacao;
import com.furnadelampiao.enums.Titulacao;
import com.furnadelampiao.enums.UnidadeFederativa;
import com.furnadelampiao.enums.SituacaoExpedicao;
import com.furnadelampiao.enums.CondicaoSetor;
import com.furnadelampiao.enums.NivelDificuldadeSetor;
import com.furnadelampiao.service.GuiaEspeleologicoService;
import com.furnadelampiao.service.PesquisadorService;
import com.furnadelampiao.service.PessoaService;
import com.furnadelampiao.service.CavernaService;
import com.furnadelampiao.service.ExpedicaoService;
import com.furnadelampiao.service.SetorService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;

public class Main {

        public static void main(String[] args) {
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("furnaPU");
                EntityManager em = emf.createEntityManager();

                PessoaRepository pessoaRepository = new PessoaRepository(em);
                PesquisadorRepositoryJpa pesquisadorRepositoryJpa = new PesquisadorRepositoryJpa(em);
                GuiaEspeleologicoRepositoryJpa guiaEspeleologicoRepositoryJpa = new GuiaEspeleologicoRepositoryJpa(em);
                CavernaRepositoryJpa cavernaRepositoryJpa = new CavernaRepositoryJpa(em);
                ExpedicaoRepositoryJpa expedicaoRepositoryJpa = new ExpedicaoRepositoryJpa(em);
                SetorRepositoryJpa setorRepositoryJpa = new SetorRepositoryJpa(em);

                PessoaService pessoaService = new PessoaService(pessoaRepository);
                PesquisadorService pesquisadorService = new PesquisadorService(pesquisadorRepositoryJpa);
                GuiaEspeleologicoService guiaEspeleologicoService = new GuiaEspeleologicoService(
                                guiaEspeleologicoRepositoryJpa);
                CavernaService cavernaService = new CavernaService(cavernaRepositoryJpa);
                ExpedicaoService expedicaoService = new ExpedicaoService(expedicaoRepositoryJpa);
                SetorService setorService = new SetorService(setorRepositoryJpa);

                Endereco endereco = new Endereco("Av. Dom Pedro II", "003", "casa", "Torre", "João Pessoa",
                                UnidadeFederativa.PB, "54000-000");

                Pessoa pessoa = Pessoa.builder()
                                .nome("amorin")
                                .cpf("10293847561")
                                .dataNasc(LocalDate.of(2001, 01, 01))
                                .email("amorin@email.com")
                                .telefone("83998999900")
                                .endereco(endereco)
                                .build();
                Pesquisador pesquisador = Pesquisador.builder()
                                .nome("João Silva")
                                .cpf("12345678900")
                                .dataNasc(LocalDate.of(1985, 7, 21))
                                .email("joao@email.com")
                                .telefone("83998999999")
                                .endereco(endereco)
                                .numRegistroInstitucional("PES-001")
                                .areaPrincipalPesquisa("Espeleologia")
                                .titulacao(Titulacao.DOUTORADO)
                                .valorDiarioBolsa(new BigDecimal("250.00"))
                                .qtdExpedicoesConcluidas(15)
                                .build();

                GuiaEspeleologico guia = GuiaEspeleologico.builder()
                                .nome("Carlos Eduardo")
                                .cpf("12345678990")
                                .dataNasc(LocalDate.of(1985, 7, 20))
                                .email("carlos@email.com")
                                .telefone("83999999999")
                                .endereco(endereco)
                                .numCredenciamento("GUIA-001")
                                .nivelCertificacao(NivelCertificacao.NIVEL_II)
                                .dataValidadeCertificacao(LocalDate.of(2027, 12, 31))
                                .build();

                Localizacao localizacao = new Localizacao(
                                new BigDecimal("-7.115900"),
                                new BigDecimal("-34.861100"),
                                "SIRGAS2000");

                Caverna caverna = Caverna.builder()
                                .nomeOficial("Furna de Lampião")
                                .codCadastroAmbiental("CANIE-PB-0001")
                                .municipio("João Pessoa")
                                .uf(UnidadeFederativa.PB)
                                .coordenadas(localizacao)
                                .altitude(new BigDecimal("15.50"))
                                .extensao(new BigDecimal("320.00"))
                                .dataUltimaInspecao(LocalDate.of(2026, 6, 15))
                                .acessoAtualmentePermitido(true)
                                .build();

                Expedicao expedicao = Expedicao.builder()
                                .codigo("EXP-2026-001")
                                .titulo("Mapeamento inicial da Furna de Lampião")
                                .objetivo("Levantamento topográfico e coleta de amostras minerais")
                                .inicioPrevisto(LocalDateTime.of(2026, 10, 1, 8, 0))
                                .terminoPrevisto(LocalDateTime.of(2026, 10, 3, 18, 0))
                                .orcamentoAprovado(new BigDecimal("15000.00"))
                                .qtdMaxParticipantes(6)
                                .situacao(SituacaoExpedicao.PLANEJADA)
                                .cancelamentoEmergencial(false)
                                .caverna(caverna)
                                .build();

                Setor setor = Setor.builder()
                                .denominacao("Galeria Principal")
                                .nivelEstimadoDificuldade(NivelDificuldadeSetor.MODERADO)
                                .profundidadeMaxima(new BigDecimal("12.00"))
                                .extensaoAproximada(new BigDecimal("150.00"))
                                .descricao("Trecho de entrada, com passagem alta e piso irregular")
                                .riscoInundacao(new BigDecimal("10.00"))
                                .condicaoCorrente(CondicaoSetor.DISPONIVEL)
                                .build();

                pessoaService.salvar(pessoa);
                pesquisadorService.cadastrar(pesquisador);
                guiaEspeleologicoService.cadastrar(guia);
                cavernaService.cadastrar(caverna);
                expedicaoService.cadastrar(expedicao);
                caverna.adicionarSetor(setor);
                setorService.cadastrar(setor);

                List<Pessoa> pessoas = pessoaService.listarTodos();

                for (Pessoa p : pessoas) {
                        System.out.println(p.getNome());
                }

                em.close();
                emf.close();
        }
}
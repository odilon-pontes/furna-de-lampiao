package com.furnadelampiao;

import com.furnadelampiao.Repository.GuiaEspeleologicoRepositoryJpa;
import com.furnadelampiao.Repository.PesquisadorRepositoryJpa;
import com.furnadelampiao.Repository.PessoaRepository;
import com.furnadelampiao.domain.*;
import com.furnadelampiao.enums.NivelCertificacao;
import com.furnadelampiao.enums.Titulacao;
import com.furnadelampiao.enums.UnidadeFederativa;
import com.furnadelampiao.service.GuiaEspeleologicoService;
import com.furnadelampiao.service.PesquisadorService;
import com.furnadelampiao.service.PessoaService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("furnaPU");
        EntityManager em = emf.createEntityManager();

        PessoaRepository pessoaRepository = new PessoaRepository(em);
        PesquisadorRepositoryJpa pesquisadorRepositoryJpa = new PesquisadorRepositoryJpa(em);
        GuiaEspeleologicoRepositoryJpa guiaEspeleologicoRepositoryJpa = new GuiaEspeleologicoRepositoryJpa(em);

        PessoaService pessoaService = new PessoaService(pessoaRepository);
        PesquisadorService pesquisadorService = new PesquisadorService(pesquisadorRepositoryJpa);
        GuiaEspeleologicoService guiaEspeleologicoService = new GuiaEspeleologicoService(guiaEspeleologicoRepositoryJpa);

        Endereco endereco = new Endereco("Av. Dom Pedro II", "003", "casa", "Torre", "João Pessoa", UnidadeFederativa.PB, "54000-000");

        Pessoa pessoa = Pessoa.builder()
                .nome("amorin")
                .cpf("10293847561")
                .dataNasc(LocalDate.of(2001,01,01))
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


        pessoaService.salvar(pessoa);
        pesquisadorService.cadastrar(pesquisador);
        guiaEspeleologicoService.cadastrar(guia);

        List<Pessoa> pessoas = pessoaService.listarTodos();


        for (Pessoa p : pessoas) {
            System.out.println(p.getNome());
        }

        em.close();
        emf.close();
    }
}
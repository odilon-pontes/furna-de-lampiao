package com.furnadelampiao.seed;

import com.furnadelampiao.repository.*;
import com.furnadelampiao.service.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SeedRunner {

        public static void main(String[] args) {
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("furnaPU");

                EntityManager em = emf.createEntityManager();

                try {
                        PessoaRepository pessoaRepository = new PessoaRepositoryJpa(em);

                        PesquisadorRepository pesquisadorRepository = new PesquisadorRepositoryJpa(em);

                        GuiaEspeleologicoRepository guiaEspeleologicoRepository = new GuiaEspeleologicoRepositoryJpa(
                                        em);

                        ExpedicaoRepository expedicaoRepository = new ExpedicaoRepositoryJpa(em);

                        SetorRepository setorRepository = new SetorRepositoryJpa(em);

                        CavernaRepository cavernaRepository = new CavernaRepositoryJpa(em);

                        ParticipacaoRepository participacaoRepository = new ParticipacaoRepositoryJpa(em);

                        ColetaCientificaRepository coletaCientificaRepository = new ColetaCientificaRepositoryJpa(em);

                        EquipamentoRepository equipamentoRepository = new EquipamentoRepositoryJpa(em);

                        PlanoSegurancaRepository planoSegurancaRepository = new PlanoSegurancaRepositoryJpa(em);

                        MovimentacaoRepository movimentacaoRepository = new MovimentacaoRepositoryJpa(em);

                        AutorizacaoAmbientalRepository autorizacaoAmbientalRepository = new AutorizacaoAmbientalRepositoryJpa(
                                        em);

                        PessoaService pessoaService = new PessoaService(em, pessoaRepository);

                        PesquisadorService pesquisadorService = new PesquisadorService(em, pesquisadorRepository);

                        GuiaEspeleologicoService guiaEspeleologicoService = new GuiaEspeleologicoService(
                                        em,
                                        guiaEspeleologicoRepository);

                        CavernaService cavernaService = new CavernaService(
                                        em,
                                        cavernaRepository);

                        ExpedicaoService expedicaoService = new ExpedicaoService(
                                        em,
                                        expedicaoRepository,
                                        cavernaRepository,
                                        planoSegurancaRepository,
                                        setorRepository);

                        SetorService setorService = new SetorService(
                                        em,
                                        setorRepository,
                                        cavernaRepository);

                        ParticipacaoService participacaoService = new ParticipacaoService(
                                        em,
                                        participacaoRepository);

                        ColetaCientificaService coletaCientificaService = new ColetaCientificaService(
                                        em,
                                        coletaCientificaRepository);

                        EquipamentoService equipamentoService = new EquipamentoService(
                                        em,
                                        equipamentoRepository);

                        MovimentacaoService movimentacaoService = new MovimentacaoService(
                                        em,
                                        movimentacaoRepository);

                        PlanoSegurancaService planoSegurancaService = new PlanoSegurancaService(
                                        em,
                                        planoSegurancaRepository);

                        AutorizacaoAmbientalService autorizacaoAmbientalService = new AutorizacaoAmbientalService(
                                        em,
                                        autorizacaoAmbientalRepository);

                        DatabaseSeeder seeder = new DatabaseSeeder(
                                        pessoaService,
                                        pesquisadorService,
                                        guiaEspeleologicoService,
                                        cavernaService,
                                        expedicaoService,
                                        setorService,
                                        participacaoService,
                                        coletaCientificaService,
                                        equipamentoService,
                                        planoSegurancaService,
                                        movimentacaoService,
                                        autorizacaoAmbientalService);

                        seeder.seedAll();

                        System.out.println("[seed] Concluído.");

                } finally {
                        em.close();
                        emf.close();
                }
        }
}
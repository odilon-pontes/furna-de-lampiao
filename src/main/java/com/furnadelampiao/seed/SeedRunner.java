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
            PessoaRepository pessoaRepository = new PessoaRepository(em);
            PesquisadorRepositoryJpa pesquisadorRepository = new PesquisadorRepositoryJpa(em);
            GuiaEspeleologicoRepositoryJpa guiaEspeleologicoRepository = new GuiaEspeleologicoRepositoryJpa(em);
            ExpedicaoRepositoryJpa expedicaoRepositoryJpa = new ExpedicaoRepositoryJpa(em);
            SetorRepositoryJpa setorRepositoryJpa = new SetorRepositoryJpa(em);
            CavernaRepositoryJpa cavernaRepositoryJpa = new CavernaRepositoryJpa(em);

            PessoaService pessoaService = new PessoaService(em, pessoaRepository);
            PesquisadorService pesquisadorService = new PesquisadorService(em, pesquisadorRepository);
            GuiaEspeleologicoService guiaEspeleologicoService = new GuiaEspeleologicoService(em,
                    guiaEspeleologicoRepository);
            CavernaService cavernaService = new CavernaService(em, cavernaRepositoryJpa);
            ExpedicaoService expedicaoService = new ExpedicaoService(em, expedicaoRepositoryJpa, cavernaRepositoryJpa);
            SetorService setorService = new SetorService(em, setorRepositoryJpa, cavernaRepositoryJpa);

            DatabaseSeeder seeder = new DatabaseSeeder(
                    pessoaService, pesquisadorService, guiaEspeleologicoService,
                    cavernaService, expedicaoService, setorService);
            seeder.seedAll();

            System.out.println("[seed] Concluído.");
        } finally {
            em.close();
            emf.close();
        }
    }
}
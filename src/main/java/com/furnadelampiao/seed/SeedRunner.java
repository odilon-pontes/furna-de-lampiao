package com.furnadelampiao.seed;

import com.furnadelampiao.repository.GuiaEspeleologicoRepositoryJpa;
import com.furnadelampiao.repository.PesquisadorRepositoryJpa;
import com.furnadelampiao.repository.PessoaRepository;
import com.furnadelampiao.service.GuiaEspeleologicoService;
import com.furnadelampiao.service.PesquisadorService;
import com.furnadelampiao.service.PessoaService;

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

            PessoaService pessoaService = new PessoaService(em, pessoaRepository);
            PesquisadorService pesquisadorService = new PesquisadorService(em, pesquisadorRepository);
            GuiaEspeleologicoService guiaEspeleologicoService = new GuiaEspeleologicoService(em, guiaEspeleologicoRepository);

            DatabaseSeeder seeder = new DatabaseSeeder(pessoaService, pesquisadorService, guiaEspeleologicoService);
            seeder.seedAll();

            System.out.println("[seed] Concluído.");
        } finally {
            em.close();
            emf.close();
        }
    }
}
package com.furnadelampiao;

import com.furnadelampiao.domain.Pessoa;
import com.furnadelampiao.repository.GuiaEspeleologicoRepository;
import com.furnadelampiao.repository.GuiaEspeleologicoRepositoryJpa;
import com.furnadelampiao.repository.PesquisadorRepository;
import com.furnadelampiao.repository.PesquisadorRepositoryJpa;
import com.furnadelampiao.repository.PessoaRepository;
import com.furnadelampiao.repository.PessoaRepositoryJpa;
import com.furnadelampiao.service.GuiaEspeleologicoService;
import com.furnadelampiao.service.PesquisadorService;
import com.furnadelampiao.service.PessoaService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("furnaPU");

        EntityManager em = emf.createEntityManager();

        PessoaRepository pessoaRepository = new PessoaRepositoryJpa(em);

        PesquisadorRepository pesquisadorRepository = new PesquisadorRepositoryJpa(em);

        GuiaEspeleologicoRepository guiaEspeleologicoRepository = new GuiaEspeleologicoRepositoryJpa(em);

        PessoaService pessoaService = new PessoaService(em, pessoaRepository);

        PesquisadorService pesquisadorService = new PesquisadorService(em, pesquisadorRepository);

        GuiaEspeleologicoService guiaEspeleologicoService = new GuiaEspeleologicoService(
                em,
                guiaEspeleologicoRepository);

        List<Pessoa> pessoas = pessoaService.listarTodos();

        for (Pessoa p : pessoas) {
            System.out.println(p.getNome());
        }

        em.close();
        emf.close();
    }
}
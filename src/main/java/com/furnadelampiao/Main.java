package com.furnadelampiao;

import com.furnadelampiao.domain.Endereco;
import com.furnadelampiao.domain.Pessoa;
import com.furnadelampiao.domain.UnidadeFederativa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("furnaPU");
        EntityManager em = emf.createEntityManager();
        try  {

            Endereco endereco = new Endereco("Rua das Aroeiras", "100","casa", "centro", "Eldorado", UnidadeFederativa.PE, "50000000");

            Pessoa pessoa = new Pessoa();
            pessoa.setNome("Lampião");
            pessoa.setCpf("12345678901");
            pessoa.setEmail("lampiao@furna.com");
            pessoa.setTelefone("81999998888");
            pessoa.setDataNasc(LocalDate.of(1897, 6, 4));
            pessoa.setSituacaoAtiva(true);
            pessoa.setEndereco(endereco);

            em.getTransaction().begin();
            em.persist(pessoa);
            em.getTransaction().commit();

            System.out.println("Pessoa salva com sucesso! ID gerado: " + pessoa.getId());


            Pessoa pessoaBuscada = em.find(Pessoa.class, pessoa.getId());
            System.out.println("Pessoa buscada do banco: " + pessoaBuscada.getNome() + " - UF: " + pessoaBuscada.getEndereco().getUf());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
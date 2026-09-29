
        package com.furnadelampiao;

import com.furnadelampiao.repository.*;
import com.furnadelampiao.service.*;
import com.furnadelampiao.view.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("furnaPU");
        EntityManager em = emf.createEntityManager();
        Scanner scanner = new Scanner(System.in);

        CavernaRepository cavernaRepository = new CavernaRepositoryJpa(em);
        SetorRepository setorRepository = new SetorRepositoryJpa(em);
        PessoaRepository pessoaRepository = new PessoaRepositoryJpa(em);
        EquipamentoRepository equipamentoRepository = new EquipamentoRepositoryJpa(em);
        PlanoSegurancaRepository planoSegurancaRepository = new PlanoSegurancaRepositoryJpa(em);
        ExpedicaoRepository expedicaoRepository = new ExpedicaoRepositoryJpa(em);

        CavernaService cavernaService =
                new CavernaService(em, cavernaRepository);

        SetorService setorService =
                new SetorService(em, setorRepository, cavernaRepository);

        PessoaService pessoaService =
                new PessoaService(em, pessoaRepository);

        EquipamentoService equipamentoService =
                new EquipamentoService(em, equipamentoRepository);

        PlanoSegurancaService planoSegurancaService =
                new PlanoSegurancaService(em, planoSegurancaRepository);

        ExpedicaoService expedicaoService =
                new ExpedicaoService(
                        em,
                        expedicaoRepository,
                        cavernaRepository,
                        planoSegurancaRepository,
                        setorRepository
                );

        CavernaView cavernaView =
                new CavernaView(cavernaService, scanner);

        SetorView setorView =
                new SetorView(setorService, scanner);

        PessoaView pessoaView =
                new PessoaView(pessoaService, scanner);

        EquipamentoView equipamentoView =
                new EquipamentoView(equipamentoService, scanner);

        PlanoSegurancaView planoSegurancaView =
                new PlanoSegurancaView(planoSegurancaService, scanner);

        ExpedicaoView expedicaoView =
                new ExpedicaoView(expedicaoService, scanner);

        int opcao;

        do {
            System.out.println("\n=== FURNA DE LAMPIÃO ===");
            System.out.println("1 - Cavernas");
            System.out.println("2 - Setores");
            System.out.println("3 - Pessoas");
            System.out.println("4 - Equipamentos");
            System.out.println("5 - Planos de Segurança");
            System.out.println("6 - Expedições");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        cavernaView.menu();
                        break;

                    case 2:
                        setorView.menu();
                        break;

                    case 3:
                        pessoaView.menu();
                        break;

                    case 4:
                        equipamentoView.menu();
                        break;

                    case 5:
                        planoSegurancaView.menu();
                        break;

                    case 6:
                        expedicaoView.menu();
                        break;

                    case 0:
                        System.out.println("Encerrando...");
                        break;

                    default:
                        System.out.println("Opção inválida.");
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Digite uma opção válida.");
                opcao = -1;
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
                opcao = -1;
            }

        } while (opcao != 0);

        scanner.close();
        em.close();
        emf.close();
    }
}

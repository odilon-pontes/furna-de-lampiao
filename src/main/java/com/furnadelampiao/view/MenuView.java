package com.furnadelampiao.view;

import com.furnadelampiao.repository.*;
import com.furnadelampiao.service.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class MenuView {
    private final Scanner scanner;

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("furnaPU");

    private final EntityManager em = emf.createEntityManager();


    private final PessoaRepository pessoaRepository = new PessoaRepositoryJpa(em);

    private final PesquisadorRepository pesquisadorRepository = new PesquisadorRepositoryJpa(em);

    private final GuiaEspeleologicoRepository guiaEspeleologicoRepository = new GuiaEspeleologicoRepositoryJpa(
                em);

    private final ExpedicaoRepository expedicaoRepository = new ExpedicaoRepositoryJpa(em);

    private final SetorRepository setorRepository = new SetorRepositoryJpa(em);

    private final CavernaRepository cavernaRepository = new CavernaRepositoryJpa(em);

    private final ParticipacaoRepository participacaoRepository = new ParticipacaoRepositoryJpa(em);

    private final ColetaCientificaRepository coletaCientificaRepository = new ColetaCientificaRepositoryJpa(em);

    private final EquipamentoRepository equipamentoRepository = new EquipamentoRepositoryJpa(em);

    private final PlanoSegurancaRepository planoSegurancaRepository = new PlanoSegurancaRepositoryJpa(em);

    private final MovimentacaoRepository movimentacaoRepository = new MovimentacaoRepositoryJpa(em);

    private final AmostraRepository amostraRepository = new AmostraRepositoryJpa(em);

    private final AutorizacaoAmbientalRepository autorizacaoAmbientalRepository = new AutorizacaoAmbientalRepositoryJpa(
                em);

    private final RelatorioRepository relatorioRepository = new RelatorioRepositoryJpa(em);

    private final PessoaService pessoaService = new PessoaService(em, pessoaRepository);

    private final PesquisadorService pesquisadorService = new PesquisadorService(em, pesquisadorRepository);

    private final GuiaEspeleologicoService guiaEspeleologicoService = new GuiaEspeleologicoService(
                em,
                guiaEspeleologicoRepository);

    private final CavernaService cavernaService = new CavernaService(
                em,
                cavernaRepository);

    private final ExpedicaoService expedicaoService = new ExpedicaoService(
                em,
                expedicaoRepository,
                cavernaRepository,
                planoSegurancaRepository,
                setorRepository);

    private final SetorService setorService = new SetorService(
                em,
                setorRepository,
                cavernaRepository);

    private final ParticipacaoService participacaoService = new ParticipacaoService(
                em,
                participacaoRepository);

    private final ColetaCientificaService coletaCientificaService = new ColetaCientificaService(
                em,
                coletaCientificaRepository);

    private final EquipamentoService equipamentoService = new EquipamentoService(
                em,
                equipamentoRepository);

    private final MovimentacaoService movimentacaoService = new MovimentacaoService(
                em,
                movimentacaoRepository);

    private final PlanoSegurancaService planoSegurancaService = new PlanoSegurancaService(
                em,
                planoSegurancaRepository);

    private final CavernaView cavernaView = new CavernaView(cavernaService);
    private final SetorView setorView = new SetorView(setorService);
    private final PessoaView pessoaView = new PessoaView(pessoaService);
    private final ExpedicaoView expedicaoView = new ExpedicaoView(expedicaoService);

    public MenuView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void menu() {
        int op;
        do {
            System.out.println();
            System.out.println("================================");
            System.out.println("       FURNA DE LAMPIÃO");
            System.out.println("================================");
            System.out.println("1 - Gerenciar cavernas");
            System.out.println("2 - Gerenciar setores");
            System.out.println("3 - Gerenciar pessoas");
            System.out.println("4 - Gerenciar expedições");
            System.out.println("0 - Sair");
            System.out.println("================================");
            System.out.print("Escolha: ");

            op = lerOpcao();

            try {
                switch (op) {
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
                        expedicaoView.menu();
                        break;
                    case 5:
                        break;
                    case 0:
                        System.out.println("Voltando...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while(op != 0);
    }

    public int lerOpcao() {
        return Integer.parseInt(scanner.nextLine());
    }
}

        package com.furnadelampiao.view;

import com.furnadelampiao.repository.*;
import com.furnadelampiao.service.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class MenuView {

    private final Scanner scanner;

    private final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("furnaPU");

    private final EntityManager em = emf.createEntityManager();

    private final PessoaRepository pessoaRepository =
            new PessoaRepositoryJpa(em);

    private final PesquisadorRepository pesquisadorRepository =
            new PesquisadorRepositoryJpa(em);

    private final GuiaEspeleologicoRepository guiaEspeleologicoRepository =
            new GuiaEspeleologicoRepositoryJpa(em);

    private final ExpedicaoRepository expedicaoRepository =
            new ExpedicaoRepositoryJpa(em);

    private final SetorRepository setorRepository =
            new SetorRepositoryJpa(em);

    private final CavernaRepository cavernaRepository =
            new CavernaRepositoryJpa(em);

    private final ParticipacaoRepository participacaoRepository =
            new ParticipacaoRepositoryJpa(em);

    private final ColetaCientificaRepository coletaCientificaRepository =
            new ColetaCientificaRepositoryJpa(em);

    private final EquipamentoRepository equipamentoRepository =
            new EquipamentoRepositoryJpa(em);

    private final PlanoSegurancaRepository planoSegurancaRepository =
            new PlanoSegurancaRepositoryJpa(em);

    private final MovimentacaoRepository movimentacaoRepository =
            new MovimentacaoRepositoryJpa(em);

    private final AmostraRepository amostraRepository =
            new AmostraRepositoryJpa(em);

    private final AutorizacaoAmbientalRepository autorizacaoAmbientalRepository =
            new AutorizacaoAmbientalRepositoryJpa(em);

    private final RelatorioRepository relatorioRepository =
            new RelatorioRepositoryJpa(em);

    private final PessoaService pessoaService =
            new PessoaService(em, pessoaRepository);

    private final PesquisadorService pesquisadorService =
            new PesquisadorService(em, pesquisadorRepository);

    private final GuiaEspeleologicoService guiaEspeleologicoService =
            new GuiaEspeleologicoService(
                    em,
                    guiaEspeleologicoRepository);

    private final CavernaService cavernaService =
            new CavernaService(
                    em,
                    cavernaRepository);

    private final ExpedicaoService expedicaoService =
            new ExpedicaoService(
                    em,
                    expedicaoRepository,
                    cavernaRepository,
                    planoSegurancaRepository,
                    setorRepository);

    private final SetorService setorService =
            new SetorService(
                    em,
                    setorRepository,
                    cavernaRepository);

    private final ParticipacaoService participacaoService =
            new ParticipacaoService(
                    em,
                    participacaoRepository);

    private final ColetaCientificaService coletaCientificaService =
            new ColetaCientificaService(
                    em,
                    coletaCientificaRepository);

    private final EquipamentoService equipamentoService =
            new EquipamentoService(
                    em,
                    equipamentoRepository);

    private final MovimentacaoService movimentacaoService =
            new MovimentacaoService(
                    em,
                    movimentacaoRepository);

    private final PlanoSegurancaService planoSegurancaService =
            new PlanoSegurancaService(
                    em,
                    planoSegurancaRepository);

    private final AmostraService amostraService =
            new AmostraService(
                    em,
                    amostraRepository);

    private final AutorizacaoAmbientalService autorizacaoAmbientalService =
            new AutorizacaoAmbientalService(
                    em,
                    autorizacaoAmbientalRepository);

    private final RelatorioService relatorioService =
            new RelatorioService(
                    em,
                    relatorioRepository);


    private final CavernaView cavernaView;
    private final SetorView setorView;
    private final PessoaView pessoaView;
    private final PesquisadorView pesquisadorView;
    private final GuiaEspeleologicoView guiaEspeleologicoView;
    private final ExpedicaoView expedicaoView;
    private final ParticipacaoView participacaoView;
    private final ColetaCientificaView coletaCientificaView;
    private final EquipamentoView equipamentoView;
    private final MovimentacaoView movimentacaoView;
    private final PlanoSegurancaView planoSegurancaView;
    private AmostraView amostraView;
    private final AutorizacaoAmbientalView autorizacaoAmbientalView;
    private final RelatorioView relatorioView;

    public MenuView(Scanner scanner) {
        this.scanner = scanner;
        this.cavernaView = new CavernaView(cavernaService, scanner);
        this.setorView = new SetorView(setorService, scanner);
        this.pessoaView = new PessoaView(pessoaService, scanner);
        this.pesquisadorView = new PesquisadorView(pesquisadorService, scanner);
        this.guiaEspeleologicoView =
                new GuiaEspeleologicoView(guiaEspeleologicoService, scanner);
        this.expedicaoView = new ExpedicaoView(expedicaoService, scanner);
        this.participacaoView =
                new ParticipacaoView(participacaoService, scanner);
        this.coletaCientificaView =
                new ColetaCientificaView(coletaCientificaService, scanner);
        this.equipamentoView =
                new EquipamentoView(equipamentoService, scanner);
        this.movimentacaoView =
                new MovimentacaoView(movimentacaoService, scanner);
        this.planoSegurancaView =
                new PlanoSegurancaView(planoSegurancaService, scanner);
        this.amostraView =
                new AmostraView(amostraService, scanner);
        this.autorizacaoAmbientalView =
                new AutorizacaoAmbientalView(autorizacaoAmbientalService, scanner);
        this.relatorioView =
                new RelatorioView(relatorioService, scanner);
    }

    public void menu() {
        int op;

        do {
            System.out.println();
            System.out.println("================================");
            System.out.println("       FURNA DE LAMPIÃO");
            System.out.println("================================");
            System.out.println("01 - Gerenciar cavernas");
            System.out.println("02 - Gerenciar setores");
            System.out.println("03 - Gerenciar pessoas");
            System.out.println("04 - Gerenciar pesquisadores");
            System.out.println("05 - Gerenciar guias espeleológicos");
            System.out.println("06 - Gerenciar expedições");
            System.out.println("07 - Gerenciar participações");
            System.out.println("08 - Gerenciar coletas científicas");
            System.out.println("09 - Gerenciar amostras");
            System.out.println("10 - Gerenciar equipamentos");
            System.out.println("11 - Gerenciar movimentações");
            System.out.println("12 - Gerenciar planos de segurança");
            System.out.println("13 - Gerenciar autorizações ambientais");
            System.out.println("14 - Gerenciar relatórios");
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
                        pesquisadorView.menu();
                        break;

                    case 5:
                        guiaEspeleologicoView.menu();
                        break;

                    case 6:
                        expedicaoView.menu();
                        break;

                    case 7:
                        participacaoView.menu();
                        break;

                    case 8:
                        coletaCientificaView.menu();
                        break;

                    case 9:
                        amostraView.menu();
                        break;

                    case 10:
                        equipamentoView.menu();
                        break;

                    case 11:
                        movimentacaoView.menu();
                        break;

                    case 12:
                        planoSegurancaView.menu();
                        break;

                    case 13:
                        autorizacaoAmbientalView.menu();
                        break;

                    case 14:
                        relatorioView.menu();
                        break;

                    case 0:
                        System.out.println("Encerrando...");
                        fecharRecursos();
                        break;

                    default:
                        System.out.println("Opção inválida.");
                        break;
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }

        } while (op != 0);
    }

    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Digite uma opção válida.");
            return -1;
        }
    }

    private void fecharRecursos() {
        if (em.isOpen()) {
            em.close();
        }

        if (emf.isOpen()) {
            emf.close();
        }
    }
}

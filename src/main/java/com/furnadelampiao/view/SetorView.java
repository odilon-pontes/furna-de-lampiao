package com.furnadelampiao.view;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.Setor;
import com.furnadelampiao.enums.CondicaoSetor;
import com.furnadelampiao.enums.NivelDificuldadeSetor;
import com.furnadelampiao.service.SetorService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class SetorView {

    private final SetorService service;
    private final Scanner scanner;

    public SetorView(SetorService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== SETORES ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Buscar por ID");
            System.out.println("3 - Listar todos");
            System.out.println("4 - Listar por caverna");
            System.out.println("5 - Listar por nível de dificuldade");
            System.out.println("6 - Atualizar");
            System.out.println("7 - Remover");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");

            opcao = Integer.parseInt(scanner.nextLine());

            try {
                switch (opcao) {
                    case 1:
                        cadastrar();
                        break;
                    case 2:
                        buscarPorId();
                        break;
                    case 3:
                        listarTodos();
                        break;
                    case 4:
                        listarPorCaverna();
                        break;
                    case 5:
                        listarPorNivelDificuldade();
                        break;
                    case 6:
                        atualizar();
                        break;
                    case 7:
                        removerPorId();
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
        } while (opcao != 0);
    }

    private void cadastrar() {
        System.out.println("\n=== CADASTRAR SETOR ===");

        Setor setor = lerDadosSetor();

        service.cadastrar(setor);

        System.out.println("Setor cadastrado com sucesso.");
    }

    private void buscarPorId() {
        System.out.print("\nID do setor: ");
        Long id = Long.parseLong(scanner.nextLine());

        Setor setor = service.buscarPorId(id);

        if (setor == null) {
            System.out.println("Setor não encontrado.");
            return;
        }

        exibirSetor(setor);
    }

    private void listarTodos() {
        System.out.println("\n=== TODOS OS SETORES ===");

        List<Setor> setores = service.listarTodos();

        if (setores.isEmpty()) {
            System.out.println("Nenhum setor cadastrado.");
            return;
        }

        setores.forEach(this::exibirSetor);
    }

    private void listarPorCaverna() {
        System.out.println("\n=== SETORES POR CAVERNA ===");

        System.out.print("ID da caverna: ");
        Long cavernaId = Long.parseLong(scanner.nextLine());

        List<Setor> setores = service.listarPorCaverna(cavernaId);

        if (setores.isEmpty()) {
            System.out.println("Nenhum setor encontrado para essa caverna.");
            return;
        }

        setores.forEach(this::exibirSetor);
    }

    private void listarPorNivelDificuldade() {
        System.out.println("\n=== SETORES POR NÍVEL DE DIFICULDADE ===");

        NivelDificuldadeSetor nivel = lerNivelDificuldade();

        List<Setor> setores = service.listarPorNivelDificuldade(nivel);

        if (setores.isEmpty()) {
            System.out.println("Nenhum setor encontrado para esse nível.");
            return;
        }

        setores.forEach(this::exibirSetor);
    }

    private void atualizar() {
        System.out.println("\n=== ATUALIZAR SETOR ===");

        System.out.print("ID do setor: ");
        Long id = Long.parseLong(scanner.nextLine());

        Setor setorExistente = service.buscarPorId(id);

        if (setorExistente == null) {
            System.out.println("Setor não encontrado.");
            return;
        }

        exibirSetor(setorExistente);

        System.out.println("\nInforme os novos dados:");

        Setor setor = lerDadosSetor();
        setor.setId(id);

        service.atualizar(setor);

        System.out.println("Setor atualizado com sucesso.");
    }

    private void removerPorId() {
        System.out.println("\n=== REMOVER SETOR ===");

        System.out.print("ID do setor: ");
        Long id = Long.parseLong(scanner.nextLine());

        Setor setor = service.buscarPorId(id);

        if (setor == null) {
            System.out.println("Setor não encontrado.");
            return;
        }

        exibirSetor(setor);

        System.out.print("Confirma a remoção? (S/N): ");
        String confirmacao = scanner.nextLine();

        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println("Operação cancelada.");
            return;
        }

        service.removerPorId(id);

        System.out.println("Setor removido com sucesso.");
    }

    private Setor lerDadosSetor() {
        System.out.print("Denominação: ");
        String denominacao = scanner.nextLine();

        NivelDificuldadeSetor nivel = lerNivelDificuldade();

        System.out.print("Profundidade máxima: ");
        String profundidadeTexto = scanner.nextLine();
        BigDecimal profundidadeMaxima = profundidadeTexto.isBlank()
                ? null
                : new BigDecimal(profundidadeTexto);

        System.out.print("Extensão aproximada: ");
        String extensaoTexto = scanner.nextLine();
        BigDecimal extensaoAproximada = extensaoTexto.isBlank()
                ? null
                : new BigDecimal(extensaoTexto);

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        System.out.print("Risco de inundação: ");
        String riscoTexto = scanner.nextLine();
        BigDecimal riscoInundacao = riscoTexto.isBlank()
                ? null
                : new BigDecimal(riscoTexto);

        CondicaoSetor condicaoCorrente = lerCondicaoCorrente();

        System.out.print("ID da caverna: ");
        Long cavernaId = Long.parseLong(scanner.nextLine());

        Caverna caverna = Caverna.builder()
                .id(cavernaId)
                .build();

        return Setor.builder()
                .denominacao(denominacao)
                .nivelEstimadoDificuldade(nivel)
                .profundidadeMaxima(profundidadeMaxima)
                .extensaoAproximada(extensaoAproximada)
                .descricao(descricao)
                .riscoInundacao(riscoInundacao)
                .condicaoCorrente(condicaoCorrente)
                .caverna(caverna)
                .build();
    }

    private NivelDificuldadeSetor lerNivelDificuldade() {
        System.out.println("\nNíveis de dificuldade:");

        NivelDificuldadeSetor[] niveis = NivelDificuldadeSetor.values();

        for (int i = 0; i < niveis.length; i++) {
            System.out.println(i + " - " + niveis[i]);
        }

        System.out.print("Escolha o nível: ");
        int indice = Integer.parseInt(scanner.nextLine());

        if (indice < 0 || indice >= niveis.length) {
            throw new IllegalArgumentException("Nível de dificuldade inválido.");
        }

        return niveis[indice];
    }

    private CondicaoSetor lerCondicaoCorrente() {
        System.out.println("\nCondições de corrente:");

        CondicaoSetor[] condicoes = CondicaoSetor.values();

        for (int i = 0; i < condicoes.length; i++) {
            System.out.println(i + " - " + condicoes[i]);
        }

        System.out.print("Escolha a condição: ");
        int indice = Integer.parseInt(scanner.nextLine());

        if (indice < 0 || indice >= condicoes.length) {
            throw new IllegalArgumentException("Condição corrente inválida.");
        }

        return condicoes[indice];
    }

    private void exibirSetor(Setor setor) {
        System.out.println("\n------------------------------");
        System.out.println("ID: " + setor.getId());
        System.out.println("Denominação: " + setor.getDenominacao());
        System.out.println("Nível de dificuldade: "
                + setor.getNivelEstimadoDificuldade());
        System.out.println("Profundidade máxima: "
                + setor.getProfundidadeMaxima());
        System.out.println("Extensão aproximada: "
                + setor.getExtensaoAproximada());
        System.out.println("Descrição: " + setor.getDescricao());
        System.out.println("Risco de inundação: "
                + setor.getRiscoInundacao());
        System.out.println("Condição corrente: "
                + setor.getCondicaoCorrente());

        if (setor.getCaverna() != null) {
            System.out.println("ID da caverna: "
                    + setor.getCaverna().getId());
        }

        System.out.println("------------------------------");
    }
}

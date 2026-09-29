package com.furnadelampiao.view;

import com.furnadelampiao.domain.AutorizacaoAmbiental;
import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.enums.SituacaoAutorizacao;
import com.furnadelampiao.service.AutorizacaoAmbientalService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AutorizacaoAmbientalView {

    private final AutorizacaoAmbientalService service;
    private final Scanner scanner;

    public AutorizacaoAmbientalView(
            AutorizacaoAmbientalService service,
            Scanner scanner) {

        this.service = service;
        this.scanner = scanner;
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== AUTORIZAÇÕES AMBIENTAIS ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Buscar por ID");
            System.out.println("3 - Buscar arquivo PDF");
            System.out.println("4 - Listar todas");
            System.out.println("5 - Atualizar");
            System.out.println("6 - Remover");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        cadastrar();
                        break;
                    case 2:
                        buscarPorId();
                        break;
                    case 3:
                        buscarArquivoPdf();
                        break;
                    case 4:
                        listarTodos();
                        break;
                    case 5:
                        atualizar();
                        break;
                    case 6:
                        removerPorId();
                        break;
                    case 0:
                        System.out.println("Voltando...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                        pressionarEnter();
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Digite uma opção válida.");
                opcao = -1;
                pressionarEnter();

            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
                opcao = -1;
                pressionarEnter();
            }

        } while (opcao != 0);
    }

    private void cadastrar() {
        System.out.println("\n=== CADASTRAR AUTORIZAÇÃO AMBIENTAL ===");

        AutorizacaoAmbiental autorizacao =
                lerDadosAutorizacao();

        service.cadastrar(autorizacao);

        System.out.println(
                "Autorização ambiental cadastrada com sucesso.");

        pressionarEnter();
    }

    private void buscarPorId() {
        Long id = lerLong("\nID da autorização: ");

        AutorizacaoAmbiental autorizacao =
                service.buscarPorId(id);

        if (autorizacao == null) {
            System.out.println("Autorização não encontrada.");
            pressionarEnter();
            return;
        }

        exibirAutorizacao(autorizacao);

        pressionarEnter();
    }

    private void buscarArquivoPdf() {
        Long id = lerLong("\nID da autorização: ");

        byte[] arquivo = service.buscarArquivoPdf(id);

        if (arquivo == null || arquivo.length == 0) {
            System.out.println(
                    "Nenhum arquivo PDF encontrado.");
            pressionarEnter();
            return;
        }

        System.out.println(
                "Arquivo PDF encontrado. Tamanho: "
                        + arquivo.length
                        + " bytes.");

        pressionarEnter();
    }

    private void listarTodos() {
        System.out.println("\n=== TODAS AS AUTORIZAÇÕES ===");

        List<AutorizacaoAmbiental> autorizacoes =
                service.listarTodos();

        if (autorizacoes.isEmpty()) {
            System.out.println(
                    "Nenhuma autorização cadastrada.");
            pressionarEnter();
            return;
        }

        autorizacoes.forEach(this::exibirAutorizacao);

        pressionarEnter();
    }

    private void atualizar() {
        System.out.println(
                "\n=== ATUALIZAR AUTORIZAÇÃO AMBIENTAL ===");

        Long id = lerLong("ID da autorização: ");

        AutorizacaoAmbiental existente =
                service.buscarPorId(id);

        if (existente == null) {
            System.out.println(
                    "Autorização não encontrada.");
            pressionarEnter();
            return;
        }

        exibirAutorizacao(existente);

        System.out.println("\nInforme os novos dados:");

        AutorizacaoAmbiental autorizacao =
                lerDadosAutorizacao();

        autorizacao.setId(id);

        service.atualizar(autorizacao);

        System.out.println(
                "Autorização atualizada com sucesso.");

        pressionarEnter();
    }

    private void removerPorId() {
        System.out.println(
                "\n=== REMOVER AUTORIZAÇÃO AMBIENTAL ===");

        Long id = lerLong("ID da autorização: ");

        AutorizacaoAmbiental autorizacao =
                service.buscarPorId(id);

        if (autorizacao == null) {
            System.out.println(
                    "Autorização não encontrada.");
            pressionarEnter();
            return;
        }

        exibirAutorizacao(autorizacao);

        System.out.print(
                "Confirma a remoção? (S/N): ");

        String confirmacao = scanner.nextLine();

        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println("Operação cancelada.");
            pressionarEnter();
            return;
        }

        service.removerPorId(id);

        System.out.println(
                "Autorização removida com sucesso.");

        pressionarEnter();
    }

    private AutorizacaoAmbiental lerDadosAutorizacao() {

        System.out.print("Número da autorização: ");

        Integer num =
                Integer.parseInt(scanner.nextLine());

        System.out.print("Órgão emissor: ");
        String orgaoEmissor = scanner.nextLine();

        System.out.print(
                "Data de emissão (AAAA-MM-DD): ");

        String emissaoTexto = scanner.nextLine();

        LocalDate dataEmissao =
                emissaoTexto.isBlank()
                        ? null
                        : LocalDate.parse(emissaoTexto);

        System.out.print(
                "Data de validade (AAAA-MM-DD): ");

        String validadeTexto = scanner.nextLine();

        LocalDate validade =
                validadeTexto.isBlank()
                        ? null
                        : LocalDate.parse(validadeTexto);

        SituacaoAutorizacao situacao =
                lerSituacaoAutorizacao();

        System.out.print(
                "Observações (opcional): ");

        String observacoes = scanner.nextLine();

        byte[] arquivoPdfAssinado =
                lerArquivoBinario(
                        "Caminho do arquivo PDF assinado");

        Long expedicaoId =
                lerLong("ID da expedição: ");

        return AutorizacaoAmbiental.builder()
                .num(num)
                .orgaoEmissor(orgaoEmissor)
                .dataEmissao(dataEmissao)
                .validade(validade)
                .situacao(situacao)
                .observacoes(
                        observacoes.isBlank()
                                ? null
                                : observacoes)
                .arquivoPdfAssinado(arquivoPdfAssinado)
                .expedicao(
                        Expedicao.builder()
                                .id(expedicaoId)
                                .build())
                .build();
    }

    private SituacaoAutorizacao lerSituacaoAutorizacao() {
        SituacaoAutorizacao[] situacoes =
                SituacaoAutorizacao.values();

        System.out.println(
                "Situações de autorização:");

        for (int i = 0; i < situacoes.length; i++) {
            System.out.println(
                    i + " - " + situacoes[i]);
        }

        System.out.print("Escolha a situação: ");

        int indice =
                Integer.parseInt(scanner.nextLine());

        if (indice < 0
                || indice >= situacoes.length) {

            throw new IllegalArgumentException(
                    "Situação de autorização inválida.");
        }

        return situacoes[indice];
    }

    private byte[] lerArquivoBinario(String mensagem) {
        System.out.print(
                mensagem + " (vazio para pular): ");

        String caminho = scanner.nextLine();

        if (caminho.isBlank()) {
            return null;
        }

        try {
            return Files.readAllBytes(
                    Paths.get(caminho));

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Não foi possível ler o arquivo: "
                            + e.getMessage());
        }
    }

    private Long lerLong(String mensagem) {
        System.out.print(mensagem);
        return Long.parseLong(scanner.nextLine());
    }

    private void exibirAutorizacao(
            AutorizacaoAmbiental autorizacao) {

        System.out.println(
                "\n------------------------------");

        System.out.println(
                "ID: " + autorizacao.getId());

        System.out.println(
                "Número: " + autorizacao.getNum());

        System.out.println(
                "Órgão emissor: "
                        + autorizacao.getOrgaoEmissor());

        System.out.println(
                "Data de emissão: "
                        + autorizacao.getDataEmissao());

        System.out.println(
                "Validade: "
                        + autorizacao.getValidade());

        System.out.println(
                "Situação: "
                        + autorizacao.getSituacao());

        System.out.println(
                "Observações: "
                        + autorizacao.getObservacoes());

        if (autorizacao.getArquivoPdfAssinado() != null) {
            System.out.println(
                    "Arquivo PDF: "
                            + autorizacao
                            .getArquivoPdfAssinado()
                            .length
                            + " bytes");
        }

        if (autorizacao.getExpedicao() != null) {
            System.out.println(
                    "ID da expedição: "
                            + autorizacao
                            .getExpedicao()
                            .getId());
        }

        System.out.println(
                "------------------------------");
    }

    private void pressionarEnter() {
        System.out.println();
        System.out.println(
                "Pressione ENTER para continuar...");
        scanner.nextLine();
    }
}
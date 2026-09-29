package com.furnadelampiao.view;

import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.domain.Relatorio;
import com.furnadelampiao.enums.SituacaoRelatorioFinal;
import com.furnadelampiao.service.RelatorioService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class RelatorioView {

    private final RelatorioService service;
    private final Scanner scanner;

    public RelatorioView(
            RelatorioService service,
            Scanner scanner) {

        this.service = service;
        this.scanner = scanner;
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== RELATÓRIOS FINAIS ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Buscar por ID");
            System.out.println("3 - Buscar por expedição");
            System.out.println("4 - Buscar arquivo");
            System.out.println("5 - Listar todos");
            System.out.println("6 - Atualizar");
            System.out.println("7 - Remover");
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
                        buscarPorExpedicaoId();
                        break;

                    case 4:
                        buscarArquivo();
                        break;

                    case 5:
                        listarTodos();
                        break;

                    case 6:
                        atualizar();
                        break;

                    case 7:
                        remover();
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
        System.out.println("\n=== CADASTRAR RELATÓRIO FINAL ===");

        Relatorio relatorio = lerDadosRelatorio();

        service.cadastrar(relatorio);

        System.out.println(
                "Relatório cadastrado com sucesso."
        );

        pressionarEnter();
    }

    private void buscarPorId() {
        Long id = lerLong("\nID do relatório: ");

        Relatorio relatorio = service.buscarPorId(id);

        if (relatorio == null) {
            System.out.println("Relatório não encontrado.");
            pressionarEnter();
            return;
        }

        exibirRelatorio(relatorio);
        pressionarEnter();
    }

    private void buscarPorExpedicaoId() {
        Long id = lerLong("\nID da expedição: ");

        Relatorio relatorio =
                service.buscarPorExpedicaoId(id);

        if (relatorio == null) {
            System.out.println(
                    "Nenhum relatório encontrado para essa expedição."
            );
            pressionarEnter();
            return;
        }

        exibirRelatorio(relatorio);
        pressionarEnter();
    }

    private void buscarArquivo() {
        Long id = lerLong("\nID do relatório: ");

        byte[] arquivo = service.buscarArquivo(id);

        if (arquivo == null || arquivo.length == 0) {
            System.out.println("Nenhum arquivo encontrado.");
            pressionarEnter();
            return;
        }

        System.out.println(
                "Arquivo encontrado. Tamanho: "
                        + arquivo.length
                        + " bytes."
        );

        pressionarEnter();
    }

    private void listarTodos() {
        System.out.println("\n=== TODOS OS RELATÓRIOS ===");

        List<Relatorio> relatorios =
                service.listarTodos();

        if (relatorios.isEmpty()) {
            System.out.println(
                    "Nenhum relatório cadastrado."
            );
            pressionarEnter();
            return;
        }

        relatorios.forEach(this::exibirRelatorio);

        pressionarEnter();
    }

    private void atualizar() {
        System.out.println("\n=== ATUALIZAR RELATÓRIO ===");

        Long id = lerLong("ID do relatório: ");

        Relatorio existente =
                service.buscarPorId(id);

        if (existente == null) {
            System.out.println(
                    "Relatório não encontrado."
            );
            pressionarEnter();
            return;
        }

        exibirRelatorio(existente);

        System.out.print(
                "\nConfirma a atualização? (S/N): "
        );

        String confirmacao = scanner.nextLine();

        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println(
                    "Operação cancelada."
            );
            pressionarEnter();
            return;
        }

        Relatorio relatorio =
                lerDadosAtualizacao(existente);

        relatorio.setId(id);

        service.atualizar(relatorio);

        System.out.println(
                "Relatório atualizado com sucesso."
        );

        pressionarEnter();
    }

    private void remover() {
        System.out.println("\n=== REMOVER RELATÓRIO ===");

        Long id = lerLong("ID do relatório: ");

        Relatorio relatorio =
                service.buscarPorId(id);

        if (relatorio == null) {
            System.out.println(
                    "Relatório não encontrado."
            );
            pressionarEnter();
            return;
        }

        exibirRelatorio(relatorio);

        System.out.print(
                "\nConfirma a remoção? (S/N): "
        );

        String confirmacao =
                scanner.nextLine();

        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println(
                    "Operação cancelada."
            );
            pressionarEnter();
            return;
        }

        service.remover(id);

        System.out.println(
                "Relatório removido com sucesso."
        );

        pressionarEnter();
    }

    private Relatorio lerDadosRelatorio() {
        System.out.print("Título: ");
        String titulo = scanner.nextLine();

        if (titulo.isBlank()) {
            throw new IllegalArgumentException(
                    "Título é obrigatório."
            );
        }

        System.out.print("Resumo: ");
        String resumo = scanner.nextLine();

        System.out.print(
                "Data de submissão (AAAA-MM-DDTHH:mm:ss, vazio = nenhuma): "
        );

        String dataTexto = scanner.nextLine();

        LocalDateTime dataSubmissao =
                dataTexto.isBlank()
                        ? null
                        : LocalDateTime.parse(dataTexto);

        System.out.print(
                "Número total de páginas: "
        );

        Integer numeroTotalPaginas =
                Integer.parseInt(scanner.nextLine());

        if (numeroTotalPaginas < 0) {
            throw new IllegalArgumentException(
                    "Número de páginas não pode ser negativo."
            );
        }

        SituacaoRelatorioFinal situacao =
                lerSituacaoRelatorioFinal();

        byte[] arquivoCompleto =
                lerArquivoBinario(
                        "Caminho do arquivo completo do relatório"
                );

        if (arquivoCompleto == null) {
            throw new IllegalArgumentException(
                    "Arquivo completo do relatório é obrigatório."
            );
        }

        Boolean publicacaoAutorizada =
                lerBooleano(
                        "Publicação autorizada? (S/N): "
                );

        Long expedicaoId =
                lerLong("ID da expedição: ");

        return Relatorio.builder()
                .titulo(titulo)
                .resumo(resumo)
                .dataSubmissao(dataSubmissao)
                .numeroTotalPaginas(numeroTotalPaginas)
                .situacaoRelatorioFinal(situacao)
                .arquivoCompleto(arquivoCompleto)
                .publicacaoAutorizada(publicacaoAutorizada)
                .expedicao(
                        Expedicao.builder()
                                .id(expedicaoId)
                                .build()
                )
                .build();
    }

    private Relatorio lerDadosAtualizacao(
            Relatorio existente) {

        System.out.println(
                "\nPressione ENTER para manter o valor atual."
        );

        System.out.print(
                "Título ["
                        + existente.getTitulo()
                        + "]: "
        );

        String titulo = scanner.nextLine();

        if (titulo.isBlank()) {
            titulo = existente.getTitulo();
        }

        System.out.print(
                "Resumo ["
                        + existente.getResumo()
                        + "]: "
        );

        String resumo = scanner.nextLine();

        if (resumo.isBlank()) {
            resumo = existente.getResumo();
        }

        System.out.print(
                "Data de submissão ["
                        + existente.getDataSubmissao()
                        + "]: "
        );

        String dataTexto = scanner.nextLine();

        LocalDateTime dataSubmissao;

        if (dataTexto.isBlank()) {
            dataSubmissao =
                    existente.getDataSubmissao();
        } else {
            dataSubmissao =
                    LocalDateTime.parse(dataTexto);
        }

        System.out.print(
                "Número total de páginas ["
                        + existente.getNumeroTotalPaginas()
                        + "]: "
        );

        String paginasTexto =
                scanner.nextLine();

        Integer numeroTotalPaginas;

        if (paginasTexto.isBlank()) {
            numeroTotalPaginas =
                    existente.getNumeroTotalPaginas();
        } else {
            numeroTotalPaginas =
                    Integer.parseInt(paginasTexto);

            if (numeroTotalPaginas < 0) {
                throw new IllegalArgumentException(
                        "Número de páginas não pode ser negativo."
                );
            }
        }

        SituacaoRelatorioFinal situacao =
                lerSituacaoAtualizacao(
                        existente.getSituacaoRelatorioFinal()
                );

        byte[] arquivoCompleto =
                lerArquivoAtualizacao(
                        existente.getArquivoCompleto()
                );

        Boolean publicacaoAutorizada =
                lerBooleanoAtualizacao(
                        existente.getPublicacaoAutorizada()
                );

        Long expedicaoId =
                lerExpedicaoAtualizacao(
                        existente
                );

        return Relatorio.builder()
                .titulo(titulo)
                .resumo(resumo)
                .dataSubmissao(dataSubmissao)
                .numeroTotalPaginas(numeroTotalPaginas)
                .situacaoRelatorioFinal(situacao)
                .arquivoCompleto(arquivoCompleto)
                .publicacaoAutorizada(publicacaoAutorizada)
                .expedicao(
                        Expedicao.builder()
                                .id(expedicaoId)
                                .build()
                )
                .build();
    }

    private SituacaoRelatorioFinal lerSituacaoRelatorioFinal() {
        SituacaoRelatorioFinal[] situacoes =
                SituacaoRelatorioFinal.values();

        System.out.println(
                "Situações do relatório:"
        );

        for (int i = 0; i < situacoes.length; i++) {
            System.out.println(
                    i + " - " + situacoes[i]
            );
        }

        System.out.print(
                "Escolha a situação: "
        );

        int indice =
                Integer.parseInt(scanner.nextLine());

        if (indice < 0 ||
                indice >= situacoes.length) {

            throw new IllegalArgumentException(
                    "Situação do relatório inválida."
            );
        }

        return situacoes[indice];
    }

    private SituacaoRelatorioFinal lerSituacaoAtualizacao(
            SituacaoRelatorioFinal atual) {

        System.out.println(
                "Situação atual: " + atual
        );

        System.out.println(
                "Escolha uma nova situação ou pressione ENTER para manter:"
        );

        SituacaoRelatorioFinal[] situacoes =
                SituacaoRelatorioFinal.values();

        for (int i = 0; i < situacoes.length; i++) {
            System.out.println(
                    i + " - " + situacoes[i]
            );
        }

        System.out.print("Situação: ");

        String valor = scanner.nextLine();

        if (valor.isBlank()) {
            return atual;
        }

        int indice = Integer.parseInt(valor);

        if (indice < 0 ||
                indice >= situacoes.length) {

            throw new IllegalArgumentException(
                    "Situação do relatório inválida."
            );
        }

        return situacoes[indice];
    }

    private byte[] lerArquivoBinario(String mensagem) {
        System.out.print(mensagem + ": ");

        String caminho =
                scanner.nextLine();

        if (caminho.isBlank()) {
            return null;
        }

        try {
            return Files.readAllBytes(
                    Paths.get(caminho)
            );
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Não foi possível ler o arquivo: "
                            + e.getMessage()
            );
        }
    }

    private byte[] lerArquivoAtualizacao(
            byte[] arquivoAtual) {

        System.out.print(
                "Caminho do novo arquivo "
                        + "(ENTER para manter o atual): "
        );

        String caminho =
                scanner.nextLine();

        if (caminho.isBlank()) {
            return arquivoAtual;
        }

        try {
            return Files.readAllBytes(
                    Paths.get(caminho)
            );
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Não foi possível ler o arquivo: "
                            + e.getMessage()
            );
        }
    }

    private Boolean lerBooleano(String mensagem) {
        System.out.print(mensagem);

        String valor =
                scanner.nextLine().trim();

        if (valor.equalsIgnoreCase("S")) {
            return true;
        }

        if (valor.equalsIgnoreCase("N")) {
            return false;
        }

        throw new IllegalArgumentException(
                "Digite apenas S ou N."
        );
    }

    private Boolean lerBooleanoAtualizacao(
            Boolean atual) {

        System.out.print(
                "Publicação autorizada ["
                        + atual
                        + "] (S/N/ENTER): "
        );

        String valor =
                scanner.nextLine().trim();

        if (valor.isBlank()) {
            return atual;
        }

        if (valor.equalsIgnoreCase("S")) {
            return true;
        }

        if (valor.equalsIgnoreCase("N")) {
            return false;
        }

        throw new IllegalArgumentException(
                "Digite S, N ou ENTER."
        );
    }

    private Long lerExpedicaoAtualizacao(
            Relatorio existente) {

        Long expedicaoAtual = null;

        if (existente.getExpedicao() != null) {
            expedicaoAtual =
                    existente.getExpedicao().getId();
        }

        System.out.print(
                "ID da expedição ["
                        + expedicaoAtual
                        + "] (ENTER para manter): "
        );

        String valor =
                scanner.nextLine();

        if (valor.isBlank()) {
            return expedicaoAtual;
        }

        return Long.parseLong(valor);
    }

    private Long lerLong(String mensagem) {
        System.out.print(mensagem);
        return Long.parseLong(scanner.nextLine());
    }

    private void exibirRelatorio(
            Relatorio relatorio) {

        System.out.println(
                "\n------------------------------"
        );

        System.out.println(
                "ID: " + relatorio.getId()
        );

        System.out.println(
                "Título: " + relatorio.getTitulo()
        );

        System.out.println(
                "Resumo: " + relatorio.getResumo()
        );

        System.out.println(
                "Data de submissão: "
                        + relatorio.getDataSubmissao()
        );

        System.out.println(
                "Número de páginas: "
                        + relatorio.getNumeroTotalPaginas()
        );

        System.out.println(
                "Situação: "
                        + relatorio.getSituacaoRelatorioFinal()
        );

        System.out.println(
                "Publicação autorizada: "
                        + relatorio.getPublicacaoAutorizada()
        );

        if (relatorio.getArquivoCompleto() != null) {
            System.out.println(
                    "Arquivo: "
                            + relatorio.getArquivoCompleto().length
                            + " bytes"
            );
        } else {
            System.out.println(
                    "Arquivo: não informado"
            );
        }

        if (relatorio.getExpedicao() != null) {
            System.out.println(
                    "ID da expedição: "
                            + relatorio.getExpedicao().getId()
            );
        }

        System.out.println(
                "------------------------------"
        );
    }

    private void pressionarEnter() {
        System.out.println();
        System.out.println(
                "Pressione ENTER para continuar..."
        );
        scanner.nextLine();
    }
}
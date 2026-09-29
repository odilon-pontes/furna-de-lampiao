package com.furnadelampiao.view;

import com.furnadelampiao.domain.Amostra;
import com.furnadelampiao.domain.ColetaCientifica;
import com.furnadelampiao.enums.CategoriaAmostra;
import com.furnadelampiao.enums.CondicaoConservacaoAmostra;
import com.furnadelampiao.enums.UnidadeMedida;
import com.furnadelampiao.service.AmostraService;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class AmostraView {

    private final AmostraService service;
    private final Scanner scanner;

    public AmostraView(AmostraService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== AMOSTRAS ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Buscar por ID");
            System.out.println("3 - Buscar por código de campo");
            System.out.println("4 - Listar todas");
            System.out.println("5 - Buscar por coleta científica");
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
                        buscarPorCodigoCampo();
                        break;
                    case 4:
                        listarTodos();
                        break;
                    case 5:
                        buscarPorColetaCientificaId();
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
        System.out.println("\n=== CADASTRAR AMOSTRA ===");

        Amostra amostra = lerDadosAmostra();

        service.cadastrar(amostra);

        System.out.println("Amostra cadastrada com sucesso.");
        pressionarEnter();
    }

    private void buscarPorId() {
        Long id = lerLong("\nID da amostra: ");

        Amostra amostra = service.buscarPorId(id);

        if (amostra == null) {
            System.out.println("Amostra não encontrada.");
            pressionarEnter();
            return;
        }

        exibirAmostra(amostra);
        pressionarEnter();
    }

    private void buscarPorCodigoCampo() {
        System.out.print("\nCódigo de campo: ");
        String codigo = scanner.nextLine();

        Amostra amostra = service.buscarPorCodigoCampo(codigo);

        if (amostra == null) {
            System.out.println("Amostra não encontrada.");
            pressionarEnter();
            return;
        }

        exibirAmostra(amostra);
        pressionarEnter();
    }

    private void listarTodos() {
        System.out.println("\n=== TODAS AS AMOSTRAS ===");

        List<Amostra> amostras = service.listarTodos();

        if (amostras.isEmpty()) {
            System.out.println("Nenhuma amostra cadastrada.");
            pressionarEnter();
            return;
        }

        amostras.forEach(this::exibirAmostra);

        pressionarEnter();
    }

    private void buscarPorColetaCientificaId() {
        Long id = lerLong("\nID da coleta científica: ");

        List<Amostra> amostras =
                service.buscarPorColetaCientificaId(id);

        if (amostras.isEmpty()) {
            System.out.println(
                    "Nenhuma amostra encontrada para essa coleta.");
            pressionarEnter();
            return;
        }

        amostras.forEach(this::exibirAmostra);

        pressionarEnter();
    }

    private void atualizar() {
        System.out.println("\n=== ATUALIZAR AMOSTRA ===");

        Long id = lerLong("ID da amostra: ");

        Amostra existente = service.buscarPorId(id);

        if (existente == null) {
            System.out.println("Amostra não encontrada.");
            pressionarEnter();
            return;
        }

        exibirAmostra(existente);

        System.out.println("\nInforme os novos dados:");

        Amostra amostra = lerDadosAmostra();
        amostra.setId(id);

        service.atualizar(amostra);

        System.out.println("Amostra atualizada com sucesso.");
        pressionarEnter();
    }

    private void removerPorId() {
        System.out.println("\n=== REMOVER AMOSTRA ===");

        Long id = lerLong("ID da amostra: ");

        Amostra amostra = service.buscarPorId(id);

        if (amostra == null) {
            System.out.println("Amostra não encontrada.");
            pressionarEnter();
            return;
        }

        exibirAmostra(amostra);

        System.out.print("Confirma a remoção? (S/N): ");
        String confirmacao = scanner.nextLine();

        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println("Operação cancelada.");
            pressionarEnter();
            return;
        }

        service.removerPorId(id);

        System.out.println("Amostra removida com sucesso.");
        pressionarEnter();
    }

    private Amostra lerDadosAmostra() {
        System.out.print("Código de campo: ");
        String codCampo = scanner.nextLine();

        CategoriaAmostra categoriaAmostra =
                lerCategoriaAmostra();

        System.out.print("Volume: ");
        BigDecimal volume =
                new BigDecimal(scanner.nextLine().replace(",", "."));

        UnidadeMedida unidadeMedida =
                lerUnidadeMedida();

        System.out.print(
                "Data de acondicionamento (AAAA-MM-DDTHH:MM:SS): ");

        String dataTexto = scanner.nextLine();

        LocalDateTime dataAcondicionamento =
                dataTexto.isBlank()
                        ? null
                        : LocalDateTime.parse(dataTexto);

        CondicaoConservacaoAmostra condicaoAmostra =
                lerCondicaoAmostra();

        System.out.print(
                "Contém material perigoso? (S/N, vazio = não): ");

        String perigosoTexto = scanner.nextLine();

        Boolean indicacaoMaterialPerigoso =
                perigosoTexto.isBlank()
                        ? null
                        : perigosoTexto.equalsIgnoreCase("S");

        byte[] fotografia =
                lerArquivoBinario(
                        "Caminho do arquivo de fotografia");

        System.out.print("Observações (opcional): ");
        String observacoes = scanner.nextLine();

        Long coletaCientificaId =
                lerLong("ID da coleta científica: ");

        return Amostra.builder()
                .codCampo(codCampo)
                .categoriaAmostra(categoriaAmostra)
                .volume(volume)
                .unidadeMedida(unidadeMedida)
                .dataAcondicionamento(dataAcondicionamento)
                .condicaoAmostra(condicaoAmostra)
                .indicacaoMaterialPerigoso(indicacaoMaterialPerigoso)
                .fotografia(fotografia)
                .observacoes(
                        observacoes.isBlank()
                                ? null
                                : observacoes)
                .coletaCientifica(
                        ColetaCientifica.builder()
                                .id(coletaCientificaId)
                                .build())
                .build();
    }

    private CategoriaAmostra lerCategoriaAmostra() {
        CategoriaAmostra[] categorias =
                CategoriaAmostra.values();

        System.out.println("\nCategorias de amostra:");

        for (int i = 0; i < categorias.length; i++) {
            System.out.println(i + " - " + categorias[i]);
        }

        System.out.print("Escolha a categoria: ");

        int indice =
                Integer.parseInt(scanner.nextLine());

        if (indice < 0 || indice >= categorias.length) {
            throw new IllegalArgumentException(
                    "Categoria de amostra inválida.");
        }

        return categorias[indice];
    }

    private UnidadeMedida lerUnidadeMedida() {
        UnidadeMedida[] unidades =
                UnidadeMedida.values();

        System.out.println("\nUnidades de medida:");

        for (int i = 0; i < unidades.length; i++) {
            System.out.println(i + " - " + unidades[i]);
        }

        System.out.print("Escolha a unidade: ");

        int indice =
                Integer.parseInt(scanner.nextLine());

        if (indice < 0 || indice >= unidades.length) {
            throw new IllegalArgumentException(
                    "Unidade de medida inválida.");
        }

        return unidades[indice];
    }

    private CondicaoConservacaoAmostra lerCondicaoAmostra() {
        CondicaoConservacaoAmostra[] condicoes =
                CondicaoConservacaoAmostra.values();

        System.out.println("\nCondições de conservação:");

        for (int i = 0; i < condicoes.length; i++) {
            System.out.println(i + " - " + condicoes[i]);
        }

        System.out.print("Escolha a condição: ");

        int indice =
                Integer.parseInt(scanner.nextLine());

        if (indice < 0 || indice >= condicoes.length) {
            throw new IllegalArgumentException(
                    "Condição de conservação inválida.");
        }

        return condicoes[indice];
    }

    private byte[] lerArquivoBinario(String mensagem) {
        System.out.print(mensagem + " (vazio para pular): ");

        String caminho = scanner.nextLine();

        if (caminho.isBlank()) {
            return null;
        }

        try {
            return Files.readAllBytes(Paths.get(caminho));
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

    private void exibirAmostra(Amostra amostra) {
        System.out.println("\n------------------------------");
        System.out.println("ID: " + amostra.getId());
        System.out.println(
                "Código de campo: " + amostra.getCodCampo());
        System.out.println(
                "Categoria: " + amostra.getCategoriaAmostra());
        System.out.println(
                "Volume: " + amostra.getVolume());
        System.out.println(
                "Unidade de medida: "
                        + amostra.getUnidadeMedida());
        System.out.println(
                "Data de acondicionamento: "
                        + amostra.getDataAcondicionamento());
        System.out.println(
                "Condição: " + amostra.getCondicaoAmostra());
        System.out.println(
                "Material perigoso: "
                        + amostra.getIndicacaoMaterialPerigoso());

        if (amostra.getFotografia() != null) {
            System.out.println(
                    "Fotografia: "
                            + amostra.getFotografia().length
                            + " bytes");
        }

        System.out.println(
                "Observações: " + amostra.getObservacoes());

        if (amostra.getColetaCientifica() != null) {
            System.out.println(
                    "ID da coleta científica: "
                            + amostra
                            .getColetaCientifica()
                            .getId());
        }

        System.out.println("------------------------------");
    }

    private void pressionarEnter() {
        System.out.println();
        System.out.println(
                "Pressione ENTER para continuar...");
        scanner.nextLine();
    }
}
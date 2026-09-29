package com.furnadelampiao.view;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.Localizacao;
import com.furnadelampiao.enums.UnidadeFederativa;
import com.furnadelampiao.service.CavernaService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class CavernaView {

    private final CavernaService service;
    private final Scanner scanner;

    public CavernaView(CavernaService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== CAVERNAS ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Buscar por ID");
            System.out.println("3 - Listar todas");
            System.out.println("4 - Listar por UF");
            System.out.println("5 - Listar com acesso permitido");
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
                        listarTodos();
                        break;
                    case 4:
                        listarPorUf();
                        break;
                    case 5:
                        listarComAcessoPermitido();
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
        System.out.println("\n=== CADASTRAR CAVERNA ===");

        Caverna caverna = lerDadosCaverna();

        service.cadastrar(caverna);

        System.out.println("Caverna cadastrada com sucesso.");

        pressionarEnter();
    }

    private void buscarPorId() {
        System.out.print("\nID da caverna: ");

        Long id = Long.parseLong(scanner.nextLine());

        Caverna caverna = service.buscarPorId(id);

        if (caverna == null) {
            System.out.println("Caverna não encontrada.");
            pressionarEnter();
            return;
        }

        exibirCaverna(caverna);

        pressionarEnter();
    }

    private void listarTodos() {
        System.out.println("\n=== TODAS AS CAVERNAS ===");

        List<Caverna> cavernas = service.listarTodos();

        if (cavernas.isEmpty()) {
            System.out.println("Nenhuma caverna cadastrada.");
            pressionarEnter();
            return;
        }

        cavernas.forEach(this::exibirCaverna);

        pressionarEnter();
    }

    private void listarPorUf() {
        System.out.println("\n=== LISTAR POR UF ===");

        UnidadeFederativa uf = lerUf();

        List<Caverna> cavernas = service.listarPorUf(uf);

        if (cavernas.isEmpty()) {
            System.out.println(
                    "Nenhuma caverna encontrada para a UF informada.");
            pressionarEnter();
            return;
        }

        cavernas.forEach(this::exibirCaverna);

        pressionarEnter();
    }

    private void listarComAcessoPermitido() {
        System.out.println("\n=== CAVERNAS COM ACESSO PERMITIDO ===");

        List<Caverna> cavernas =
                service.listarComAcessoPermitido();

        if (cavernas.isEmpty()) {
            System.out.println(
                    "Nenhuma caverna com acesso permitido.");
            pressionarEnter();
            return;
        }

        cavernas.forEach(this::exibirCaverna);

        pressionarEnter();
    }

    private void atualizar() {
        System.out.println("\n=== ATUALIZAR CAVERNA ===");

        System.out.print("ID da caverna: ");

        Long id = Long.parseLong(scanner.nextLine());

        Caverna caverna = service.buscarPorId(id);

        if (caverna == null) {
            System.out.println("Caverna não encontrada.");
            pressionarEnter();
            return;
        }

        exibirCaverna(caverna);

        System.out.println("\nInforme os novos dados:");

        Caverna dados = lerDadosCaverna();

        dados.setId(id);

        service.atualizar(dados);

        System.out.println("Caverna atualizada com sucesso.");

        pressionarEnter();
    }

    private void removerPorId() {
        System.out.println("\n=== REMOVER CAVERNA ===");

        System.out.print("ID da caverna: ");

        Long id = Long.parseLong(scanner.nextLine());

        Caverna caverna = service.buscarPorId(id);

        if (caverna == null) {
            System.out.println("Caverna não encontrada.");
            pressionarEnter();
            return;
        }

        exibirCaverna(caverna);

        System.out.print("Confirma a remoção? (S/N): ");

        String confirmacao = scanner.nextLine();

        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println("Operação cancelada.");
            pressionarEnter();
            return;
        }

        service.removerPorId(id);

        System.out.println("Caverna removida com sucesso.");

        pressionarEnter();
    }

    private Caverna lerDadosCaverna() {
        System.out.print("Nome oficial: ");
        String nomeOficial = scanner.nextLine();

        System.out.print("Código de cadastro ambiental: ");
        String codCadastroAmbiental = scanner.nextLine();

        System.out.print("Município: ");
        String municipio = scanner.nextLine();

        UnidadeFederativa uf = lerUf();

        System.out.print("Latitude: ");
        BigDecimal latitude =
                new BigDecimal(scanner.nextLine().replace(",", "."));

        System.out.print("Longitude: ");
        BigDecimal longitude =
                new BigDecimal(scanner.nextLine().replace(",", "."));

        System.out.print("Datum geodésico: ");
        String datumGeodesico = scanner.nextLine();

        System.out.print("Altitude: ");
        BigDecimal altitude =
                new BigDecimal(scanner.nextLine().replace(",", "."));

        System.out.print("Extensão: ");
        BigDecimal extensao =
                new BigDecimal(scanner.nextLine().replace(",", "."));

        System.out.print(
                "Data da última inspeção (AAAA-MM-DD): ");

        String dataTexto = scanner.nextLine();

        LocalDate dataUltimaInspecao =
                dataTexto.isBlank()
                        ? null
                        : LocalDate.parse(dataTexto);

        System.out.print(
                "Acesso atualmente permitido? (S/N): ");

        boolean acessoPermitido =
                scanner.nextLine().equalsIgnoreCase("S");

        Localizacao coordenadas =
                Localizacao.builder()
                        .latitude(latitude)
                        .longitude(longitude)
                        .datumGeodesico(datumGeodesico)
                        .build();

        return Caverna.builder()
                .nomeOficial(nomeOficial)
                .codCadastroAmbiental(codCadastroAmbiental)
                .municipio(municipio)
                .uf(uf)
                .coordenadas(coordenadas)
                .altitude(altitude)
                .extensao(extensao)
                .dataUltimaInspecao(dataUltimaInspecao)
                .acessoAtualmentePermitido(acessoPermitido)
                .build();
    }

    private UnidadeFederativa lerUf() {
        System.out.println("UFs disponíveis:");

        UnidadeFederativa[] ufs =
                UnidadeFederativa.values();

        for (int i = 0; i < ufs.length; i++) {
            System.out.println(i + " - " + ufs[i]);
        }

        System.out.print("Escolha a UF: ");

        int indice =
                Integer.parseInt(scanner.nextLine());

        if (indice < 0 || indice >= ufs.length) {
            throw new IllegalArgumentException("UF inválida.");
        }

        return ufs[indice];
    }

    private void exibirCaverna(Caverna caverna) {
        System.out.println("\n------------------------------");

        System.out.println("ID: " + caverna.getId());
        System.out.println(
                "Nome oficial: " + caverna.getNomeOficial());
        System.out.println(
                "Código ambiental: "
                        + caverna.getCodCadastroAmbiental());
        System.out.println(
                "Município: " + caverna.getMunicipio());
        System.out.println("UF: " + caverna.getUf());
        System.out.println(
                "Altitude: " + caverna.getAltitude());
        System.out.println(
                "Extensão: " + caverna.getExtensao());
        System.out.println(
                "Última inspeção: "
                        + caverna.getDataUltimaInspecao());
        System.out.println(
                "Acesso permitido: "
                        + caverna.getAcessoAtualmentePermitido());

        if (caverna.getCoordenadas() != null) {
            System.out.println(
                    "Latitude: "
                            + caverna.getCoordenadas().getLatitude());
            System.out.println(
                    "Longitude: "
                            + caverna.getCoordenadas().getLongitude());
            System.out.println(
                    "Datum geodésico: "
                            + caverna.getCoordenadas()
                            .getDatumGeodesico());
        }

        System.out.println("------------------------------");
    }

    private void pressionarEnter() {
        System.out.println();
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
}
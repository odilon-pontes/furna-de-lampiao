package com.furnadelampiao.view;

import com.furnadelampiao.domain.Localizacao;
import com.furnadelampiao.domain.PlanoSeguranca;
import com.furnadelampiao.service.PlanoSegurancaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class PlanoSegurancaView {

    private final PlanoSegurancaService service;
    private final Scanner scanner;

    public PlanoSegurancaView(
            PlanoSegurancaService service,
            Scanner scanner) {

        this.service = service;
        this.scanner = scanner;
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== PLANOS DE SEGURANÇA ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Buscar por ID");
            System.out.println("3 - Buscar mapa da rota");
            System.out.println("4 - Listar todos");
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
                        buscarMapaRota();
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
        System.out.println("\n=== CADASTRAR PLANO DE SEGURANÇA ===");

        PlanoSeguranca plano = lerDadosPlano();

        service.cadastrar(plano);

        System.out.println(
                "Plano de segurança cadastrado com sucesso."
        );

        pressionarEnter();
    }

    private void buscarPorId() {
        System.out.println("\n=== BUSCAR PLANO DE SEGURANÇA ===");

        Long id = lerLong("ID: ");

        PlanoSeguranca plano = service.buscarPorId(id);

        if (plano == null) {
            System.out.println(
                    "Plano de segurança não encontrado."
            );
            pressionarEnter();
            return;
        }

        exibirPlano(plano);
        pressionarEnter();
    }

    private void buscarMapaRota() {
        System.out.println("\n=== MAPA DA ROTA ===");

        Long id = lerLong("ID do plano: ");

        byte[] mapa = service.buscarMapaRota(id);

        if (mapa == null || mapa.length == 0) {
            System.out.println(
                    "Nenhum mapa de rota encontrado."
            );
            pressionarEnter();
            return;
        }

        System.out.println("Mapa de rota encontrado.");
        System.out.println(
                "Tamanho do arquivo: "
                        + mapa.length
                        + " bytes."
        );

        pressionarEnter();
    }

    private void listarTodos() {
        System.out.println(
                "\n=== TODOS OS PLANOS DE SEGURANÇA ==="
        );

        List<PlanoSeguranca> planos =
                service.listarTodos();

        if (planos.isEmpty()) {
            System.out.println(
                    "Nenhum plano de segurança encontrado."
            );
            pressionarEnter();
            return;
        }

        planos.forEach(this::exibirPlano);

        pressionarEnter();
    }

    private void atualizar() {
        System.out.println(
                "\n=== ATUALIZAR PLANO DE SEGURANÇA ==="
        );

        Long id = lerLong("ID do plano: ");

        PlanoSeguranca existente =
                service.buscarPorId(id);

        if (existente == null) {
            System.out.println(
                    "Plano de segurança não encontrado."
            );
            pressionarEnter();
            return;
        }

        exibirPlano(existente);

        System.out.print(
                "Confirma a atualização? (S/N): "
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

        System.out.println(
                "\nInforme os novos dados:"
        );

        PlanoSeguranca plano =
                lerDadosPlano();

        plano.setId(id);

        service.atualizar(plano);

        System.out.println(
                "Plano de segurança atualizado com sucesso."
        );

        pressionarEnter();
    }

    private void removerPorId() {
        System.out.println(
                "\n=== REMOVER PLANO DE SEGURANÇA ==="
        );

        Long id = lerLong("ID do plano: ");

        PlanoSeguranca existente =
                service.buscarPorId(id);

        if (existente == null) {
            System.out.println(
                    "Plano de segurança não encontrado."
            );
            pressionarEnter();
            return;
        }

        exibirPlano(existente);

        System.out.print(
                "Confirma a remoção? (S/N): "
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

        service.removerPorId(id);

        System.out.println(
                "Plano de segurança removido com sucesso."
        );

        pressionarEnter();
    }

    private PlanoSeguranca lerDadosPlano() {
        System.out.print(
                "Tempo máximo sem comunicação (minutos): "
        );

        Integer tempoMaxSemComunicacao =
                Integer.parseInt(scanner.nextLine());

        if (tempoMaxSemComunicacao <= 0) {
            throw new IllegalArgumentException(
                    "Tempo máximo sem comunicação deve ser maior que zero."
            );
        }

        System.out.print(
                "Telefone de emergência: "
        );

        String telefoneEmergencia =
                scanner.nextLine();

        if (telefoneEmergencia.isBlank()) {
            throw new IllegalArgumentException(
                    "Telefone de emergência é obrigatório."
            );
        }

        Localizacao pontoExternoEncontro =
                lerLocalizacao();

        Boolean necessidadeEquipeMedica =
                lerBooleano(
                        "Necessidade de equipe médica? (S/N): "
                );

        return PlanoSeguranca.builder()
                .tempoMaxSemComunicacao(
                        tempoMaxSemComunicacao
                )
                .telefoneEmergencia(
                        telefoneEmergencia
                )
                .pontoExternoEncontro(
                        pontoExternoEncontro
                )
                .necessidadeEquipeMedica(
                        necessidadeEquipeMedica
                )
                .build();
    }

    private Localizacao lerLocalizacao() {
        System.out.println(
                "-- Ponto externo de encontro --"
        );

        System.out.print("Latitude: ");

        BigDecimal latitude =
                lerBigDecimal(scanner.nextLine());

        System.out.print("Longitude: ");

        BigDecimal longitude =
                lerBigDecimal(scanner.nextLine());

        System.out.print("Datum geodésico: ");

        String datumGeodesico =
                scanner.nextLine();

        if (datumGeodesico.isBlank()) {
            throw new IllegalArgumentException(
                    "Datum geodésico é obrigatório."
            );
        }

        return Localizacao.builder()
                .latitude(latitude)
                .longitude(longitude)
                .datumGeodesico(datumGeodesico)
                .build();
    }

    private Boolean lerBooleano(String mensagem) {
        System.out.print(mensagem);

        String valor =
                scanner.nextLine()
                        .trim();

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

    private BigDecimal lerBigDecimal(String valor) {
        try {
            return new BigDecimal(
                    valor.replace(",", ".")
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Digite um valor numérico válido."
            );
        }
    }

    private Long lerLong(String mensagem) {
        System.out.print(mensagem);
        return Long.parseLong(scanner.nextLine());
    }

    private void exibirPlano(PlanoSeguranca plano) {
        System.out.println(
                "\n-----------------------------"
        );

        System.out.println(
                "ID: " + plano.getId()
        );

        System.out.println(
                "Tempo máximo sem comunicação: "
                        + plano.getTempoMaxSemComunicacao()
        );

        System.out.println(
                "Telefone de emergência: "
                        + plano.getTelefoneEmergencia()
        );

        System.out.println(
                "Ponto externo de encontro: "
                        + plano.getPontoExternoEncontro()
        );

        System.out.println(
                "Necessidade de equipe médica: "
                        + plano.getNecessidadeEquipeMedica()
        );

        System.out.println(
                "-----------------------------"
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
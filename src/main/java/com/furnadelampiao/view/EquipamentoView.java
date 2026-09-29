
        package com.furnadelampiao.view;

import com.furnadelampiao.domain.Equipamento;
import com.furnadelampiao.enums.SituacaoOperacional;
import com.furnadelampiao.enums.TipoEquipamento;
import com.furnadelampiao.service.EquipamentoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class EquipamentoView {

    private final EquipamentoService service;
    private final Scanner scanner;

    public EquipamentoView(EquipamentoService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== EQUIPAMENTOS ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Buscar por ID");
            System.out.println("3 - Listar todos");
            System.out.println("4 - Listar por tipo");
            System.out.println("5 - Listar por situação operacional");
            System.out.println("6 - Listar disponíveis entre datas");
            System.out.println("7 - Atualizar");
            System.out.println("8 - Remover");
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
                        listarPorTipo();
                        break;
                    case 5:
                        listarPorSituacao();
                        break;
                    case 6:
                        listarDisponiveisEntre();
                        break;
                    case 7:
                        atualizar();
                        break;
                    case 8:
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
                opcao = -1;
            }

        } while (opcao != 0);
    }

    private void cadastrar() {
        System.out.println("\n=== CADASTRAR EQUIPAMENTO ===");

        Equipamento equipamento = lerDadosEquipamento();

        service.cadastrar(equipamento);

        System.out.println("Equipamento cadastrado com sucesso.");
    }

    private void buscarPorId() {
        System.out.println("\n=== BUSCAR EQUIPAMENTO ===");

        Long id = lerLong("ID: ");

        Equipamento equipamento = service.buscarPorId(id);

        if (equipamento == null) {
            System.out.println("Equipamento não encontrado.");
            return;
        }

        exibirEquipamento(equipamento);
    }

    private void listarTodos() {
        System.out.println("\n=== TODOS OS EQUIPAMENTOS ===");

        List<Equipamento> equipamentos = service.listarTodos();

        if (equipamentos.isEmpty()) {
            System.out.println("Nenhum equipamento encontrado.");
            return;
        }

        for (Equipamento equipamento : equipamentos) {
            exibirEquipamento(equipamento);
        }
    }

    private void listarPorTipo() {
        System.out.println("\n=== LISTAR POR TIPO ===");

        TipoEquipamento tipo = lerTipoEquipamento();

        List<Equipamento> equipamentos = service.listarPorTipo(tipo);

        if (equipamentos.isEmpty()) {
            System.out.println("Nenhum equipamento encontrado.");
            return;
        }

        for (Equipamento equipamento : equipamentos) {
            exibirEquipamento(equipamento);
        }
    }

    private void listarPorSituacao() {
        System.out.println("\n=== LISTAR POR SITUAÇÃO OPERACIONAL ===");

        SituacaoOperacional situacao = lerSituacaoOperacional();

        List<Equipamento> equipamentos =
                service.listarPorSituacaoOperacional(situacao);

        if (equipamentos.isEmpty()) {
            System.out.println("Nenhum equipamento encontrado.");
            return;
        }

        for (Equipamento equipamento : equipamentos) {
            exibirEquipamento(equipamento);
        }
    }

    private void listarDisponiveisEntre() {
        System.out.println("\n=== EQUIPAMENTOS DISPONÍVEIS ENTRE DATAS ===");

        LocalDate inicio = lerData("Data inicial (yyyy-MM-dd): ");
        LocalDate fim = lerData("Data final (yyyy-MM-dd): ");

        List<Equipamento> equipamentos =
                service.listarDisponiveisEntre(inicio, fim);

        if (equipamentos.isEmpty()) {
            System.out.println("Nenhum equipamento disponível encontrado.");
            return;
        }

        for (Equipamento equipamento : equipamentos) {
            exibirEquipamento(equipamento);
        }
    }

    private void atualizar() {
        System.out.println("\n=== ATUALIZAR EQUIPAMENTO ===");

        Long id = lerLong("ID do equipamento: ");

        Equipamento equipamento = service.buscarPorId(id);

        if (equipamento == null) {
            System.out.println("Equipamento não encontrado.");
            return;
        }

        System.out.println("Informe os novos dados:");

        Equipamento dados = lerDadosEquipamento();
        dados.setId(id);

        service.atualizar(dados);

        System.out.println("Equipamento atualizado com sucesso.");
    }

    private void removerPorId() {
        System.out.println("\n=== REMOVER EQUIPAMENTO ===");

        Long id = lerLong("ID do equipamento: ");

        Equipamento equipamento = service.buscarPorId(id);

        if (equipamento == null) {
            System.out.println("Equipamento não encontrado.");
            return;
        }

        service.removerPorId(id);

        System.out.println("Equipamento removido com sucesso.");
    }

    private Equipamento lerDadosEquipamento() {
        System.out.print("Código patrimonial: ");
        String codPatrimonial = scanner.nextLine();

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        TipoEquipamento tipo = lerTipoEquipamento();

        System.out.print("Fabricante: ");
        String fabricante = scanner.nextLine();

        BigDecimal valorAquisicao = lerBigDecimalOpcional(
                "Valor de aquisição: ");

        LocalDate dataCompra = lerData(
                "Data de compra (yyyy-MM-dd): ");

        LocalDate dataUltimaManutencao = lerDataOpcional(
                "Data da última manutenção (yyyy-MM-dd): ");

        SituacaoOperacional situacaoOperacional =
                lerSituacaoOperacional();

        Boolean indicacaoCalibracao =
                lerBooleano("Indicação de calibração? (s/n): ");

        return Equipamento.builder()
                .codPatrimonial(codPatrimonial)
                .nome(nome)
                .tipo(tipo)
                .fabricante(fabricante)
                .valorAquisicao(valorAquisicao)
                .dataCompra(dataCompra)
                .dataUltimaManutencao(dataUltimaManutencao)
                .situacaoOperacional(situacaoOperacional)
                .indicacaoCalibracao(indicacaoCalibracao)
                .build();
    }

    private TipoEquipamento lerTipoEquipamento() {
        TipoEquipamento[] valores = TipoEquipamento.values();

        System.out.println("\nTipos de equipamento:");

        for (int i = 0; i < valores.length; i++) {
            System.out.println((i + 1) + " - " + valores[i]);
        }

        int opcao = lerInt("Escolha o tipo: ");

        if (opcao < 1 || opcao > valores.length) {
            throw new IllegalArgumentException("Tipo inválido.");
        }

        return valores[opcao - 1];
    }

    private SituacaoOperacional lerSituacaoOperacional() {
        SituacaoOperacional[] valores =
                SituacaoOperacional.values();

        System.out.println("\nSituações operacionais:");

        for (int i = 0; i < valores.length; i++) {
            System.out.println((i + 1) + " - " + valores[i]);
        }

        int opcao = lerInt("Escolha a situação: ");

        if (opcao < 1 || opcao > valores.length) {
            throw new IllegalArgumentException("Situação inválida.");
        }

        return valores[opcao - 1];
    }

    private LocalDate lerData(String mensagem) {
        System.out.print(mensagem);
        return LocalDate.parse(scanner.nextLine());
    }

    private LocalDate lerDataOpcional(String mensagem) {
        System.out.print(mensagem);

        String valor = scanner.nextLine();

        if (valor.isBlank()) {
            return null;
        }

        return LocalDate.parse(valor);
    }

    private BigDecimal lerBigDecimalOpcional(String mensagem) {
        System.out.print(mensagem);

        String valor = scanner.nextLine();

        if (valor.isBlank()) {
            return null;
        }

        return new BigDecimal(valor.replace(",", "."));
    }

    private Boolean lerBooleano(String mensagem) {
        System.out.print(mensagem);

        String valor = scanner.nextLine().trim().toLowerCase();

        if (valor.equals("s")) {
            return true;
        }

        if (valor.equals("n")) {
            return false;
        }

        throw new IllegalArgumentException(
                "Digite apenas 's' ou 'n'.");
    }

    private int lerInt(String mensagem) {
        System.out.print(mensagem);
        return Integer.parseInt(scanner.nextLine());
    }

    private Long lerLong(String mensagem) {
        System.out.print(mensagem);
        return Long.parseLong(scanner.nextLine());
    }

    private void exibirEquipamento(Equipamento equipamento) {
        System.out.println("\n-----------------------------");
        System.out.println("ID: " + equipamento.getId());
        System.out.println("Código patrimonial: "
                + equipamento.getCodPatrimonial());
        System.out.println("Nome: " + equipamento.getNome());
        System.out.println("Tipo: " + equipamento.getTipo());
        System.out.println("Fabricante: "
                + equipamento.getFabricante());
        System.out.println("Valor de aquisição: "
                + equipamento.getValorAquisicao());
        System.out.println("Data de compra: "
                + equipamento.getDataCompra());
        System.out.println("Última manutenção: "
                + equipamento.getDataUltimaManutencao());
        System.out.println("Situação operacional: "
                + equipamento.getSituacaoOperacional());
        System.out.println("Indicação de calibração: "
                + equipamento.getIndicacaoCalibracao());
        System.out.println("-----------------------------");
    }
}


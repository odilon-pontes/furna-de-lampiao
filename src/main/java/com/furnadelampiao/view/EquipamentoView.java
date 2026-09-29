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
            System.out.println("6 - Listar disponíveis em um período");
            System.out.println("7 - Atualizar");
            System.out.println("8 - Remover");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");

            opcao = Integer.parseInt(scanner.nextLine());

            try {
                switch (opcao) {
                    case 1:
                        cadastrar();
                        pressionarEnter();
                        break;
                    case 2:
                        buscarPorId();
                        pressionarEnter();
                        break;
                    case 3:
                        listarTodos();
                        pressionarEnter();
                        break;
                    case 4:
                        listarPorTipo();
                        pressionarEnter();
                        break;
                    case 5:
                        listarPorSituacaoOperacional();
                        pressionarEnter();
                        break;
                    case 6:
                        listarDisponiveisEntre();
                        pressionarEnter();
                        break;
                    case 7:
                        atualizar();
                        pressionarEnter();
                        break;
                    case 8:
                        removerPorId();
                        pressionarEnter();
                        break;
                    case 0:
                        System.out.println("Voltando...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                        pressionarEnter();
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
                pressionarEnter();
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
        Long id = lerLong("\nID do equipamento: ");

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
            System.out.println("Nenhum equipamento cadastrado.");
            return;
        }

        equipamentos.forEach(this::exibirEquipamento);
    }

    private void listarPorTipo() {
        System.out.println("\n=== EQUIPAMENTOS POR TIPO ===");

        TipoEquipamento tipo = lerTipoEquipamento();

        List<Equipamento> equipamentos = service.listarPorTipo(tipo);

        if (equipamentos.isEmpty()) {
            System.out.println("Nenhum equipamento encontrado para esse tipo.");
            return;
        }

        equipamentos.forEach(this::exibirEquipamento);
    }

    private void listarPorSituacaoOperacional() {
        System.out.println("\n=== EQUIPAMENTOS POR SITUAÇÃO ===");

        SituacaoOperacional situacao = lerSituacaoOperacional();

        List<Equipamento> equipamentos =
                service.listarPorSituacaoOperacional(situacao);

        if (equipamentos.isEmpty()) {
            System.out.println("Nenhum equipamento encontrado para essa situação.");
            return;
        }

        equipamentos.forEach(this::exibirEquipamento);
    }

    private void listarDisponiveisEntre() {
        System.out.println("\n=== EQUIPAMENTOS DISPONÍVEIS EM PERÍODO ===");

        System.out.print("Data inicial (AAAA-MM-DD): ");
        LocalDate inicio = LocalDate.parse(scanner.nextLine());

        System.out.print("Data final (AAAA-MM-DD): ");
        LocalDate fim = LocalDate.parse(scanner.nextLine());

        List<Equipamento> equipamentos =
                service.listarDisponiveisEntre(inicio, fim);

        if (equipamentos.isEmpty()) {
            System.out.println("Nenhum equipamento disponível nesse período.");
            return;
        }

        equipamentos.forEach(this::exibirEquipamento);
    }

    private void atualizar() {
        System.out.println("\n=== ATUALIZAR EQUIPAMENTO ===");

        Long id = lerLong("ID do equipamento: ");

        Equipamento existente = service.buscarPorId(id);

        if (existente == null) {
            System.out.println("Equipamento não encontrado.");
            return;
        }

        exibirEquipamento(existente);

        System.out.println("\nInforme os novos dados:");

        Equipamento equipamento = lerDadosEquipamento();
        equipamento.setId(id);

        service.atualizar(equipamento);

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

        exibirEquipamento(equipamento);

        System.out.print("Confirma a remoção? (S/N): ");
        String confirmacao = scanner.nextLine();

        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println("Operação cancelada.");
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

        System.out.print("Fabricante (opcional): ");
        String fabricante = scanner.nextLine();

        System.out.print("Valor de aquisição (vazio = não informado): ");
        String valorTexto = scanner.nextLine();
        BigDecimal valorAquisicao =
                valorTexto.isBlank() ? null : new BigDecimal(valorTexto);

        System.out.print("Data de compra (AAAA-MM-DD): ");
        String dataCompraTexto = scanner.nextLine();
        LocalDate dataCompra =
                dataCompraTexto.isBlank() ? null : LocalDate.parse(dataCompraTexto);

        System.out.print("Data da última manutenção (AAAA-MM-DD, opcional): ");
        String dataManutencaoTexto = scanner.nextLine();
        LocalDate dataUltimaManutencao =
                dataManutencaoTexto.isBlank()
                        ? null
                        : LocalDate.parse(dataManutencaoTexto);

        SituacaoOperacional situacaoOperacional = null;

        System.out.print("Deseja informar a situação operacional agora? (S/N): ");

        if (scanner.nextLine().equalsIgnoreCase("S")) {
            situacaoOperacional = lerSituacaoOperacional();
        }

        System.out.print("Exige calibração? (S/N, vazio = não): ");
        String calibracaoTexto = scanner.nextLine();

        Boolean indicacaoCalibracao =
                calibracaoTexto.isBlank()
                        ? null
                        : calibracaoTexto.equalsIgnoreCase("S");

        return Equipamento.builder()
                .codPatrimonial(codPatrimonial)
                .nome(nome)
                .tipo(tipo)
                .fabricante(fabricante.isBlank() ? null : fabricante)
                .valorAquisicao(valorAquisicao)
                .dataCompra(dataCompra)
                .dataUltimaManutencao(dataUltimaManutencao)
                .situacaoOperacional(situacaoOperacional)
                .indicacaoCalibracao(indicacaoCalibracao)
                .build();
    }

    private TipoEquipamento lerTipoEquipamento() {
        TipoEquipamento[] tipos = TipoEquipamento.values();

        System.out.println("Tipos de equipamento:");

        for (int i = 0; i < tipos.length; i++) {
            System.out.println(i + " - " + tipos[i]);
        }

        System.out.print("Escolha o tipo: ");
        int indice = Integer.parseInt(scanner.nextLine());

        if (indice < 0 || indice >= tipos.length) {
            throw new IllegalArgumentException("Tipo de equipamento inválido.");
        }

        return tipos[indice];
    }

    private SituacaoOperacional lerSituacaoOperacional() {
        SituacaoOperacional[] situacoes =
                SituacaoOperacional.values();

        System.out.println("Situações operacionais:");

        for (int i = 0; i < situacoes.length; i++) {
            System.out.println(i + " - " + situacoes[i]);
        }

        System.out.print("Escolha a situação: ");
        int indice = Integer.parseInt(scanner.nextLine());

        if (indice < 0 || indice >= situacoes.length) {
            throw new IllegalArgumentException(
                    "Situação operacional inválida.");
        }

        return situacoes[indice];
    }

    private Long lerLong(String mensagem) {
        System.out.print(mensagem);
        return Long.parseLong(scanner.nextLine());
    }

    private void exibirEquipamento(Equipamento equipamento) {
        System.out.println("\n------------------------------");
        System.out.println("ID: " + equipamento.getId());
        System.out.println("Código patrimonial: " + equipamento.getCodPatrimonial());
        System.out.println("Nome: " + equipamento.getNome());
        System.out.println("Tipo: " + equipamento.getTipo());
        System.out.println("Fabricante: " + equipamento.getFabricante());
        System.out.println("Valor de aquisição: " + equipamento.getValorAquisicao());
        System.out.println("Data de compra: " + equipamento.getDataCompra());
        System.out.println("Última manutenção: " + equipamento.getDataUltimaManutencao());
        System.out.println("Situação operacional: " + equipamento.getSituacaoOperacional());
        System.out.println("Exige calibração: " + equipamento.getIndicacaoCalibracao());
        System.out.println("------------------------------");
    }

    private void pressionarEnter() {
        System.out.println();
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
}
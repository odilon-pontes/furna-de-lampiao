package com.furnadelampiao.view;

import com.furnadelampiao.domain.Equipamento;
import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.domain.Movimentacao;
import com.furnadelampiao.domain.Pessoa;
import com.furnadelampiao.enums.EstadoSaida;
import com.furnadelampiao.service.MovimentacaoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MovimentacaoView {

    private static final DateTimeFormatter FORMATO_DATA_HORA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final MovimentacaoService service;
    private final Scanner scanner;

    public MovimentacaoView(
            MovimentacaoService service,
            Scanner scanner) {

        this.service = service;
        this.scanner = scanner;
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== MOVIMENTAÇÕES DE EQUIPAMENTO ===");
            System.out.println("1 - Cadastrar (registrar retirada)");
            System.out.println("2 - Buscar por ID");
            System.out.println("3 - Listar todas");
            System.out.println("4 - Buscar por pessoa");
            System.out.println("5 - Buscar por expedição");
            System.out.println("6 - Buscar por equipamento");
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
                        buscarPorPessoaId();
                        break;

                    case 5:
                        buscarPorExpedicaoId();
                        break;

                    case 6:
                        buscarPorEquipamentoId();
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
        System.out.println("\n=== REGISTRAR RETIRADA DE EQUIPAMENTO ===");

        Movimentacao movimentacao = lerDadosMovimentacao();

        service.cadastrar(movimentacao);

        System.out.println("Movimentação registrada com sucesso.");
        pressionarEnter();
    }

    private void buscarPorId() {
        Long id = lerLong("\nID da movimentação: ");

        Movimentacao movimentacao = service.buscarPorId(id);

        if (movimentacao == null) {
            System.out.println("Movimentação não encontrada.");
            pressionarEnter();
            return;
        }

        exibirMovimentacao(movimentacao);
        pressionarEnter();
    }

    private void listarTodos() {
        System.out.println("\n=== TODAS AS MOVIMENTAÇÕES ===");

        List<Movimentacao> movimentacoes =
                service.listarTodos();

        if (movimentacoes.isEmpty()) {
            System.out.println("Nenhuma movimentação cadastrada.");
            pressionarEnter();
            return;
        }

        movimentacoes.forEach(this::exibirMovimentacao);
        pressionarEnter();
    }

    private void buscarPorPessoaId() {
        Long id = lerLong("\nID da pessoa: ");

        List<Movimentacao> movimentacoes =
                service.buscarPorPessoaId(id);

        if (movimentacoes.isEmpty()) {
            System.out.println(
                    "Nenhuma movimentação encontrada para essa pessoa."
            );
            pressionarEnter();
            return;
        }

        movimentacoes.forEach(this::exibirMovimentacao);
        pressionarEnter();
    }

    private void buscarPorExpedicaoId() {
        Long id = lerLong("\nID da expedição: ");

        List<Movimentacao> movimentacoes =
                service.buscarPorExpedicaoId(id);

        if (movimentacoes.isEmpty()) {
            System.out.println(
                    "Nenhuma movimentação encontrada para essa expedição."
            );
            pressionarEnter();
            return;
        }

        movimentacoes.forEach(this::exibirMovimentacao);
        pressionarEnter();
    }

    private void buscarPorEquipamentoId() {
        Long id = lerLong("\nID do equipamento: ");

        List<Movimentacao> movimentacoes =
                service.buscarPorEquipamentoId(id);

        if (movimentacoes.isEmpty()) {
            System.out.println(
                    "Nenhuma movimentação encontrada para esse equipamento."
            );
            pressionarEnter();
            return;
        }

        movimentacoes.forEach(this::exibirMovimentacao);
        pressionarEnter();
    }

    private Movimentacao lerDadosMovimentacao() {
        LocalDateTime dataHoraRetirada =
                lerDataHora(
                        "Data/hora da retirada (AAAA-MM-DD HH:mm): "
                );

        System.out.print(
                "Data prevista de devolução (AAAA-MM-DD): "
        );

        LocalDate dataPrevisaoDevolucao =
                LocalDate.parse(scanner.nextLine());

        if (dataPrevisaoDevolucao.isBefore(
                dataHoraRetirada.toLocalDate())) {

            throw new IllegalArgumentException(
                    "Data prevista de devolução não pode ser anterior à data de retirada."
            );
        }

        EstadoSaida estadoSaida =
                lerEstadoEquipamento(
                        "Estado de saída do equipamento:"
                );

        System.out.print(
                "Custo de avaria (opcional, vazio = nenhum): "
        );

        String custoTexto = scanner.nextLine();

        BigDecimal custoAvaria =
                custoTexto.isBlank()
                        ? null
                        : new BigDecimal(
                        custoTexto.replace(",", ".")
                );

        if (custoAvaria != null &&
                custoAvaria.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Custo de avaria não pode ser negativo."
            );
        }

        Long expedicaoId =
                lerLong("ID da expedição: ");

        Long equipamentoId =
                lerLong("ID do equipamento: ");

        Long pessoaId =
                lerLong(
                        "ID da pessoa responsável pela retirada: "
                );

        return Movimentacao.builder()
                .dataHoraRetirada(dataHoraRetirada)
                .dataPrevisaoDevolucao(dataPrevisaoDevolucao)
                .estadoSaida(estadoSaida)
                .custoAvaria(custoAvaria)
                .expedicao(
                        Expedicao.builder()
                                .id(expedicaoId)
                                .build()
                )
                .equipamento(
                        Equipamento.builder()
                                .id(equipamentoId)
                                .build()
                )
                .pessoa(
                        Pessoa.builder()
                                .id(pessoaId)
                                .build()
                )
                .build();
    }

    private EstadoSaida lerEstadoEquipamento(String titulo) {
        EstadoSaida[] estados = EstadoSaida.values();

        System.out.println(titulo);

        for (int i = 0; i < estados.length; i++) {
            System.out.println(i + " - " + estados[i]);
        }

        System.out.print("Escolha o estado: ");

        int indice =
                Integer.parseInt(scanner.nextLine());

        if (indice < 0 || indice >= estados.length) {
            throw new IllegalArgumentException(
                    "Estado do equipamento inválido."
            );
        }

        return estados[indice];
    }

    private LocalDateTime lerDataHora(String mensagem) {
        System.out.print(mensagem);

        return LocalDateTime.parse(
                scanner.nextLine(),
                FORMATO_DATA_HORA
        );
    }

    private Long lerLong(String mensagem) {
        System.out.print(mensagem);
        return Long.parseLong(scanner.nextLine());
    }

    private void exibirMovimentacao(
            Movimentacao movimentacao) {

        System.out.println("\n------------------------------");
        System.out.println("ID: " + movimentacao.getId());
        System.out.println(
                "Data/hora de retirada: "
                        + movimentacao.getDataHoraRetirada()
        );
        System.out.println(
                "Previsão de devolução: "
                        + movimentacao.getDataPrevisaoDevolucao()
        );
        System.out.println(
                "Data de devolução: "
                        + movimentacao.getDataDevolucao()
        );
        System.out.println(
                "Estado de saída: "
                        + movimentacao.getEstadoSaida()
        );
        System.out.println(
                "Custo de avaria: "
                        + movimentacao.getCustoAvaria()
        );

        if (movimentacao.getExpedicao() != null) {
            System.out.println(
                    "ID da expedição: "
                            + movimentacao.getExpedicao().getId()
            );
        }

        if (movimentacao.getEquipamento() != null) {
            System.out.println(
                    "ID do equipamento: "
                            + movimentacao.getEquipamento().getId()
            );
        }

        if (movimentacao.getPessoa() != null) {
            System.out.println(
                    "ID da pessoa responsável: "
                            + movimentacao.getPessoa().getId()
            );
        }

        System.out.println("------------------------------");
    }

    private void pressionarEnter() {
        System.out.println();
        System.out.println(
                "Pressione ENTER para continuar..."
        );
        scanner.nextLine();
    }
}
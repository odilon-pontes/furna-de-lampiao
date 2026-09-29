package com.furnadelampiao.view;

import com.furnadelampiao.domain.ColetaCientifica;
import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.domain.Pesquisador;
import com.furnadelampiao.domain.Setor;
import com.furnadelampiao.enums.MetodoEmpregado;
import com.furnadelampiao.enums.SituacaoValidacaoColeta;
import com.furnadelampiao.service.ColetaCientificaService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ColetaCientificaView {

    private static final DateTimeFormatter FORMATO_DATA_HORA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ColetaCientificaService service;
    private final Scanner scanner;

    public ColetaCientificaView(
            ColetaCientificaService service,
            Scanner scanner) {

        this.service = service;
        this.scanner = scanner;
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== COLETAS CIENTÍFICAS ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar todas");
            System.out.println("3 - Buscar por expedição");
            System.out.println("4 - Buscar por setor");
            System.out.println("5 - Buscar por pesquisador");
            System.out.println("6 - Buscar por período");
            System.out.println("7 - Buscar por método empregado");
            System.out.println("8 - Buscar por situação de validação");
            System.out.println("9 - Atualizar");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        cadastrar();
                        break;
                    case 2:
                        listarTodos();
                        break;
                    case 3:
                        buscarPorExpedicaoId();
                        break;
                    case 4:
                        buscarPorSetorId();
                        break;
                    case 5:
                        buscarPorPesquisadorId();
                        break;
                    case 6:
                        buscarPorPeriodo();
                        break;
                    case 7:
                        buscarPorMetodoEmpregado();
                        break;
                    case 8:
                        buscarPorSituacaoValidacao();
                        break;
                    case 9:
                        atualizar();
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
        System.out.println("\n=== CADASTRAR COLETA CIENTÍFICA ===");

        ColetaCientifica coleta = lerDadosColeta();

        service.cadastrar(coleta);

        System.out.println(
                "Coleta científica cadastrada com sucesso.");

        pressionarEnter();
    }

    private void listarTodos() {
        System.out.println("\n=== TODAS AS COLETAS ===");

        List<ColetaCientifica> coletas =
                service.listarTodos();

        if (coletas.isEmpty()) {
            System.out.println("Nenhuma coleta cadastrada.");
            pressionarEnter();
            return;
        }

        coletas.forEach(this::exibirColeta);

        pressionarEnter();
    }

    private void buscarPorExpedicaoId() {
        Long id = lerLong("\nID da expedição: ");

        List<ColetaCientifica> coletas =
                service.buscarPorExpedicaoId(id);

        if (coletas.isEmpty()) {
            System.out.println(
                    "Nenhuma coleta encontrada para essa expedição.");
            pressionarEnter();
            return;
        }

        coletas.forEach(this::exibirColeta);

        pressionarEnter();
    }

    private void buscarPorSetorId() {
        Long id = lerLong("\nID do setor: ");

        List<ColetaCientifica> coletas =
                service.buscarPorSetorId(id);

        if (coletas.isEmpty()) {
            System.out.println(
                    "Nenhuma coleta encontrada para esse setor.");
            pressionarEnter();
            return;
        }

        coletas.forEach(this::exibirColeta);

        pressionarEnter();
    }

    private void buscarPorPesquisadorId() {
        Long id = lerLong("\nID do pesquisador: ");

        List<ColetaCientifica> coletas =
                service.buscarPorPesquisadorId(id);

        if (coletas.isEmpty()) {
            System.out.println(
                    "Nenhuma coleta encontrada para esse pesquisador.");
            pressionarEnter();
            return;
        }

        coletas.forEach(this::exibirColeta);

        pressionarEnter();
    }

    private void buscarPorPeriodo() {
        System.out.println("\n=== COLETAS POR PERÍODO ===");

        LocalDateTime inicio =
                lerDataHora(
                        "Data/hora inicial (AAAA-MM-DD HH:mm): ");

        LocalDateTime fim =
                lerDataHora(
                        "Data/hora final (AAAA-MM-DD HH:mm): ");

        List<ColetaCientifica> coletas =
                service.buscarPorPeriodo(inicio, fim);

        if (coletas.isEmpty()) {
            System.out.println(
                    "Nenhuma coleta encontrada nesse período.");
            pressionarEnter();
            return;
        }

        coletas.forEach(this::exibirColeta);

        pressionarEnter();
    }

    private void buscarPorMetodoEmpregado() {
        MetodoEmpregado metodo =
                lerMetodoEmpregado();

        List<ColetaCientifica> coletas =
                service.buscarPorMetodoEmpregado(metodo);

        if (coletas.isEmpty()) {
            System.out.println(
                    "Nenhuma coleta encontrada para esse método.");
            pressionarEnter();
            return;
        }

        coletas.forEach(this::exibirColeta);

        pressionarEnter();
    }

    private void buscarPorSituacaoValidacao() {
        SituacaoValidacaoColeta situacao =
                lerSituacaoValidacao();

        List<ColetaCientifica> coletas =
                service.buscarPorSituacaoValidacao(situacao);

        if (coletas.isEmpty()) {
            System.out.println(
                    "Nenhuma coleta encontrada para essa situação.");
            pressionarEnter();
            return;
        }

        coletas.forEach(this::exibirColeta);

        pressionarEnter();
    }

    private void atualizar() {
        System.out.println(
                "\n=== ATUALIZAR COLETA CIENTÍFICA ===");

        Long idExpedicao =
                lerLong(
                        "ID da expedição da coleta (para localizar): ");

        List<ColetaCientifica> coletas =
                service.buscarPorExpedicaoId(idExpedicao);

        if (coletas.isEmpty()) {
            System.out.println(
                    "Nenhuma coleta encontrada para essa expedição.");
            pressionarEnter();
            return;
        }

        coletas.forEach(this::exibirColeta);

        Long id =
                lerLong("ID da coleta a atualizar: ");

        ColetaCientifica coleta =
                lerDadosColeta();

        coleta.setId(id);

        service.atualizar(coleta);

        System.out.println(
                "Coleta científica atualizada com sucesso.");

        pressionarEnter();
    }

    private ColetaCientifica lerDadosColeta() {
        LocalDateTime dataHoraColeta =
                lerDataHora(
                        "Data/hora da coleta (AAAA-MM-DD HH:mm): ");

        MetodoEmpregado metodoEmpregado =
                lerMetodoEmpregado();

        System.out.print(
                "Descrição do ponto (opcional): ");

        String descricaoPonto =
                scanner.nextLine();

        System.out.print("Temperatura: ");

        BigDecimal temperatura =
                new BigDecimal(
                        scanner.nextLine().replace(",", "."));

        System.out.print("Umidade relativa (0-100): ");

        BigDecimal umidadeRelativa =
                new BigDecimal(
                        scanner.nextLine().replace(",", "."));

        System.out.print("Profundidade: ");

        BigDecimal profundidade =
                new BigDecimal(
                        scanner.nextLine().replace(",", "."));

        System.out.print("Observações (opcional): ");

        String observacoes =
                scanner.nextLine();

        SituacaoValidacaoColeta situacaoValidacao =
                lerSituacaoValidacao();

        Long expedicaoId =
                lerLong("ID da expedição: ");

        Long setorId =
                lerLong("ID do setor: ");

        Long pesquisadorId =
                lerLong(
                        "ID do pesquisador responsável: ");

        return ColetaCientifica.builder()
                .dataHoraColeta(dataHoraColeta)
                .metodoEmpregado(metodoEmpregado)
                .descricaoPonto(
                        descricaoPonto.isBlank()
                                ? null
                                : descricaoPonto)
                .temperatura(temperatura)
                .umidadeRelativa(umidadeRelativa)
                .profundidade(profundidade)
                .observacoes(
                        observacoes.isBlank()
                                ? null
                                : observacoes)
                .situacaoValidacao(situacaoValidacao)
                .expedicao(
                        Expedicao.builder()
                                .id(expedicaoId)
                                .build())
                .setor(
                        Setor.builder()
                                .id(setorId)
                                .build())
                .pesquisador(
                        Pesquisador.builder()
                                .id(pesquisadorId)
                                .build())
                .build();
    }

    private MetodoEmpregado lerMetodoEmpregado() {
        MetodoEmpregado[] metodos =
                MetodoEmpregado.values();

        System.out.println("Métodos empregados:");

        for (int i = 0; i < metodos.length; i++) {
            System.out.println(
                    i + " - " + metodos[i]);
        }

        System.out.print("Escolha o método: ");

        int indice =
                Integer.parseInt(scanner.nextLine());

        if (indice < 0 || indice >= metodos.length) {
            throw new IllegalArgumentException(
                    "Método empregado inválido.");
        }

        return metodos[indice];
    }

    private SituacaoValidacaoColeta lerSituacaoValidacao() {
        SituacaoValidacaoColeta[] situacoes =
                SituacaoValidacaoColeta.values();

        System.out.println(
                "Situações de validação:");

        for (int i = 0; i < situacoes.length; i++) {
            System.out.println(
                    i + " - " + situacoes[i]);
        }

        System.out.print("Escolha a situação: ");

        int indice =
                Integer.parseInt(scanner.nextLine());

        if (indice < 0 || indice >= situacoes.length) {
            throw new IllegalArgumentException(
                    "Situação de validação inválida.");
        }

        return situacoes[indice];
    }

    private LocalDateTime lerDataHora(String mensagem) {
        System.out.print(mensagem);

        return LocalDateTime.parse(
                scanner.nextLine(),
                FORMATO_DATA_HORA);
    }

    private Long lerLong(String mensagem) {
        System.out.print(mensagem);

        return Long.parseLong(
                scanner.nextLine());
    }

    private void exibirColeta(
            ColetaCientifica coleta) {

        System.out.println(
                "\n------------------------------");

        System.out.println(
                "ID: " + coleta.getId());

        System.out.println(
                "Data/hora: "
                        + coleta.getDataHoraColeta());

        System.out.println(
                "Método empregado: "
                        + coleta.getMetodoEmpregado());

        System.out.println(
                "Descrição do ponto: "
                        + coleta.getDescricaoPonto());

        System.out.println(
                "Temperatura: "
                        + coleta.getTemperatura());

        System.out.println(
                "Umidade relativa: "
                        + coleta.getUmidadeRelativa());

        System.out.println(
                "Profundidade: "
                        + coleta.getProfundidade());

        System.out.println(
                "Observações: "
                        + coleta.getObservacoes());

        System.out.println(
                "Situação de validação: "
                        + coleta.getSituacaoValidacao());

        if (coleta.getExpedicao() != null) {
            System.out.println(
                    "ID da expedição: "
                            + coleta.getExpedicao().getId());
        }

        if (coleta.getSetor() != null) {
            System.out.println(
                    "ID do setor: "
                            + coleta.getSetor().getId());
        }

        if (coleta.getPesquisador() != null) {
            System.out.println(
                    "ID do pesquisador: "
                            + coleta.getPesquisador().getId());
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
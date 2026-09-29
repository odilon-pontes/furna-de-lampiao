package com.furnadelampiao.view;

import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.domain.Participacao;
import com.furnadelampiao.domain.Pessoa;
import com.furnadelampiao.enums.PapelParticipante;
import com.furnadelampiao.service.ParticipacaoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ParticipacaoView {

    private final ParticipacaoService service;
    private final Scanner scanner;

    public ParticipacaoView(
            ParticipacaoService service,
            Scanner scanner) {

        this.service = service;
        this.scanner = scanner;
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== PARTICIPAÇÕES ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar todas");
            System.out.println("3 - Buscar por pessoa");
            System.out.println("4 - Buscar por expedição");
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
                        listarTodos();
                        break;

                    case 3:
                        buscarPorPessoaId();
                        break;

                    case 4:
                        buscarPorExpedicaoId();
                        break;

                    case 5:
                        atualizar();
                        break;

                    case 6:
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
        System.out.println("\n=== CADASTRAR PARTICIPAÇÃO ===");

        Participacao participacao =
                lerDadosParticipacao();

        service.cadastrar(participacao);

        System.out.println(
                "Participação cadastrada com sucesso."
        );

        pressionarEnter();
    }

    private void listarTodos() {
        System.out.println("\n=== TODAS AS PARTICIPAÇÕES ===");

        List<Participacao> participacoes =
                service.listarTodos();

        if (participacoes.isEmpty()) {
            System.out.println(
                    "Nenhuma participação cadastrada."
            );
            pressionarEnter();
            return;
        }

        participacoes.forEach(
                this::exibirParticipacao
        );

        pressionarEnter();
    }

    private void buscarPorPessoaId() {
        Long id = lerLong("\nID da pessoa: ");

        List<Participacao> participacoes =
                service.buscarPorPessoaId(id);

        if (participacoes.isEmpty()) {
            System.out.println(
                    "Nenhuma participação encontrada para essa pessoa."
            );
            pressionarEnter();
            return;
        }

        participacoes.forEach(
                this::exibirParticipacao
        );

        pressionarEnter();
    }

    private void buscarPorExpedicaoId() {
        Long id = lerLong("\nID da expedição: ");

        List<Participacao> participacoes =
                service.buscarPorExpedicaoId(id);

        if (participacoes.isEmpty()) {
            System.out.println(
                    "Nenhuma participação encontrada para essa expedição."
            );
            pressionarEnter();
            return;
        }

        participacoes.forEach(
                this::exibirParticipacao
        );

        pressionarEnter();
    }

    private void atualizar() {
        System.out.println("\n=== ATUALIZAR PARTICIPAÇÃO ===");

        Long id =
                lerLong("ID da participação: ");

        Participacao existente =
                service.buscarPorId(id);

        if (existente == null) {
            System.out.println(
                    "Participação não encontrada."
            );
            pressionarEnter();
            return;
        }

        exibirParticipacao(existente);

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

        Participacao participacao =
                lerDadosParticipacao();

        participacao.setId(id);

        service.atualizar(participacao);

        System.out.println(
                "Participação atualizada com sucesso."
        );

        pressionarEnter();
    }

    private void remover() {
        System.out.println("\n=== REMOVER PARTICIPAÇÃO ===");

        Long id =
                lerLong("ID da participação: ");

        Participacao participacao =
                service.buscarPorId(id);

        if (participacao == null) {
            System.out.println(
                    "Participação não encontrada."
            );
            pressionarEnter();
            return;
        }

        exibirParticipacao(participacao);

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

        service.remover(id);

        System.out.println(
                "Participação removida com sucesso."
        );

        pressionarEnter();
    }

    private Participacao lerDadosParticipacao() {
        Long pessoaId =
                lerLong("ID da pessoa: ");

        Long expedicaoId =
                lerLong("ID da expedição: ");

        PapelParticipante papelParticipante =
                lerPapelParticipante();

        System.out.print(
                "Data de confirmação (AAAA-MM-DD): "
        );

        String dataTexto =
                scanner.nextLine();

        LocalDate dataConfirmacao =
                dataTexto.isBlank()
                        ? null
                        : LocalDate.parse(dataTexto);

        System.out.print(
                "Valor da diária (opcional, vazio = não informado): "
        );

        String valorTexto =
                scanner.nextLine();

        BigDecimal valorDiaria =
                valorTexto.isBlank()
                        ? null
                        : new BigDecimal(
                        valorTexto.replace(",", ".")
                );

        if (valorDiaria != null &&
                valorDiaria.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Valor da diária não pode ser negativo."
            );
        }

        System.out.print(
                "Quantidade prevista de dias (opcional, vazio = não informado): "
        );

        String qtdTexto =
                scanner.nextLine();

        Integer qtdPrevistaDias =
                qtdTexto.isBlank()
                        ? null
                        : Integer.parseInt(qtdTexto);

        if (qtdPrevistaDias != null &&
                qtdPrevistaDias < 0) {

            throw new IllegalArgumentException(
                    "Quantidade prevista de dias não pode ser negativa."
            );
        }

        System.out.print(
                "Presença confirmada? (S/N, vazio = não informado): "
        );

        String presencaTexto =
                scanner.nextLine();

        Boolean presencaConfirmada =
                presencaTexto.isBlank()
                        ? null
                        : lerBoolean(presencaTexto);

        System.out.print(
                "Observações (opcional): "
        );

        String observacoes =
                scanner.nextLine();

        return Participacao.builder()
                .pessoa(
                        Pessoa.builder()
                                .id(pessoaId)
                                .build()
                )
                .expedicao(
                        Expedicao.builder()
                                .id(expedicaoId)
                                .build()
                )
                .papelParticipante(papelParticipante)
                .dataConfirmacao(dataConfirmacao)
                .valorDiaria(valorDiaria)
                .qtdPrevistaDias(qtdPrevistaDias)
                .presencaConfirmada(presencaConfirmada)
                .observacoes(
                        observacoes.isBlank()
                                ? null
                                : observacoes
                )
                .build();
    }

    private PapelParticipante lerPapelParticipante() {
        PapelParticipante[] papeis =
                PapelParticipante.values();

        System.out.println(
                "Papéis de participante:"
        );

        for (int i = 0; i < papeis.length; i++) {
            System.out.println(
                    i + " - " + papeis[i]
            );
        }

        System.out.print("Escolha o papel: ");

        int indice =
                Integer.parseInt(scanner.nextLine());

        if (indice < 0 ||
                indice >= papeis.length) {

            throw new IllegalArgumentException(
                    "Papel de participante inválido."
            );
        }

        return papeis[indice];
    }

    private Boolean lerBoolean(String valor) {
        if (valor.equalsIgnoreCase("S")) {
            return true;
        }

        if (valor.equalsIgnoreCase("N")) {
            return false;
        }

        throw new IllegalArgumentException(
                "Digite S para sim ou N para não."
        );
    }

    private Long lerLong(String mensagem) {
        System.out.print(mensagem);
        return Long.parseLong(scanner.nextLine());
    }

    private void exibirParticipacao(
            Participacao participacao) {

        System.out.println("\n------------------------------");
        System.out.println(
                "ID: " + participacao.getId()
        );
        System.out.println(
                "Papel: " +
                        participacao.getPapelParticipante()
        );
        System.out.println(
                "Data de confirmação: " +
                        participacao.getDataConfirmacao()
        );
        System.out.println(
                "Valor da diária: " +
                        participacao.getValorDiaria()
        );
        System.out.println(
                "Dias previstos: " +
                        participacao.getQtdPrevistaDias()
        );
        System.out.println(
                "Presença confirmada: " +
                        participacao.getPresencaConfirmada()
        );
        System.out.println(
                "Observações: " +
                        participacao.getObservacoes()
        );

        if (participacao.getPessoa() != null) {
            System.out.println(
                    "ID da pessoa: " +
                            participacao.getPessoa().getId()
            );
        }

        if (participacao.getExpedicao() != null) {
            System.out.println(
                    "ID da expedição: " +
                            participacao.getExpedicao().getId()
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
package com.furnadelampiao.view;

import com.furnadelampiao.domain.Endereco;
import com.furnadelampiao.domain.GuiaEspeleologico;
import com.furnadelampiao.enums.NivelCertificacao;
import com.furnadelampiao.enums.UnidadeFederativa;
import com.furnadelampiao.service.GuiaEspeleologicoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class GuiaEspeleologicoView {

    private final GuiaEspeleologicoService service;
    private final Scanner scanner;

    public GuiaEspeleologicoView(
            GuiaEspeleologicoService service,
            Scanner scanner) {

        this.service = service;
        this.scanner = scanner;
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== GUIAS DE ESPELEOLOGIA ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar todos");
            System.out.println("3 - Buscar por nível de certificação");
            System.out.println("4 - Listar certificações vencidas");
            System.out.println("5 - Atualizar");
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
                        buscarPorNivelCertificacao();
                        break;
                    case 4:
                        listarCertificacoesVencidas();
                        break;
                    case 5:
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
        System.out.println("\n=== CADASTRAR GUIA DE ESPELEOLOGIA ===");

        GuiaEspeleologico guia = lerDadosGuia();

        service.cadastrar(guia);

        System.out.println("Guia cadastrado com sucesso.");

        pressionarEnter();
    }

    private void listarTodos() {
        System.out.println("\n=== TODOS OS GUIAS ===");

        List<GuiaEspeleologico> guias =
                service.listarTodos();

        if (guias.isEmpty()) {
            System.out.println("Nenhum guia cadastrado.");
            pressionarEnter();
            return;
        }

        guias.forEach(this::exibirGuia);

        pressionarEnter();
    }

    private void buscarPorNivelCertificacao() {
        System.out.println(
                "\n=== GUIAS POR NÍVEL DE CERTIFICAÇÃO ===");

        NivelCertificacao nivel =
                lerNivelCertificacao();

        List<GuiaEspeleologico> guias =
                service.buscarPorNivelCertificacao(nivel);

        if (guias.isEmpty()) {
            System.out.println(
                    "Nenhum guia encontrado para esse nível.");
            pressionarEnter();
            return;
        }

        guias.forEach(this::exibirGuia);

        pressionarEnter();
    }

    private void listarCertificacoesVencidas() {
        System.out.println(
                "\n=== CERTIFICAÇÕES VENCIDAS ===");

        List<GuiaEspeleologico> guias =
                service.listarCertificacoesVencidas();

        if (guias.isEmpty()) {
            System.out.println(
                    "Nenhuma certificação vencida encontrada.");
            pressionarEnter();
            return;
        }

        guias.forEach(this::exibirGuia);

        pressionarEnter();
    }

    private void atualizar() {
        System.out.println("\n=== ATUALIZAR GUIA ===");

        Long id = lerLong("ID do guia: ");

        GuiaEspeleologico existente =
                service.buscarPorId(id);

        if (existente == null) {
            System.out.println("Guia não encontrado.");
            pressionarEnter();
            return;
        }

        exibirGuia(existente);

        System.out.print(
                "Confirma que deseja atualizar este guia? (S/N): ");

        String confirmacao = scanner.nextLine();

        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println("Operação cancelada.");
            pressionarEnter();
            return;
        }

        System.out.println("\nInforme os novos dados:");

        GuiaEspeleologico guia = lerDadosGuia();
        guia.setId(id);

        service.atualizar(guia);

        System.out.println(
                "Guia atualizado com sucesso.");

        pressionarEnter();
    }

    private GuiaEspeleologico lerDadosGuia() {
        System.out.println("-- Dados pessoais --");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        System.out.print(
                "Data de nascimento (AAAA-MM-DD): ");

        String dataTexto = scanner.nextLine();

        LocalDate dataNasc =
                dataTexto.isBlank()
                        ? null
                        : LocalDate.parse(dataTexto);

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        Endereco endereco = lerEndereco();

        System.out.println("-- Dados de guia --");

        System.out.print(
                "Número de credenciamento: ");

        String numCredenciamento =
                scanner.nextLine();

        NivelCertificacao nivelCertificacao =
                lerNivelCertificacao();

        System.out.print(
                "Data de validade da certificação (AAAA-MM-DD): ");

        String validadeTexto =
                scanner.nextLine();

        LocalDate dataValidadeCertificacao =
                validadeTexto.isBlank()
                        ? null
                        : LocalDate.parse(validadeTexto);

        System.out.print(
                "Quantidade de expedições concluídas (vazio = 0): ");

        String qtdTexto =
                scanner.nextLine();

        Integer qtdExpedicoesConcluidas =
                qtdTexto.isBlank()
                        ? 0
                        : Integer.parseInt(qtdTexto);

        return GuiaEspeleologico.builder()
                .nome(nome)
                .cpf(cpf)
                .dataNasc(dataNasc)
                .email(email)
                .telefone(telefone)
                .endereco(endereco)
                .numCredenciamento(numCredenciamento)
                .nivelCertificacao(nivelCertificacao)
                .dataValidadeCertificacao(
                        dataValidadeCertificacao)
                .qtdExpedicoesConcluidas(
                        qtdExpedicoesConcluidas)
                .build();
    }

    private Endereco lerEndereco() {
        System.out.println("-- Endereço --");

        System.out.print("Logradouro: ");
        String logradouro = scanner.nextLine();

        System.out.print("Número: ");
        String numero = scanner.nextLine();

        System.out.print("Complemento (opcional): ");
        String complemento = scanner.nextLine();

        System.out.print("Bairro: ");
        String bairro = scanner.nextLine();

        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();

        UnidadeFederativa uf = lerUf();

        System.out.print("CEP: ");
        String cep = scanner.nextLine();

        return new Endereco(
                logradouro,
                numero,
                complemento.isBlank()
                        ? null
                        : complemento,
                bairro,
                cidade,
                uf,
                cep);
    }

    private UnidadeFederativa lerUf() {
        UnidadeFederativa[] ufs =
                UnidadeFederativa.values();

        System.out.println("UFs disponíveis:");

        for (int i = 0; i < ufs.length; i++) {
            System.out.println(
                    i + " - " + ufs[i]);
        }

        System.out.print("Escolha a UF: ");

        int indice =
                Integer.parseInt(scanner.nextLine());

        if (indice < 0 || indice >= ufs.length) {
            throw new IllegalArgumentException(
                    "UF inválida.");
        }

        return ufs[indice];
    }

    private NivelCertificacao lerNivelCertificacao() {
        NivelCertificacao[] niveis =
                NivelCertificacao.values();

        System.out.println(
                "Níveis de certificação:");

        for (int i = 0; i < niveis.length; i++) {
            System.out.println(
                    i + " - " + niveis[i]);
        }

        System.out.print("Escolha o nível: ");

        int indice =
                Integer.parseInt(scanner.nextLine());

        if (indice < 0 || indice >= niveis.length) {
            throw new IllegalArgumentException(
                    "Nível de certificação inválido.");
        }

        return niveis[indice];
    }

    private Long lerLong(String mensagem) {
        System.out.print(mensagem);

        return Long.parseLong(
                scanner.nextLine());
    }

    private void exibirGuia(
            GuiaEspeleologico guia) {

        System.out.println(
                "\n------------------------------");

        System.out.println(
                "ID: " + guia.getId());

        System.out.println(
                "Nome: " + guia.getNome());

        System.out.println(
                "CPF: " + guia.getCpf());

        System.out.println(
                "Data de nascimento: "
                        + guia.getDataNasc());

        System.out.println(
                "E-mail: " + guia.getEmail());

        System.out.println(
                "Telefone: " + guia.getTelefone());

        System.out.println(
                "Credenciamento: "
                        + guia.getNumCredenciamento());

        System.out.println(
                "Nível de certificação: "
                        + guia.getNivelCertificacao());

        System.out.println(
                "Validade da certificação: "
                        + guia.getDataValidadeCertificacao());

        System.out.println(
                "Expedições concluídas: "
                        + guia.getQtdExpedicoesConcluidas());

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
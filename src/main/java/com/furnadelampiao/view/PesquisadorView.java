package com.furnadelampiao.view;

import com.furnadelampiao.domain.Endereco;
import com.furnadelampiao.domain.Pesquisador;
import com.furnadelampiao.enums.Titulacao;
import com.furnadelampiao.enums.UnidadeFederativa;
import com.furnadelampiao.service.PesquisadorService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class PesquisadorView {

    private final PesquisadorService service;
    private final Scanner scanner;

    public PesquisadorView(
            PesquisadorService service,
            Scanner scanner) {

        this.service = service;
        this.scanner = scanner;
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== PESQUISADORES ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar todos");
            System.out.println("3 - Buscar por área de pesquisa");
            System.out.println("4 - Buscar por titulação");
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
                        buscarPorAreaPesquisa();
                        break;

                    case 4:
                        buscarPorTitulacao();
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
        System.out.println("\n=== CADASTRAR PESQUISADOR ===");

        Pesquisador pesquisador =
                lerDadosPesquisador();

        service.cadastrar(pesquisador);

        System.out.println(
                "Pesquisador cadastrado com sucesso."
        );

        pressionarEnter();
    }

    private void listarTodos() {
        System.out.println("\n=== TODOS OS PESQUISADORES ===");

        List<Pesquisador> pesquisadores =
                service.listarTodos();

        if (pesquisadores.isEmpty()) {
            System.out.println(
                    "Nenhum pesquisador cadastrado."
            );
            pressionarEnter();
            return;
        }

        pesquisadores.forEach(
                this::exibirPesquisador
        );

        pressionarEnter();
    }

    private void buscarPorAreaPesquisa() {
        System.out.print("\nÁrea de pesquisa: ");

        String area =
                scanner.nextLine();

        if (area.isBlank()) {
            throw new IllegalArgumentException(
                    "Área de pesquisa é obrigatória."
            );
        }

        List<Pesquisador> pesquisadores =
                service.buscarPorAreaPesquisa(area);

        if (pesquisadores.isEmpty()) {
            System.out.println(
                    "Nenhum pesquisador encontrado para essa área."
            );
            pressionarEnter();
            return;
        }

        pesquisadores.forEach(
                this::exibirPesquisador
        );

        pressionarEnter();
    }

    private void buscarPorTitulacao() {
        Titulacao titulacao =
                lerTitulacao();

        List<Pesquisador> pesquisadores =
                service.buscarPorTitulacao(titulacao);

        if (pesquisadores.isEmpty()) {
            System.out.println(
                    "Nenhum pesquisador encontrado para essa titulação."
            );
            pressionarEnter();
            return;
        }

        pesquisadores.forEach(
                this::exibirPesquisador
        );

        pressionarEnter();
    }

    private void atualizar() {
        System.out.println("\n=== ATUALIZAR PESQUISADOR ===");

        Long id =
                lerLong("ID do pesquisador: ");

        Pesquisador existente =
                service.buscarPorId(id);

        if (existente == null) {
            System.out.println(
                    "Pesquisador não encontrado."
            );
            pressionarEnter();
            return;
        }

        exibirPesquisador(existente);

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

        Pesquisador pesquisador =
                lerDadosPesquisador();

        pesquisador.setId(id);

        service.atualizar(pesquisador);

        System.out.println(
                "Pesquisador atualizado com sucesso."
        );

        pressionarEnter();
    }

    private Pesquisador lerDadosPesquisador() {
        System.out.println("-- Dados pessoais --");

        System.out.print("Nome: ");
        String nome =
                scanner.nextLine();

        System.out.print("CPF: ");
        String cpf =
                scanner.nextLine();

        System.out.print(
                "Data de nascimento (AAAA-MM-DD): "
        );

        String dataTexto =
                scanner.nextLine();

        LocalDate dataNasc =
                dataTexto.isBlank()
                        ? null
                        : LocalDate.parse(dataTexto);

        System.out.print("E-mail: ");
        String email =
                scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone =
                scanner.nextLine();

        Endereco endereco =
                lerEndereco();

        System.out.println(
                "-- Dados de pesquisador --"
        );

        System.out.print(
                "Número de registro institucional: "
        );

        String numRegistroInstitucional =
                scanner.nextLine();

        System.out.print(
                "Área principal de pesquisa: "
        );

        String areaPrincipalPesquisa =
                scanner.nextLine();

        Titulacao titulacao =
                lerTitulacao();

        System.out.print(
                "Valor diário da bolsa (vazio = não informado): "
        );

        String valorTexto =
                scanner.nextLine();

        BigDecimal valorDiarioBolsa =
                valorTexto.isBlank()
                        ? null
                        : new BigDecimal(
                        valorTexto.replace(",", ".")
                );

        if (valorDiarioBolsa != null &&
                valorDiarioBolsa.compareTo(
                        BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Valor diário da bolsa não pode ser negativo."
            );
        }

        return Pesquisador.builder()
                .nome(nome)
                .cpf(cpf)
                .dataNasc(dataNasc)
                .email(email)
                .telefone(telefone)
                .endereco(endereco)
                .numRegistroInstitucional(
                        numRegistroInstitucional
                )
                .areaPrincipalPesquisa(
                        areaPrincipalPesquisa
                )
                .titulacao(titulacao)
                .valorDiarioBolsa(
                        valorDiarioBolsa
                )
                .build();
    }

    private Endereco lerEndereco() {
        System.out.println("-- Endereço --");

        System.out.print("Logradouro: ");
        String logradouro =
                scanner.nextLine();

        System.out.print("Número: ");
        String numero =
                scanner.nextLine();

        System.out.print(
                "Complemento (opcional): "
        );

        String complemento =
                scanner.nextLine();

        System.out.print("Bairro: ");
        String bairro =
                scanner.nextLine();

        System.out.print("Cidade: ");
        String cidade =
                scanner.nextLine();

        UnidadeFederativa uf =
                lerUf();

        System.out.print("CEP: ");
        String cep =
                scanner.nextLine();

        return new Endereco(
                logradouro,
                numero,
                complemento.isBlank()
                        ? null
                        : complemento,
                bairro,
                cidade,
                uf,
                cep
        );
    }

    private UnidadeFederativa lerUf() {
        UnidadeFederativa[] ufs =
                UnidadeFederativa.values();

        System.out.println("UFs disponíveis:");

        for (int i = 0; i < ufs.length; i++) {
            System.out.println(
                    i + " - " + ufs[i]
            );
        }

        System.out.print("Escolha a UF: ");

        int indice =
                Integer.parseInt(scanner.nextLine());

        if (indice < 0 ||
                indice >= ufs.length) {

            throw new IllegalArgumentException(
                    "UF inválida."
            );
        }

        return ufs[indice];
    }

    private Titulacao lerTitulacao() {
        Titulacao[] titulacoes =
                Titulacao.values();

        System.out.println(
                "Titulações disponíveis:"
        );

        for (int i = 0; i < titulacoes.length; i++) {
            System.out.println(
                    i + " - " + titulacoes[i]
            );
        }

        System.out.print(
                "Escolha a titulação: "
        );

        int indice =
                Integer.parseInt(scanner.nextLine());

        if (indice < 0 ||
                indice >= titulacoes.length) {

            throw new IllegalArgumentException(
                    "Titulação inválida."
            );
        }

        return titulacoes[indice];
    }

    private Long lerLong(String mensagem) {
        System.out.print(mensagem);
        return Long.parseLong(
                scanner.nextLine()
        );
    }

    private void exibirPesquisador(
            Pesquisador pesquisador) {

        System.out.println(
                "\n------------------------------"
        );

        System.out.println(
                "ID: " + pesquisador.getId()
        );

        System.out.println(
                "Nome: " + pesquisador.getNome()
        );

        System.out.println(
                "CPF: " + pesquisador.getCpf()
        );

        System.out.println(
                "Data de nascimento: " +
                        pesquisador.getDataNasc()
        );

        System.out.println(
                "E-mail: " + pesquisador.getEmail()
        );

        System.out.println(
                "Telefone: " +
                        pesquisador.getTelefone()
        );

        System.out.println(
                "Registro institucional: " +
                        pesquisador.getNumRegistroInstitucional()
        );

        System.out.println(
                "Área de pesquisa: " +
                        pesquisador.getAreaPrincipalPesquisa()
        );

        System.out.println(
                "Titulação: " +
                        pesquisador.getTitulacao()
        );

        System.out.println(
                "Valor diário da bolsa: " +
                        pesquisador.getValorDiarioBolsa()
        );

        if (pesquisador.getEndereco() != null) {
            System.out.println(
                    "Endereço: " +
                            pesquisador.getEndereco()
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
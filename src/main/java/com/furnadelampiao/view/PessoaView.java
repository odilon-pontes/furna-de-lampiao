package com.furnadelampiao.view;

import com.furnadelampiao.domain.Endereco;
import com.furnadelampiao.domain.Pessoa;
import com.furnadelampiao.enums.UnidadeFederativa;
import com.furnadelampiao.service.PessoaService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class PessoaView {

    private final PessoaService service;
    private final Scanner scanner;

    public PessoaView(PessoaService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== PESSOAS ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Buscar por ID");
            System.out.println("3 - Listar todas");
            System.out.println("4 - Atualizar");
            System.out.println("5 - Remover");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");

            opcao = Integer.parseInt(scanner.nextLine());

            try {
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
                        atualizar();
                        break;
                    case 5:
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
            }
        } while (opcao != 0);
    }

    private void cadastrar() {
        System.out.println("\n=== CADASTRAR PESSOA ===");

        Pessoa pessoa = lerDadosPessoa();

        service.salvar(pessoa);

        System.out.println("Pessoa cadastrada com sucesso.");
    }

    private void buscarPorId() {
        Long id = lerLong("\nID da pessoa: ");

        Pessoa pessoa = service.buscarPorId(id);

        if (pessoa == null) {
            System.out.println("Pessoa não encontrada.");
            return;
        }

        exibirPessoa(pessoa);
    }

    private void listarTodos() {
        System.out.println("\n=== TODAS AS PESSOAS ===");

        List<Pessoa> pessoas = service.listarTodos();

        if (pessoas.isEmpty()) {
            System.out.println("Nenhuma pessoa cadastrada.");
            return;
        }

        pessoas.forEach(this::exibirPessoa);
    }

    private void atualizar() {
        System.out.println("\n=== ATUALIZAR PESSOA ===");

        Long id = lerLong("ID da pessoa: ");

        Pessoa existente = service.buscarPorId(id);

        if (existente == null) {
            System.out.println("Pessoa não encontrada.");
            return;
        }

        exibirPessoa(existente);

        System.out.println("\nInforme os novos dados:");

        Pessoa pessoa = lerDadosPessoa();
        pessoa.setId(id);

        service.atualizar(pessoa);

        System.out.println("Pessoa atualizada com sucesso.");
    }

    private void removerPorId() {
        System.out.println("\n=== REMOVER PESSOA ===");

        Long id = lerLong("ID da pessoa: ");

        Pessoa pessoa = service.buscarPorId(id);

        if (pessoa == null) {
            System.out.println("Pessoa não encontrada.");
            return;
        }

        exibirPessoa(pessoa);

        System.out.print("Confirma a remoção? (S/N): ");
        String confirmacao = scanner.nextLine();

        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println("Operação cancelada.");
            return;
        }

        service.removerPorId(id);

        System.out.println("Pessoa removida com sucesso.");
    }

    private Pessoa lerDadosPessoa() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Data de nascimento (AAAA-MM-DD): ");
        String dataTexto = scanner.nextLine();
        LocalDate dataNasc = dataTexto.isBlank() ? null : LocalDate.parse(dataTexto);

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        Endereco endereco = lerEndereco();

        System.out.print("Situação ativa? (S/N, vazio = sim): ");
        String situacaoTexto = scanner.nextLine();
        Boolean situacaoAtiva = situacaoTexto.isBlank() ? null : situacaoTexto.equalsIgnoreCase("S");

        return Pessoa.builder()
                .nome(nome)
                .cpf(cpf)
                .dataNasc(dataNasc)
                .email(email)
                .telefone(telefone)
                .endereco(endereco)
                .situacaoAtiva(situacaoAtiva)
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

        return new Endereco(logradouro, numero, complemento.isBlank() ? null : complemento, bairro, cidade, uf, cep);
    }

    private UnidadeFederativa lerUf() {
        System.out.println("UFs disponíveis:");

        UnidadeFederativa[] ufs = UnidadeFederativa.values();

        for (int i = 0; i < ufs.length; i++) {
            System.out.println(i + " - " + ufs[i]);
        }

        System.out.print("Escolha a UF: ");
        int indice = Integer.parseInt(scanner.nextLine());

        if (indice < 0 || indice >= ufs.length) {
            throw new IllegalArgumentException("UF inválida.");
        }

        return ufs[indice];
    }

    private Long lerLong(String mensagem) {
        System.out.print(mensagem);
        return Long.parseLong(scanner.nextLine());
    }

    private void exibirPessoa(Pessoa pessoa) {
        System.out.println("\n------------------------------");
        System.out.println("ID: " + pessoa.getId());
        System.out.println("Nome: " + pessoa.getNome());
        System.out.println("CPF: " + pessoa.getCpf());
        System.out.println("Data de nascimento: " + pessoa.getDataNasc());
        System.out.println("E-mail: " + pessoa.getEmail());
        System.out.println("Telefone: " + pessoa.getTelefone());
        System.out.println("Situação ativa: " + pessoa.getSituacaoAtiva());
        System.out.println("------------------------------");
    }
}
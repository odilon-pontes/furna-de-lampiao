
package com.furnadelampiao.view;

import com.furnadelampiao.domain.Pessoa;
import com.furnadelampiao.service.PessoaService;

import java.util.List;
import java.util.Scanner;

public class PessoaView {

    private final PessoaService service;
    private final Scanner scanner;

    public PessoaView(PessoaService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== PESSOAS ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Buscar por ID");
            System.out.println("3 - Listar todos");
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
        System.out.print("\nID da pessoa: ");
        Long id = Long.parseLong(scanner.nextLine());

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

        System.out.print("ID da pessoa: ");
        Long id = Long.parseLong(scanner.nextLine());

        Pessoa pessoaExistente = service.buscarPorId(id);

        if (pessoaExistente == null) {
            System.out.println("Pessoa não encontrada.");
            return;
        }

        exibirPessoa(pessoaExistente);

        System.out.println("\nInforme os novos dados:");

        Pessoa pessoa = lerDadosPessoa();
        pessoa.setId(id);

        service.atualizar(pessoa);

        System.out.println("Pessoa atualizada com sucesso.");
    }

    private void removerPorId() {
        System.out.println("\n=== REMOVER PESSOA ===");

        System.out.print("ID da pessoa: ");
        Long id = Long.parseLong(scanner.nextLine());

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
        String dataNascimento = scanner.nextLine();

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        return Pessoa.builder()
                .nome(nome)
                .cpf(cpf)
                .dataNasc(java.time.LocalDate.parse(dataNascimento))
                .email(email)
                .telefone(telefone)
                .situacaoAtiva(true)
                .build();
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

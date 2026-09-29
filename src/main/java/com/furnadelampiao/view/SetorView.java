package com.furnadelampiao.view;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.Setor;
import com.furnadelampiao.enums.CondicaoSetor;
import com.furnadelampiao.enums.NivelDificuldadeSetor;
import com.furnadelampiao.service.SetorService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class SetorView {

    private final SetorService service;
    private final Scanner scanner;

    public SetorView(
            SetorService service,
            Scanner scanner) {

        this.service = service;
        this.scanner = scanner;
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== SETORES ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Buscar por ID");
            System.out.println("3 - Listar todos");
            System.out.println("4 - Listar por caverna");
            System.out.println("5 - Listar por nível de dificuldade");
            System.out.println("6 - Atualizar");
            System.out.println("7 - Remover");
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
                        listarPorCaverna();
                        break;

                    case 5:
                        listarPorNivelDificuldade();
                        break;

                    case 6:
                        atualizar();
                        break;

                    case 7:
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
        System.out.println("\n=== CADASTRAR SETOR ===");

        Setor setor = lerDadosSetor();

        service.cadastrar(setor);

        System.out.println(
                "Setor cadastrado com sucesso."
        );

        pressionarEnter();
    }

    private void buscarPorId() {
        System.out.println("\n=== BUSCAR SETOR ===");

        Long id = lerLong("ID do setor: ");

        Setor setor = service.buscarPorId(id);

        if (setor == null) {
            System.out.println("Setor não encontrado.");
            pressionarEnter();
            return;
        }

        exibirSetor(setor);
        pressionarEnter();
    }

    private void listarTodos() {
        System.out.println("\n=== TODOS OS SETORES ===");

        List<Setor> setores =
                service.listarTodos();

        if (setores.isEmpty()) {
            System.out.println(
                    "Nenhum setor cadastrado."
            );
            pressionarEnter();
            return;
        }

        setores.forEach(this::exibirSetor);

        pressionarEnter();
    }

    private void listarPorCaverna() {
        System.out.println(
                "\n=== SETORES POR CAVERNA ==="
        );

        Long cavernaId =
                lerLong("ID da caverna: ");

        List<Setor> setores =
                service.listarPorCaverna(cavernaId);

        if (setores.isEmpty()) {
            System.out.println(
                    "Nenhum setor encontrado para essa caverna."
            );
            pressionarEnter();
            return;
        }

        setores.forEach(this::exibirSetor);

        pressionarEnter();
    }

    private void listarPorNivelDificuldade() {
        System.out.println(
                "\n=== SETORES POR NÍVEL DE DIFICULDADE ==="
        );

        NivelDificuldadeSetor nivel =
                lerNivelDificuldade();

        List<Setor> setores =
                service.listarPorNivelDificuldade(nivel);

        if (setores.isEmpty()) {
            System.out.println(
                    "Nenhum setor encontrado para esse nível."
            );
            pressionarEnter();
            return;
        }

        setores.forEach(this::exibirSetor);

        pressionarEnter();
    }

    private void atualizar() {
        System.out.println(
                "\n=== ATUALIZAR SETOR ==="
        );

        Long id = lerLong("ID do setor: ");

        Setor existente =
                service.buscarPorId(id);

        if (existente == null) {
            System.out.println(
                    "Setor não encontrado."
            );
            pressionarEnter();
            return;
        }

        exibirSetor(existente);

        System.out.print(
                "\nConfirma a atualização? (S/N): "
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

        Setor setor =
                lerDadosAtualizacao(existente);

        setor.setId(id);

        service.atualizar(setor);

        System.out.println(
                "Setor atualizado com sucesso."
        );

        pressionarEnter();
    }

    private void removerPorId() {
        System.out.println(
                "\n=== REMOVER SETOR ==="
        );

        Long id = lerLong("ID do setor: ");

        Setor setor =
                service.buscarPorId(id);

        if (setor == null) {
            System.out.println(
                    "Setor não encontrado."
            );
            pressionarEnter();
            return;
        }

        exibirSetor(setor);

        System.out.print(
                "\nConfirma a remoção? (S/N): "
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
                "Setor removido com sucesso."
        );

        pressionarEnter();
    }

    private Setor lerDadosSetor() {
        System.out.print("Denominação: ");

        String denominacao =
                scanner.nextLine();

        if (denominacao.isBlank()) {
            throw new IllegalArgumentException(
                    "Denominação é obrigatória."
            );
        }

        NivelDificuldadeSetor nivel =
                lerNivelDificuldade();

        BigDecimal profundidadeMaxima =
                lerBigDecimalOpcional(
                        "Profundidade máxima: "
                );

        BigDecimal extensaoAproximada =
                lerBigDecimalOpcional(
                        "Extensão aproximada: "
                );

        System.out.print("Descrição: ");

        String descricao =
                scanner.nextLine();

        BigDecimal riscoInundacao =
                lerBigDecimalOpcional(
                        "Risco de inundação: "
                );

        CondicaoSetor condicaoCorrente =
                lerCondicaoCorrente();

        Long cavernaId =
                lerLong("ID da caverna: ");

        if (cavernaId <= 0) {
            throw new IllegalArgumentException(
                    "ID da caverna deve ser maior que zero."
            );
        }

        Caverna caverna =
                Caverna.builder()
                        .id(cavernaId)
                        .build();

        return Setor.builder()
                .denominacao(denominacao)
                .nivelEstimadoDificuldade(nivel)
                .profundidadeMaxima(profundidadeMaxima)
                .extensaoAproximada(extensaoAproximada)
                .descricao(
                        descricao.isBlank()
                                ? null
                                : descricao
                )
                .riscoInundacao(riscoInundacao)
                .condicaoCorrente(condicaoCorrente)
                .caverna(caverna)
                .build();
    }

    private Setor lerDadosAtualizacao(
            Setor existente) {

        System.out.println(
                "\nPressione ENTER para manter o valor atual."
        );

        System.out.print(
                "Denominação ["
                        + existente.getDenominacao()
                        + "]: "
        );

        String denominacao =
                scanner.nextLine();

        if (denominacao.isBlank()) {
            denominacao =
                    existente.getDenominacao();
        }

        NivelDificuldadeSetor nivel =
                lerNivelAtualizacao(
                        existente.getNivelEstimadoDificuldade()
                );

        BigDecimal profundidadeMaxima =
                lerBigDecimalAtualizacao(
                        "Profundidade máxima",
                        existente.getProfundidadeMaxima()
                );

        BigDecimal extensaoAproximada =
                lerBigDecimalAtualizacao(
                        "Extensão aproximada",
                        existente.getExtensaoAproximada()
                );

        System.out.print(
                "Descrição ["
                        + existente.getDescricao()
                        + "]: "
        );

        String descricao =
                scanner.nextLine();

        if (descricao.isBlank()) {
            descricao =
                    existente.getDescricao();
        }

        BigDecimal riscoInundacao =
                lerBigDecimalAtualizacao(
                        "Risco de inundação",
                        existente.getRiscoInundacao()
                );

        CondicaoSetor condicao =
                lerCondicaoAtualizacao(
                        existente.getCondicaoCorrente()
                );

        Long cavernaId =
                lerCavernaAtualizacao(existente);

        Caverna caverna =
                Caverna.builder()
                        .id(cavernaId)
                        .build();

        return Setor.builder()
                .denominacao(denominacao)
                .nivelEstimadoDificuldade(nivel)
                .profundidadeMaxima(profundidadeMaxima)
                .extensaoAproximada(extensaoAproximada)
                .descricao(descricao)
                .riscoInundacao(riscoInundacao)
                .condicaoCorrente(condicao)
                .caverna(caverna)
                .build();
    }

    private NivelDificuldadeSetor lerNivelDificuldade() {
        System.out.println(
                "\nNíveis de dificuldade:"
        );

        NivelDificuldadeSetor[] niveis =
                NivelDificuldadeSetor.values();

        for (int i = 0; i < niveis.length; i++) {
            System.out.println(
                    i + " - " + niveis[i]
            );
        }

        System.out.print(
                "Escolha o nível: "
        );

        int indice =
                Integer.parseInt(scanner.nextLine());

        if (indice < 0 ||
                indice >= niveis.length) {

            throw new IllegalArgumentException(
                    "Nível de dificuldade inválido."
            );
        }

        return niveis[indice];
    }

    private NivelDificuldadeSetor lerNivelAtualizacao(
            NivelDificuldadeSetor atual) {

        System.out.println(
                "\nNível atual: " + atual
        );

        System.out.println(
                "Escolha um novo nível ou ENTER para manter:"
        );

        NivelDificuldadeSetor[] niveis =
                NivelDificuldadeSetor.values();

        for (int i = 0; i < niveis.length; i++) {
            System.out.println(
                    i + " - " + niveis[i]
            );
        }

        System.out.print("Nível: ");

        String valor =
                scanner.nextLine();

        if (valor.isBlank()) {
            return atual;
        }

        int indice =
                Integer.parseInt(valor);

        if (indice < 0 ||
                indice >= niveis.length) {

            throw new IllegalArgumentException(
                    "Nível de dificuldade inválido."
            );
        }

        return niveis[indice];
    }

    private CondicaoSetor lerCondicaoCorrente() {
        System.out.println(
                "\nCondições de corrente:"
        );

        CondicaoSetor[] condicoes =
                CondicaoSetor.values();

        for (int i = 0; i < condicoes.length; i++) {
            System.out.println(
                    i + " - " + condicoes[i]
            );
        }

        System.out.print(
                "Escolha a condição: "
        );

        int indice =
                Integer.parseInt(scanner.nextLine());

        if (indice < 0 ||
                indice >= condicoes.length) {

            throw new IllegalArgumentException(
                    "Condição corrente inválida."
            );
        }

        return condicoes[indice];
    }

    private CondicaoSetor lerCondicaoAtualizacao(
            CondicaoSetor atual) {

        System.out.println(
                "\nCondição atual: " + atual
        );

        System.out.println(
                "Escolha uma nova condição ou ENTER para manter:"
        );

        CondicaoSetor[] condicoes =
                CondicaoSetor.values();

        for (int i = 0; i < condicoes.length; i++) {
            System.out.println(
                    i + " - " + condicoes[i]
            );
        }

        System.out.print("Condição: ");

        String valor =
                scanner.nextLine();

        if (valor.isBlank()) {
            return atual;
        }

        int indice =
                Integer.parseInt(valor);

        if (indice < 0 ||
                indice >= condicoes.length) {

            throw new IllegalArgumentException(
                    "Condição corrente inválida."
            );
        }

        return condicoes[indice];
    }

    private BigDecimal lerBigDecimalOpcional(
            String mensagem) {

        System.out.print(mensagem);

        String valor =
                scanner.nextLine().trim();

        if (valor.isBlank()) {
            return null;
        }

        BigDecimal numero =
                lerBigDecimal(valor);

        validarNaoNegativo(numero, mensagem);

        return numero;
    }

    private BigDecimal lerBigDecimalAtualizacao(
            String campo,
            BigDecimal atual) {

        System.out.print(
                campo
                        + " ["
                        + atual
                        + "] (ENTER para manter): "
        );

        String valor =
                scanner.nextLine().trim();

        if (valor.isBlank()) {
            return atual;
        }

        BigDecimal numero =
                lerBigDecimal(valor);

        validarNaoNegativo(numero, campo);

        return numero;
    }

    private BigDecimal lerBigDecimal(
            String valor) {

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

    private void validarNaoNegativo(
            BigDecimal valor,
            String campo) {

        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    campo
                            + " não pode ser negativo."
            );
        }
    }

    private Long lerCavernaAtualizacao(
            Setor existente) {

        Long cavernaAtual = null;

        if (existente.getCaverna() != null) {
            cavernaAtual =
                    existente.getCaverna().getId();
        }

        System.out.print(
                "ID da caverna ["
                        + cavernaAtual
                        + "] (ENTER para manter): "
        );

        String valor =
                scanner.nextLine();

        if (valor.isBlank()) {
            return cavernaAtual;
        }

        Long id =
                Long.parseLong(valor);

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "ID da caverna deve ser maior que zero."
            );
        }

        return id;
    }

    private Long lerLong(String mensagem) {
        System.out.print(mensagem);

        Long valor =
                Long.parseLong(
                        scanner.nextLine()
                );

        if (valor <= 0) {
            throw new IllegalArgumentException(
                    "ID deve ser maior que zero."
            );
        }

        return valor;
    }

    private void exibirSetor(Setor setor) {
        System.out.println(
                "\n------------------------------"
        );

        System.out.println(
                "ID: " + setor.getId()
        );

        System.out.println(
                "Denominação: "
                        + setor.getDenominacao()
        );

        System.out.println(
                "Nível de dificuldade: "
                        + setor.getNivelEstimadoDificuldade()
        );

        System.out.println(
                "Profundidade máxima: "
                        + setor.getProfundidadeMaxima()
        );

        System.out.println(
                "Extensão aproximada: "
                        + setor.getExtensaoAproximada()
        );

        System.out.println(
                "Descrição: "
                        + setor.getDescricao()
        );

        System.out.println(
                "Risco de inundação: "
                        + setor.getRiscoInundacao()
        );

        System.out.println(
                "Condição corrente: "
                        + setor.getCondicaoCorrente()
        );

        if (setor.getCaverna() != null) {
            System.out.println(
                    "ID da caverna: "
                            + setor.getCaverna().getId()
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
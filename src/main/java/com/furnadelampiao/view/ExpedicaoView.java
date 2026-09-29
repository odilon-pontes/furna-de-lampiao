
package com.furnadelampiao.view;

import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.domain.PlanoSeguranca;
import com.furnadelampiao.domain.Setor;
import com.furnadelampiao.dto.ExpedicaoDetalheDTO;
import com.furnadelampiao.dto.ExpedicaoResumoDTO;
import com.furnadelampiao.enums.SituacaoExpedicao;
import com.furnadelampiao.service.ExpedicaoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class ExpedicaoView {

    private final ExpedicaoService service;
    private final Scanner scanner;

    public ExpedicaoView(ExpedicaoService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== EXPEDIÇÕES ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Buscar por ID");
            System.out.println("3 - Buscar detalhes por ID");
            System.out.println("4 - Listar todas");
            System.out.println("5 - Listar por caverna");
            System.out.println("6 - Listar por situação");
            System.out.println("7 - Resumo por período e situação");
            System.out.println("8 - Atualizar");
            System.out.println("9 - Remover");
            System.out.println("10 - Associar setor");
            System.out.println("11 - Desassociar setor");
            System.out.println("12 - Listar setores visitados");
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
                        buscarDetalhesPorId();
                        break;
                    case 4:
                        listarTodos();
                        break;
                    case 5:
                        listarPorCaverna();
                        break;
                    case 6:
                        listarPorSituacao();
                        break;
                    case 7:
                        listarResumoPorPeriodoESituacao();
                        break;
                    case 8:
                        atualizar();
                        break;
                    case 9:
                        removerPorId();
                        break;
                    case 10:
                        associarSetor();
                        break;
                    case 11:
                        desassociarSetor();
                        break;
                    case 12:
                        listarSetoresVisitados();
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
        System.out.println("\n=== CADASTRAR EXPEDIÇÃO ===");

        Expedicao expedicao = lerDadosExpedicao();

        service.cadastrar(expedicao);

        System.out.println("Expedição cadastrada com sucesso.");
    }

    private void buscarPorId() {
        System.out.print("\nID da expedição: ");
        Long id = Long.parseLong(scanner.nextLine());

        Expedicao expedicao = service.buscarPorId(id);

        if (expedicao == null) {
            System.out.println("Expedição não encontrada.");
            return;
        }

        exibirExpedicao(expedicao);
    }

    private void buscarDetalhesPorId() {
        System.out.print("\nID da expedição: ");
        Long id = Long.parseLong(scanner.nextLine());

        ExpedicaoDetalheDTO detalhe = service.buscarDetalhesPorId(id);

        if (detalhe == null) {
            System.out.println("Expedição não encontrada.");
            return;
        }

        exibirDetalhes(detalhe);
    }

    private void listarTodos() {
        System.out.println("\n=== TODAS AS EXPEDIÇÕES ===");

        List<Expedicao> expedicoes = service.listarTodos();

        if (expedicoes.isEmpty()) {
            System.out.println("Nenhuma expedição cadastrada.");
            return;
        }

        expedicoes.forEach(this::exibirExpedicao);
    }

    private void listarPorCaverna() {
        System.out.println("\n=== EXPEDIÇÕES POR CAVERNA ===");

        System.out.print("ID da caverna: ");
        Long cavernaId = Long.parseLong(scanner.nextLine());

        List<Expedicao> expedicoes = service.listarPorCaverna(cavernaId);

        if (expedicoes.isEmpty()) {
            System.out.println("Nenhuma expedição encontrada.");
            return;
        }

        expedicoes.forEach(this::exibirExpedicao);
    }

    private void listarPorSituacao() {
        System.out.println("\n=== EXPEDIÇÕES POR SITUAÇÃO ===");

        SituacaoExpedicao situacao = lerSituacao();

        List<Expedicao> expedicoes = service.listarPorSituacao(situacao);

        if (expedicoes.isEmpty()) {
            System.out.println("Nenhuma expedição encontrada.");
            return;
        }

        expedicoes.forEach(this::exibirExpedicao);
    }

    private void listarResumoPorPeriodoESituacao() {
        System.out.println("\n=== RESUMO POR PERÍODO E SITUAÇÃO ===");

        LocalDate inicio = lerData("Data inicial (AAAA-MM-DD): ");
        LocalDate fim = lerData("Data final (AAAA-MM-DD): ");
        SituacaoExpedicao situacao = lerSituacao();

        List<ExpedicaoResumoDTO> resumos =
                service.listarResumoPorPeriodoESituacao(inicio, fim, situacao);

        if (resumos.isEmpty()) {
            System.out.println("Nenhuma expedição encontrada.");
            return;
        }

        resumos.forEach(this::exibirResumo);
    }

    private void atualizar() {
        System.out.println("\n=== ATUALIZAR EXPEDIÇÃO ===");

        System.out.print("ID da expedição: ");
        Long id = Long.parseLong(scanner.nextLine());

        Expedicao existente = service.buscarPorId(id);

        if (existente == null) {
            System.out.println("Expedição não encontrada.");
            return;
        }

        exibirExpedicao(existente);

        System.out.println("\nInforme os novos dados:");

        Expedicao expedicao = lerDadosExpedicao();
        expedicao.setId(id);

        service.atualizar(expedicao);

        System.out.println("Expedição atualizada com sucesso.");
    }

    private void removerPorId() {
        System.out.println("\n=== REMOVER EXPEDIÇÃO ===");

        System.out.print("ID da expedição: ");
        Long id = Long.parseLong(scanner.nextLine());

        Expedicao expedicao = service.buscarPorId(id);

        if (expedicao == null) {
            System.out.println("Expedição não encontrada.");
            return;
        }

        exibirExpedicao(expedicao);

        System.out.print("Confirma a remoção? (S/N): ");
        String confirmacao = scanner.nextLine();

        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println("Operação cancelada.");
            return;
        }

        service.removerPorId(id);

        System.out.println("Expedição removida com sucesso.");
    }

    private void associarSetor() {
        System.out.println("\n=== ASSOCIAR SETOR ===");

        System.out.print("ID da expedição: ");
        Long expedicaoId = Long.parseLong(scanner.nextLine());

        System.out.print("ID do setor: ");
        Long setorId = Long.parseLong(scanner.nextLine());

        service.associarSetor(expedicaoId, setorId);

        System.out.println("Setor associado à expedição com sucesso.");
    }

    private void desassociarSetor() {
        System.out.println("\n=== DESASSOCIAR SETOR ===");

        System.out.print("ID da expedição: ");
        Long expedicaoId = Long.parseLong(scanner.nextLine());

        System.out.print("ID do setor: ");
        Long setorId = Long.parseLong(scanner.nextLine());

        service.desassociarSetor(expedicaoId, setorId);

        System.out.println("Setor desassociado da expedição com sucesso.");
    }

    private void listarSetoresVisitados() {
        System.out.println("\n=== SETORES VISITADOS ===");

        System.out.print("ID da expedição: ");
        Long expedicaoId = Long.parseLong(scanner.nextLine());

        List<Setor> setores = service.listarSetoresVisitados(expedicaoId);

        if (setores.isEmpty()) {
            System.out.println("Nenhum setor visitado.");
            return;
        }

        setores.forEach(this::exibirSetor);
    }

    private Expedicao lerDadosExpedicao() {
        System.out.print("Código: ");
        String codigo = scanner.nextLine();
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Objetivo: ");
        String objetivo = scanner.nextLine();
        System.out.print("Início previsto (AAAA-MM-DDTHH:MM): ");
        LocalDateTime inicioPrevisto = LocalDateTime.parse(scanner.nextLine());
        System.out.print("Término previsto (AAAA-MM-DDTHH:MM): ");
        LocalDateTime terminoPrevisto = LocalDateTime.parse(scanner.nextLine());
        System.out.print("Orçamento aprovado: ");
        String orcamentoTexto = scanner.nextLine();
        BigDecimal orcamentoAprovado = orcamentoTexto.isBlank() ? null : new BigDecimal(orcamentoTexto);
        System.out.print("Custo realizado: ");
        String custoTexto = scanner.nextLine();
        BigDecimal custoRealizado = custoTexto.isBlank() ? null : new BigDecimal(custoTexto);
        System.out.print("Quantidade máxima de participantes: ");
        Integer qtdMaxParticipantes = Integer.parseInt(scanner.nextLine());
        SituacaoExpedicao situacao = lerSituacao();
        System.out.print("Cancelamento emergencial? (S/N): ");
        boolean cancelamentoEmergencial = scanner.nextLine().equalsIgnoreCase("S");
        System.out.print("ID da caverna: ");
        Long cavernaId = Long.parseLong(scanner.nextLine());
        System.out.print("ID do plano de segurança: ");
        Long planoSegurancaId = Long.parseLong(scanner.nextLine());
        Caverna caverna = Caverna.builder().id(cavernaId).build();
        PlanoSeguranca planoSeguranca = PlanoSeguranca.builder().id(planoSegurancaId).build();
        return Expedicao.builder().codigo(codigo).titulo(titulo).objetivo(objetivo).inicioPrevisto(inicioPrevisto).terminoPrevisto(terminoPrevisto).orcamentoAprovado(orcamentoAprovado).custoRealizado(custoRealizado).qtdMaxParticipantes(qtdMaxParticipantes).situacao(situacao).cancelamentoEmergencial(cancelamentoEmergencial).caverna(caverna).planoSeguranca(planoSeguranca).build();
    }

    private SituacaoExpedicao lerSituacao() {
        System.out.println("\nSituações disponíveis:");
        SituacaoExpedicao[] situacoes = SituacaoExpedicao.values();
        for (int i = 0; i < situacoes.length; i++) {
            System.out.println(i + " - " + situacoes[i]);
        }
        System.out.print("Escolha a situação: ");
        int indice = Integer.parseInt(scanner.nextLine());
        if (indice < 0 || indice >= situacoes.length) {
            throw new IllegalArgumentException("Situação inválida.");
        }
        return situacoes[indice];
    }

    private LocalDate lerData(String mensagem) {
        System.out.print(mensagem);
        return LocalDate.parse(scanner.nextLine());
    }

    private void exibirExpedicao(Expedicao expedicao) {
        System.out.println("\n------------------------------");
        System.out.println("ID: " + expedicao.getId());
        System.out.println("Código: " + expedicao.getCodigo());
        System.out.println("Título: " + expedicao.getTitulo());
        System.out.println("Objetivo: " + expedicao.getObjetivo());
        System.out.println("Início previsto: " + expedicao.getInicioPrevisto());
        System.out.println("Término previsto: " + expedicao.getTerminoPrevisto());
        System.out.println("Orçamento aprovado: " + expedicao.getOrcamentoAprovado());
        System.out.println("Custo realizado: " + expedicao.getCustoRealizado());
        System.out.println("Máximo de participantes: " + expedicao.getQtdMaxParticipantes());
        System.out.println("Situação: " + expedicao.getSituacao());
        System.out.println("Cancelamento emergencial: " + expedicao.getCancelamentoEmergencial());
        if (expedicao.getCaverna() != null) {
            System.out.println("ID da caverna: " + expedicao.getCaverna().getId());
        }
        if (expedicao.getPlanoSeguranca() != null) {
            System.out.println("ID do plano de segurança: " + expedicao.getPlanoSeguranca().getId());
        }
        System.out.println("------------------------------");
    }

    private void exibirDetalhes(ExpedicaoDetalheDTO detalhe) {
        System.out.println("\n=== DETALHES DA EXPEDIÇÃO ===");
        System.out.println(detalhe);
    }

    private void exibirResumo(ExpedicaoResumoDTO resumo) {
        System.out.println("\n------------------------------");
        System.out.println(resumo);
        System.out.println("------------------------------");
    }

    private void exibirSetor(Setor setor) {
        System.out.println("\n------------------------------");
        System.out.println("ID: " + setor.getId());
        System.out.println("Denominação: " + setor.getDenominacao());
        System.out.println("Nível de dificuldade: " + setor.getNivelEstimadoDificuldade());
        System.out.println("Condição corrente: " + setor.getCondicaoCorrente());
        System.out.println("------------------------------");
    }
}
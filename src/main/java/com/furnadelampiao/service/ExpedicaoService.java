package com.furnadelampiao.service;

import com.furnadelampiao.repository.CavernaRepository;
import com.furnadelampiao.repository.ExpedicaoRepository;
import com.furnadelampiao.repository.PlanoSegurancaRepository;
import com.furnadelampiao.repository.SetorRepository;
import com.furnadelampiao.domain.Caverna;
import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.domain.PlanoSeguranca;
import com.furnadelampiao.domain.Setor;
import com.furnadelampiao.enums.SituacaoExpedicao;
import com.furnadelampiao.infra.TransacaoExecutor;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class ExpedicaoService {

    private final EntityManager entityManager;
    private final ExpedicaoRepository repository;
    private final CavernaRepository cavernaRepository;
    private final PlanoSegurancaRepository planoSegurancaRepository;
    private final SetorRepository setorRepository;

    public ExpedicaoService(
            EntityManager entityManager,
            ExpedicaoRepository repository,
            CavernaRepository cavernaRepository,
            PlanoSegurancaRepository planoSegurancaRepository,
            SetorRepository setorRepository) {

        this.repository = repository;
        this.entityManager = entityManager;
        this.cavernaRepository = cavernaRepository;
        this.planoSegurancaRepository = planoSegurancaRepository;
        this.setorRepository = setorRepository;
    }

    public void cadastrar(Expedicao expedicao) {
        validarExpedicao(expedicao);

        Expedicao existente = repository.buscarPorCodigo(expedicao.getCodigo());
        if (existente != null) {
            throw new IllegalArgumentException(
                    "Já existe uma expedição cadastrada com o código "
                            + expedicao.getCodigo());
        }

        if (expedicao.getSituacao() == null) {
            expedicao.setSituacao(SituacaoExpedicao.PLANEJADA);
        }

        TransacaoExecutor.executar(entityManager, () -> repository.salvar(expedicao));
    }

    public Expedicao buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        return repository.buscarPorId(id);
    }

    public List<Expedicao> listarTodos() {
        return repository.listarTodos();
    }

    public List<Expedicao> listarPorCaverna(Long cavernaId) {
        if (cavernaId == null) {
            throw new IllegalArgumentException("ID da caverna não pode ser nulo.");
        }
        return repository.listarPorCaverna(cavernaId);
    }

    public List<Expedicao> listarPorSituacao(SituacaoExpedicao situacao) {
        if (situacao == null) {
            throw new IllegalArgumentException("Situação não pode ser nula.");
        }
        return repository.listarPorSituacao(situacao);
    }

    public void atualizar(Expedicao expedicao) {
        validarExpedicao(expedicao);

        if (expedicao.getId() == null) {
            throw new IllegalArgumentException(
                    "Expedição precisa de ID para ser atualizada.");
        }

        Expedicao existente = repository.buscarPorCodigo(expedicao.getCodigo());
        if (existente != null && !existente.getId().equals(expedicao.getId())) {
            throw new IllegalArgumentException(
                    "Já existe outra expedição cadastrada com o código "
                            + expedicao.getCodigo());
        }

        TransacaoExecutor.executar(entityManager, () -> repository.atualizar(expedicao));
    }

    public void removerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        TransacaoExecutor.executar(entityManager, () -> repository.removerPorId(id));
    }

    public void associarSetor(Long expedicaoId, Long setorId) {
        if (expedicaoId == null || setorId == null) {
            throw new IllegalArgumentException(
                    "ID da expedição e do setor são obrigatórios.");
        }

        Expedicao expedicao = repository.buscarPorId(expedicaoId);
        if (expedicao == null) {
            throw new IllegalArgumentException(
                    "Expedição (id=" + expedicaoId + ") não existe no banco.");
        }

        Setor setor = setorRepository.buscarPorId(setorId);
        if (setor == null) {
            throw new IllegalArgumentException(
                    "Setor (id=" + setorId + ") não existe no banco.");
        }

        if (setor.getCaverna() == null
                || !setor.getCaverna().getId().equals(expedicao.getCaverna().getId())) {
            throw new IllegalArgumentException(
                    "O setor (id=" + setorId + ") não pertence à mesma caverna da expedição "
                            + "(caverna id=" + expedicao.getCaverna().getId() + ").");
        }

        boolean jaAssociado = expedicao.getSetoresVisitados().stream()
                .anyMatch(s -> s.getId().equals(setorId));
        if (jaAssociado) {
            throw new IllegalArgumentException(
                    "Setor (id=" + setorId + ") já está associado a essa expedição.");
        }

        TransacaoExecutor.executar(entityManager, () -> {
            expedicao.getSetoresVisitados().add(setor);
            repository.atualizar(expedicao);
        });
    }

    public void desassociarSetor(Long expedicaoId, Long setorId) {
        if (expedicaoId == null || setorId == null) {
            throw new IllegalArgumentException(
                    "ID da expedição e do setor são obrigatórios.");
        }

        Expedicao expedicao = repository.buscarPorId(expedicaoId);
        if (expedicao == null) {
            throw new IllegalArgumentException(
                    "Expedição (id=" + expedicaoId + ") não existe no banco.");
        }

        boolean removido = expedicao.getSetoresVisitados()
                .removeIf(s -> s.getId().equals(setorId));

        if (!removido) {
            throw new IllegalArgumentException(
                    "Setor (id=" + setorId + ") não está associado a essa expedição.");
        }

        TransacaoExecutor.executar(entityManager, () -> repository.atualizar(expedicao));
    }

    public List<Setor> listarSetoresVisitados(Long expedicaoId) {
        if (expedicaoId == null) {
            throw new IllegalArgumentException("ID da expedição não pode ser nulo.");
        }

        Expedicao expedicao = repository.buscarPorId(expedicaoId);
        if (expedicao == null) {
            throw new IllegalArgumentException(
                    "Expedição (id=" + expedicaoId + ") não existe no banco.");
        }

        return expedicao.getSetoresVisitados();
    }

    private void validarExpedicao(Expedicao expedicao) {
        if (expedicao == null) {
            throw new IllegalArgumentException("Expedição não pode ser nula.");
        }

        if (expedicao.getCodigo() == null || expedicao.getCodigo().isBlank()) {
            throw new IllegalArgumentException("Código da expedição é obrigatório.");
        }

        if (expedicao.getTitulo() == null || expedicao.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Título da expedição é obrigatório.");
        }

        if (expedicao.getCaverna() == null || expedicao.getCaverna().getId() == null) {
            throw new IllegalArgumentException(
                    "Expedição precisa estar associada a uma caverna já cadastrada.");
        }

        Caverna caverna = cavernaRepository.buscarPorId(expedicao.getCaverna().getId());
        if (caverna == null) {
            throw new IllegalArgumentException(
                    "Caverna associada (id="
                            + expedicao.getCaverna().getId()
                            + ") não existe no banco.");
        }

        if (expedicao.getPlanoSeguranca() == null
                || expedicao.getPlanoSeguranca().getId() == null) {
            throw new IllegalArgumentException(
                    "Expedição precisa estar associada a um plano de segurança já cadastrado.");
        }

        PlanoSeguranca planoSeguranca = planoSegurancaRepository.buscarPorId(
                expedicao.getPlanoSeguranca().getId());

        if (planoSeguranca == null) {
            throw new IllegalArgumentException(
                    "Plano de segurança associado (id="
                            + expedicao.getPlanoSeguranca().getId()
                            + ") não existe no banco.");
        }

        if (expedicao.getInicioPrevisto() == null
                || expedicao.getTerminoPrevisto() == null) {
            throw new IllegalArgumentException(
                    "Datas previstas de início e término são obrigatórias.");
        }

        if (!expedicao.getTerminoPrevisto().isAfter(expedicao.getInicioPrevisto())) {
            throw new IllegalArgumentException(
                    "A data de término prevista deve ser posterior à data de início prevista.");
        }

        if (expedicao.getQtdMaxParticipantes() <= 0) {
            throw new IllegalArgumentException(
                    "Quantidade máxima de participantes deve ser maior que zero.");
        }

        if (expedicao.getOrcamentoAprovado() != null
                && expedicao.getOrcamentoAprovado().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Orçamento aprovado não pode ser negativo.");
        }

        if (expedicao.getCustoRealizado() != null
                && expedicao.getCustoRealizado().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Custo realizado não pode ser negativo.");
        }
    }
}
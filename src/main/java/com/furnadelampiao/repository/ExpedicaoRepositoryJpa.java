package com.furnadelampiao.repository;

import com.furnadelampiao.domain.Expedicao;
import com.furnadelampiao.dto.ExpedicaoDetalheDTO;
import com.furnadelampiao.dto.ExpedicaoResumoDTO;
import com.furnadelampiao.dto.ParticipanteResumoDTO;
import com.furnadelampiao.enums.SituacaoExpedicao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ExpedicaoRepositoryJpa implements ExpedicaoRepository {

        private final EntityManager entityManager;

        public ExpedicaoRepositoryJpa(EntityManager entityManager) {
                this.entityManager = entityManager;
        }

        @Override
        public void salvar(Expedicao expedicao) {
                entityManager.persist(expedicao);
        }

        @Override
        public Expedicao buscarPorId(Long id) {
                return entityManager.find(Expedicao.class, id);
        }

        @Override
        public List<Expedicao> listarTodos() {
                return entityManager
                                .createNamedQuery(
                                                "Expedicao.listarTodos",
                                                Expedicao.class)
                                .getResultList();
        }

        @Override
        public Expedicao buscarPorCodigo(String codigo) {
                try {
                        return entityManager
                                        .createNamedQuery(
                                                        "Expedicao.buscarPorCodigo",
                                                        Expedicao.class)
                                        .setParameter("codigo", codigo)
                                        .getSingleResult();
                } catch (NoResultException e) {
                        return null;
                }
        }

        @Override
        public List<Expedicao> listarPorCaverna(Long cavernaId) {
                return entityManager
                                .createNamedQuery(
                                                "Expedicao.listarPorCaverna",
                                                Expedicao.class)
                                .setParameter("cavernaId", cavernaId)
                                .getResultList();
        }

        @Override
        public List<Expedicao> listarPorSituacao(SituacaoExpedicao situacao) {
                return entityManager
                                .createNamedQuery(
                                                "Expedicao.listarPorSituacao",
                                                Expedicao.class)
                                .setParameter("situacao", situacao)
                                .getResultList();
        }

        @Override
        public void atualizar(Expedicao expedicao) {
                entityManager.merge(expedicao);
        }

        @Override
        public void removerPorId(Long id) {
                Expedicao expedicao = entityManager.find(Expedicao.class, id);

                if (expedicao != null) {
                        entityManager.remove(expedicao);
                }
        }

        @Override
        public List<ExpedicaoResumoDTO> listarResumoPorPeriodoESituacao(
                        LocalDate inicio,
                        LocalDate fim,
                        SituacaoExpedicao situacao) {

                LocalDateTime inicioDoDia = inicio.atStartOfDay();
                LocalDateTime fimDoDia = fim.atTime(LocalTime.MAX);

                return entityManager
                                .createNamedQuery(
                                                "Expedicao.listarResumoPorPeriodoESituacao",
                                                ExpedicaoResumoDTO.class)
                                .setParameter("inicioDoDia", inicioDoDia)
                                .setParameter("fimDoDia", fimDoDia)
                                .setParameter("situacao", situacao)
                                .getResultList();
        }

        @Override
        public ExpedicaoDetalheDTO buscarDetalhesPorId(Long id) {

                List<ExpedicaoDetalheDTO> resultado = entityManager
                                .createQuery(
                                                "SELECT new com.furnadelampiao.dto.ExpedicaoDetalheDTO(" +
                                                                "e.id, e.codigo, e.titulo, e.objetivo, e.caverna.nomeOficial, "
                                                                +
                                                                "e.inicioPrevisto, e.terminoPrevisto, e.orcamentoAprovado, "
                                                                +
                                                                "e.custoRealizado, e.qtdMaxParticipantes, e.situacao, "
                                                                +
                                                                "e.cancelamentoEmergencial) " +
                                                                "FROM Expedicao e " +
                                                                "WHERE e.id = :id",
                                                ExpedicaoDetalheDTO.class)
                                .setParameter("id", id)
                                .getResultList();

                if (resultado.isEmpty()) {
                        return null;
                }

                ExpedicaoDetalheDTO detalhe = resultado.get(0);

                List<ParticipanteResumoDTO> participantes = entityManager
                                .createQuery(
                                                "SELECT new com.furnadelampiao.dto.ParticipanteResumoDTO(" +
                                                                "p.pessoa.id, p.pessoa.nome, p.papelParticipante, " +
                                                                "p.dataConfirmacao, p.presencaConfirmada) " +
                                                                "FROM Participacao p " +
                                                                "WHERE p.expedicao.id = :id " +
                                                                "ORDER BY p.pessoa.nome",
                                                ParticipanteResumoDTO.class)
                                .setParameter("id", id)
                                .getResultList();

                detalhe.setParticipantes(participantes);

                return detalhe;
        }
}
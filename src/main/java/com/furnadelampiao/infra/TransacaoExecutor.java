package com.furnadelampiao.infra;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public final class TransacaoExecutor {

    private TransacaoExecutor() {
    }

    public static void executar(EntityManager entityManager, Runnable operacao) {
        EntityTransaction transacao = entityManager.getTransaction();
        try {
            transacao.begin();
            operacao.run();
            transacao.commit();
        } catch (RuntimeException e) {
            if (transacao.isActive()) {
                transacao.rollback();
            }
            throw e;
        }
    }
}
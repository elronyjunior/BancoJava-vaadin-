package com.bc.financeiro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GerenciadorTransacoes {

    private static final GerenciadorTransacoes instancia = new GerenciadorTransacoes();
    private final List<Transacao> historico = new ArrayList<>();

    private GerenciadorTransacoes() {
    }

    public static GerenciadorTransacoes getInstancia() {
        return instancia;
    }

    public void registrar(Transacao transacao) {
        historico.add(transacao);
    }

    public List<Transacao> getHistorico() {
        return Collections.unmodifiableList(historico);
    }
}

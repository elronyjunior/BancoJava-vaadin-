package com.bc.financeiro;

import java.time.Instant;

public class Transacao {

    private final String tipo;
    private final double valor;
    private final String descricao;
    private final Instant momento;

    public Transacao(String tipo, double valor, String descricao) {
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.momento = Instant.now();
    }

    public String getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public Instant getMomento() {
        return momento;
    }

    @Override
    public String toString() {
        return momento + " - " + tipo + " - R$ " + String.format("%.2f", valor) + " - " + descricao;
    }
}

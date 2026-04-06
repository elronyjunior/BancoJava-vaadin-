package com.bc.financeiro;

public class ContaInvestimento implements Conta {

    private double saldo;
    private final String tipoAtivo;

    public ContaInvestimento(String tipoAtivo) {
        this.tipoAtivo = tipoAtivo;
        this.saldo = 0.0;
    }

    @Override
    public void depositar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor de depósito deve ser positivo.");
        }
        saldo += valor;
        GerenciadorTransacoes.getInstancia().registrar(new Transacao("Depósito", valor, "Conta Investimento"));
    }

    @Override
    public void sacar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor de saque deve ser positivo.");
        }
        if (valor > saldo) {
            throw new IllegalStateException("Saldo insuficiente.");
        }
        saldo -= valor;
        GerenciadorTransacoes.getInstancia().registrar(new Transacao("Saque", valor, "Conta Investimento"));
    }

    @Override
    public String visualizar() {
        return "Conta Investimento (" + tipoAtivo + ") - Saldo: R$ " + String.format("%.2f", saldo);
    }
}

package com.bc.financeiro;

public class ContaPoupanca implements Conta {

    private double saldo;
    private final double taxaRendimento;

    public ContaPoupanca(double taxaRendimento) {
        this.taxaRendimento = taxaRendimento;
        this.saldo = 0.0;
    }

    @Override
    public void depositar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor de depósito deve ser positivo.");
        }
        saldo += valor;
        GerenciadorTransacoes.getInstancia().registrar(new Transacao("Depósito", valor, "Conta Poupança"));
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
        GerenciadorTransacoes.getInstancia().registrar(new Transacao("Saque", valor, "Conta Poupança"));
    }

    @Override
    public String visualizar() {
        return "Conta Poupança - Saldo: R$ " + String.format("%.2f", saldo) + " - Taxa de rendimento: " + (taxaRendimento * 100) + "%";
    }

    @Override
    public String getTipoConta() {
        return "Conta Poupança";
    }
}

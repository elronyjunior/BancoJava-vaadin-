package com.bc.financeiro;

public class ContaCorrente implements Conta {

    private double saldo;
    private final double limiteChequeEspecial;
    private final double limiteCredito;

    public ContaCorrente(double saldoInicial, double limiteChequeEspecial) {
        this.saldo = saldoInicial;
        this.limiteChequeEspecial = limiteChequeEspecial;
        this.limiteCredito = 0.0;
    }

    @Override
    public void depositar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor de depósito deve ser positivo.");
        }
        saldo += valor;
        GerenciadorTransacoes.getInstancia().registrar(new Transacao("Depósito", valor, "Conta Corrente"));
    }

    @Override
    public void sacar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor de saque deve ser positivo.");
        }
        if (valor > saldo + limiteChequeEspecial) {
            throw new IllegalStateException("Saldo insuficiente.");
        }
        saldo -= valor;
        GerenciadorTransacoes.getInstancia().registrar(new Transacao("Saque", valor, "Conta Corrente"));
    }

    @Override
    public String visualizar() {
        return "Conta Corrente - Saldo: R$ " + String.format("%.2f", saldo) + " - Limite Cheque Especial: R$ " + String.format("%.2f", limiteChequeEspecial);
    }

    @Override
    public String getTipoConta() {
        return "Conta Corrente";
    }
}

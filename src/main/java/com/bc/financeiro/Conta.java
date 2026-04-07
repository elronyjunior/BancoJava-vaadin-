package com.bc.financeiro;

public interface Conta {

    void depositar(double valor);

    void sacar(double valor);

    String visualizar();

    String getTipoConta();
}

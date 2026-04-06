package com.bc.financeiro;

public class PacotePessoaJuridicaFactory implements ProdutoFinanceiroFactory {

    private final ContaFactory contaFactory = new ContaFactory();

    @Override
    public Conta criarConta() {
        return contaFactory.criarConta("investimento");
    }

    @Override
    public Cartao criarCartao() {
        return new Cartao("5200 9876 5432 1098", "Mastercard", 15000.0, "11/30");
    }

    @Override
    public Seguro criarSeguro() {
        return new Seguro("Frota", 2560.0, 250000.0);
    }
}

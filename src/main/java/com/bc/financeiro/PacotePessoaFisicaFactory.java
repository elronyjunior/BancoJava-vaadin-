package com.bc.financeiro;

public class PacotePessoaFisicaFactory implements ProdutoFinanceiroFactory {

    private final ContaFactory contaFactory = new ContaFactory();

    @Override
    public Conta criarConta() {
        return contaFactory.criarConta("corrente");
    }

    @Override
    public Cartao criarCartao() {
        return new Cartao("4500 1234 5678 9012", "Visa", 5000.0, "12/29");
    }

    @Override
    public Seguro criarSeguro() {
        return new Seguro("Residencial", 850.0, 75000.0);
    }
}

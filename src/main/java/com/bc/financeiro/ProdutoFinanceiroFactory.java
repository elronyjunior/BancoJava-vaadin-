package com.bc.financeiro;

public interface ProdutoFinanceiroFactory {

    Conta criarConta();

    Cartao criarCartao();

    Seguro criarSeguro();
}

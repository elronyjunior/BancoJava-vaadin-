package com.bc.financeiro;

import java.util.List;

public interface ProdutoFinanceiroFactory {

    Conta criarConta(String tipoConta);

    Cartao criarCartao();

    Seguro criarSeguro();

    List<String> getTiposContaDisponiveis();

    String getNomePacote();

    String getDescricaoPacote();
}

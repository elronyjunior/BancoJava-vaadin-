package com.bc.financeiro;

import java.util.List;

public class PacotePessoaFisicaFactory implements ProdutoFinanceiroFactory {

    private final ContaFactory contaFactory = new ContaFactory();

    @Override
    public Conta criarConta(String tipoConta) {
        if (!getTiposContaDisponiveis().contains(tipoConta)) {
            throw new IllegalArgumentException("O pacote Pessoa Física não oferece a conta " + tipoConta + ".");
        }
        return contaFactory.criarConta(tipoConta);
    }

    @Override
    public Cartao criarCartao() {
        return new Cartao("4500 1234 5678 9012", "Visa", 5000.0, "12/29");
    }

    @Override
    public Seguro criarSeguro() {
        return new Seguro("Residencial", 850.0, 75000.0);
    }

    @Override
    public List<String> getTiposContaDisponiveis() {
        return List.of("corrente", "poupanca", "investimento");
    }

    @Override
    public String getNomePacote() {
        return "Pessoa Física";
    }

    @Override
    public String getDescricaoPacote() {
        return "Pacote com foco em uso pessoal, cartão de limite moderado, seguro residencial e opção de investimento.";
    }
}

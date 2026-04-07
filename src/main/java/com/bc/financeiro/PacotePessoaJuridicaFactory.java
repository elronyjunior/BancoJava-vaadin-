package com.bc.financeiro;

import java.util.List;

public class PacotePessoaJuridicaFactory implements ProdutoFinanceiroFactory {

    private final ContaFactory contaFactory = new ContaFactory();

    @Override
    public Conta criarConta(String tipoConta) {
        if (!getTiposContaDisponiveis().contains(tipoConta)) {
            throw new IllegalArgumentException("O pacote Pessoa Jurídica não oferece a conta " + tipoConta + ".");
        }
        return contaFactory.criarConta(tipoConta);
    }

    @Override
    public Cartao criarCartao() {
        return new Cartao("5200 9876 5432 1098", "Mastercard", 15000.0, "11/30");
    }

    @Override
    public Seguro criarSeguro() {
        return new Seguro("Frota", 2560.0, 250000.0);
    }

    @Override
    public List<String> getTiposContaDisponiveis() {
        return List.of("corrente", "investimento");
    }

    @Override
    public String getNomePacote() {
        return "Pessoa Jurídica";
    }

    @Override
    public String getDescricaoPacote() {
        return "Pacote empresarial com crédito ampliado, cobertura robusta e opção de investimento.";
    }
}

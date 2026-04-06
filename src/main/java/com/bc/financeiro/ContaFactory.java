package com.bc.financeiro;

public class ContaFactory {

    public Conta criarConta(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "corrente" -> new ContaCorrente(1000.0, 500.0);
            case "poupanca" -> new ContaPoupanca(0.03);
            case "investimento" -> new ContaInvestimento("Fundo Multimercado");
            default -> throw new IllegalArgumentException("Tipo de conta desconhecido: " + tipo);
        };
    }
}

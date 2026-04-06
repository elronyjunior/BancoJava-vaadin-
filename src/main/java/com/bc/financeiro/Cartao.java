package com.bc.financeiro;

public class Cartao {

    private final String numero;
    private final String bandeira;
    private final double limiteCredito;
    private final String validade;

    public Cartao(String numero, String bandeira, double limiteCredito, String validade) {
        this.numero = numero;
        this.bandeira = bandeira;
        this.limiteCredito = limiteCredito;
        this.validade = validade;
    }

    public String getNumero() {
        return numero;
    }

    public String getBandeira() {
        return bandeira;
    }

    public double getLimiteCredito() {
        return limiteCredito;
    }

    public String getValidade() {
        return validade;
    }

    @Override
    public String toString() {
        return "Cartão " + bandeira + " - " + numero + " - Limite R$ " + String.format("%.2f", limiteCredito) + " - Validade " + validade;
    }
}

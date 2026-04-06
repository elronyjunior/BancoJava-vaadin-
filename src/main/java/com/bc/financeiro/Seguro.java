package com.bc.financeiro;

public class Seguro {

    private final String tipoApolice;
    private final double valorPremio;
    private final double valorCobertura;

    public Seguro(String tipoApolice, double valorPremio, double valorCobertura) {
        this.tipoApolice = tipoApolice;
        this.valorPremio = valorPremio;
        this.valorCobertura = valorCobertura;
    }

    public String getTipoApolice() {
        return tipoApolice;
    }

    public double getValorPremio() {
        return valorPremio;
    }

    public double getValorCobertura() {
        return valorCobertura;
    }

    @Override
    public String toString() {
        return "Seguro " + tipoApolice + " - Prêmio R$ " + String.format("%.2f", valorPremio) + " - Cobertura R$ " + String.format("%.2f", valorCobertura);
    }
}

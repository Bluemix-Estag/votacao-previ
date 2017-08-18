package com.bakery.code.votacaoprevi.models;

/**
 * Created by Ramon on 18-Aug-17.
 */

public class Voto {
    private String cpf;
    private String chapa;

    public Voto() {
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getChapa() {
        return chapa;
    }

    public void setChapa(String chapa) {
        this.chapa = chapa;
    }
}

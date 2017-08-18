package com.bakery.code.votacaoprevi.models;

/**
 * Created by Ramon on 17-Aug-17.
 */

public class User {

    private String cpf;
    private String senha;

    public User(String cpf, String senha) {
        this.cpf = cpf;
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}

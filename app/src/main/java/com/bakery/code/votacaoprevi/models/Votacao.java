package com.bakery.code.votacaoprevi.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ramon on 17-Aug-17.
 */

public class Votacao implements Parcelable{

    private String nome;
    private String info;
    private String iniVotacao;
    private String fimVotacao;

    public Votacao() {
    }

    protected Votacao(Parcel in) {
        nome = in.readString();
        info = in.readString();
        iniVotacao = in.readString();
        fimVotacao = in.readString();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getIniVotacao() {
        return iniVotacao;
    }

    public void setIniVotacao(String iniVotacao) {
        this.iniVotacao = iniVotacao;
    }

    public String getFimVotacao() {
        return fimVotacao;
    }

    public void setFimVotacao(String fimVotacao) {
        this.fimVotacao = fimVotacao;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeString(nome);
        dest.writeString(info);
        dest.writeString(iniVotacao);
        dest.writeString(fimVotacao);
    }
    public static final Creator<Votacao> CREATOR = new Creator<Votacao>() {
        @Override
        public Votacao createFromParcel(Parcel in) {
            return new Votacao(in);
        }

        @Override
        public Votacao[] newArray(int size) {
            return new Votacao[size];
        }
    };
}

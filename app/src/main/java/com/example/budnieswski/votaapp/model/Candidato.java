package com.example.budnieswski.votaapp.model;

/**
 * Created by budnieswski on 04/12/16.
 */
public class Candidato {
    private String nome;
    private String partido;
    private String fotoURL;

    public Candidato() {
    }

    public Candidato(String nome, String partido, String fotoURL) {
        this.nome = nome;
        this.partido = partido;
        this.fotoURL = fotoURL;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPartido() {
        return partido;
    }

    public void setPartido(String partido) {
        this.partido = partido;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }
}
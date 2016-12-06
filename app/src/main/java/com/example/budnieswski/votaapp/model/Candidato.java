package com.example.budnieswski.votaapp.model;

/**
 * Created by budnieswski on 04/12/16.
 */
public class Candidato {
    private int id;
    private String type; // Define se é prefeito/vereador
    private String json;
    private String nome;
    private String partido;
    private String fotoURL;

    public Candidato() {
    }

    public Candidato(int id, String nome, String partido, String fotoURL) {
        this.id = id;
        this.nome = nome;
        this.partido = partido;
        this.fotoURL = fotoURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
package com.example.budnieswski.votaapp.model;

/**
 * Created by budnieswski on 05/12/16.
 */
public class Vote {
    private int id;
    private int userId;
    private String user;
    private String confirma;
    private String prefeito;
    private String vereador;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConfirma() {
        return confirma;
    }

    public void setConfirma(String confirma) {
        this.confirma = confirma;
    }

    public String getPrefeito() {
        return prefeito;
    }

    public void setPrefeito(String prefeito) {
        this.prefeito = prefeito;
    }

    public String getVereador() {
        return vereador;
    }

    public void setVereador(String vereador) {
        this.vereador = vereador;
    }
}

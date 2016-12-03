package com.example.budnieswski.votaapp;

/**
 * Created by budnieswski on 06/10/16.
 */
public class Datastore {

    private static boolean voteStatus = false;

    private static String[][] candidato = {
            {"Gustavo Fruet", "PMDB", "12"},
            {"Angelo Vagnhoni", "PSOL", "37"},
            {"Greca", "PT", "05"},
    };

    public static String[] getCandidato(int position) {
        return Datastore.candidato[position];
    }

    public static String[][] getCandidatos() {
        return Datastore.candidato;
    }

    public static boolean getVoteStatus() {
        return Datastore.voteStatus;
    }

    public static void setVoteStatus(boolean vote) {
        Datastore.voteStatus = vote;
    }
}

package com.example.budnieswski.votaapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.budnieswski.votaapp.model.Vote;

/**
 * Created by budnieswski on 05/12/16.
 */
public class VoteOperations {
    private SimpleDBWrapper dbHelper;
    private String[] VOTE_COLUMNS = {
            SimpleDBWrapper.VOTE_ID,
            SimpleDBWrapper.VOTE_USER,
            SimpleDBWrapper.VOTE_USER_ID,
            SimpleDBWrapper.VOTE_CONFIRMA,
            SimpleDBWrapper.VOTE_PREFEITO,
            SimpleDBWrapper.VOTE_VEREADOR
    };
    private SQLiteDatabase db;

    public VoteOperations(Context context) {
        dbHelper = new SimpleDBWrapper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Vote setUser(int userId) {
        ContentValues values = new ContentValues();

        values.put(SimpleDBWrapper.VOTE_USER_ID, userId);

        long voteID = db.insert(SimpleDBWrapper.VOTE, null, values);

        Cursor cursor = db.query(SimpleDBWrapper.VOTE, VOTE_COLUMNS,
                SimpleDBWrapper.VOTE_ID + "=" + voteID, null, null, null, null);

        cursor.moveToFirst();

        Vote vote = parseVote(cursor);
        cursor.close();

        return vote;
    }

    public Vote getUser(int userId) {
        Cursor cursor = db.query(SimpleDBWrapper.VOTE, VOTE_COLUMNS,
                SimpleDBWrapper.VOTE_USER_ID + "=" + userId, null, null, null, null);

        if (!cursor.moveToFirst())
            return null;

        Vote vote = parseVote(cursor);
        cursor.close();

        return vote;
    }

    public void setPrefeito(int userId, String prefeito) {
        ContentValues values = new ContentValues();

        values.put(SimpleDBWrapper.VOTE_PREFEITO, prefeito);

        db.update(SimpleDBWrapper.VOTE, values, SimpleDBWrapper.VOTE_USER_ID + "=" + userId, null);
    }

    public String getPrefeito(int userId) {
        Cursor cursor = db.query(SimpleDBWrapper.VOTE, VOTE_COLUMNS,
                SimpleDBWrapper.VOTE_USER_ID + "=" + userId, null, null, null, null);

        if (!cursor.moveToFirst())
            return null;

        return cursor.getString( cursor.getColumnIndex(SimpleDBWrapper.VOTE_PREFEITO) );
    }

    public void setVereador(int userId, String vereador) {
        ContentValues values = new ContentValues();

        values.put(SimpleDBWrapper.VOTE_VEREADOR, vereador);

        db.update(SimpleDBWrapper.VOTE, values, SimpleDBWrapper.VOTE_USER_ID + "=" + userId, null);
    }

    public String getVereador(int userId) {
        Cursor cursor = db.query(SimpleDBWrapper.VOTE, VOTE_COLUMNS,
                SimpleDBWrapper.VOTE_USER_ID + "=" + userId, null, null, null, null);

        if (!cursor.moveToFirst())
            return null;

        return cursor.getString( cursor.getColumnIndex(SimpleDBWrapper.VOTE_VEREADOR) );
    }

    public void setConfirma(int userId, String confirma) {
        ContentValues values = new ContentValues();

        values.put(SimpleDBWrapper.VOTE_CONFIRMA, "S");

        db.update(SimpleDBWrapper.VOTE, values, SimpleDBWrapper.VOTE_USER_ID + "=" + userId, null);
    }

    public void resetVoto(int userId) {
        ContentValues values = new ContentValues();

        values.put(SimpleDBWrapper.VOTE_VEREADOR, "");
        values.put(SimpleDBWrapper.VOTE_PREFEITO, "");

        db.update(SimpleDBWrapper.VOTE, values, SimpleDBWrapper.VOTE_USER_ID + "=" + userId, null);
    }

    private Vote parseVote(Cursor cursor) {
        Vote vote = new Vote();

        vote.setId( cursor.getInt(0) );
        vote.setUser( cursor.getString(1) );
        vote.setUserId( cursor.getInt(2) );
        vote.setConfirma( cursor.getString(3) );
        vote.setPrefeito( cursor.getString(4) );
        vote.setVereador( cursor.getString(5) );

        return vote;
    }
}

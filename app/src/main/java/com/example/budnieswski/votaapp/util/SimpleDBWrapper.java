package com.example.budnieswski.votaapp.util;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by budnieswski on 05/12/16.
 */
public class SimpleDBWrapper extends SQLiteOpenHelper {
    public static final String VOTE = "Vote";
    public static final String VOTE_ID = "_id";
    public static final String VOTE_USER     = "_user";
    public static final String VOTE_USER_ID  = "_user_id";
    public static final String VOTE_CONFIRMA = "_confirma";
    public static final String VOTE_PREFEITO = "_prefeito";
    public static final String VOTE_VEREADOR = "_vereador";

    public static final String DATABASE_NAME = "Vote.db";
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_CREATE = "CREATE TABLE " + VOTE + "("
            + VOTE_ID + " integer primary key autoincrement, "
            + VOTE_USER + " text, "
            + VOTE_USER_ID + " integer, "
            + VOTE_CONFIRMA + " text, "
            + VOTE_PREFEITO + " text, "
            + VOTE_VEREADOR + " text);";

    public SimpleDBWrapper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + VOTE);
        onCreate(db);
    }
}

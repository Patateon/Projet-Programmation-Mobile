package com.main.projet_programmation_mobile.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseCanvasHelper extends SQLiteOpenHelper {

    static final  String DATABASE_NAME = "CANVAS_DB";
    static final int DATABASE_VERSION = 1;
    static final SQLiteDatabase.CursorFactory DATABASE_FACTORY = null;

    static final String canvas_table = "_CANVAS";
    static final String id = "_ID";
    static final String name = "_name";
    static final String data = "_data";

    private static final String CREATE_CANVAS_TABLE = "CREATE TABLE " + canvas_table + "(" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + name + " TEXT, " + data + " BLOB);";
    public DatabaseCanvasHelper(Context context) {
        super(context, DATABASE_NAME, DATABASE_FACTORY, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CANVAS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + canvas_table);
    }
}

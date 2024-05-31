package com.main.projet_programmation_mobile.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseUserHelper extends SQLiteOpenHelper {

    static final  String DATABASE_NAME = "USERS_DB";
    static final int DATABASE_VERSION = 1;
    static final SQLiteDatabase.CursorFactory DATABASE_FACTORY = null;

    public static final String user_table = "_USERS";
    public static final String id = "_ID";
    public static final String username = "_username";
    public static final String mail = "_mail";
    public static final String password = "_password";

    private static final String CREATE_TABLE = "CREATE TABLE " + user_table + "(" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + username + " TEXT, " + mail + " TEXT, " + password + " TEXT);";

    public DatabaseUserHelper(Context context) {
        super(context, DATABASE_NAME, DATABASE_FACTORY, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + user_table);
    }
}

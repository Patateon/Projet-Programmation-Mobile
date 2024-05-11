package com.main.projet_programmation_mobile.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLDataException;

public class DatabaseUserManager {
    private DatabaseUserHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseUserManager(DatabaseUserHelper dbHelper, Context context) {
        this.dbHelper = dbHelper;
        this.context = context;
    }

    public DatabaseUserManager open() throws SQLDataException {
        dbHelper = new DatabaseUserHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String username, String mail, String password) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUserHelper.username, username);
        values.put(DatabaseUserHelper.mail, mail);
        values.put(DatabaseUserHelper.password, password);
        database.insert(DatabaseUserHelper.user_table, null, values);
    }

    public Cursor fetch() {
        String [] columns = new String[] {DatabaseUserHelper.id, DatabaseUserHelper.username, DatabaseUserHelper.mail, DatabaseUserHelper.password};
        Cursor cursor = database.query(DatabaseUserHelper.user_table, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
}

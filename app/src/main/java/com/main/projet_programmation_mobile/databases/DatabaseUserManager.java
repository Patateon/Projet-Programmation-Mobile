package com.main.projet_programmation_mobile.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLDataException;

public class DatabaseUserManager {
    private DatabaseUserHelper dbHelper;
    private final Context context;
    private SQLiteDatabase database;

    public DatabaseUserManager(Context context){
        this.context = context;
    }

    public void open() throws SQLDataException {
        dbHelper = new DatabaseUserHelper(context);
        database = dbHelper.getWritableDatabase();
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

    public Cursor fetch(long _id) {
        String [] columns = new String[] {DatabaseUserHelper.id, DatabaseUserHelper.username, DatabaseUserHelper.mail, DatabaseUserHelper.password};
        Cursor cursor = database.query(DatabaseUserHelper.user_table, columns, DatabaseUserHelper.id + " = " + _id, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch(String mail){
        String[] columns = new String[] {DatabaseUserHelper.id, DatabaseUserHelper.username, DatabaseUserHelper.mail, DatabaseUserHelper.password};
        Cursor cursor = database.query(DatabaseUserHelper.user_table, columns, DatabaseUserHelper.mail + " = ?", new String[] {mail}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int updateUsername(long _id, String username){
        ContentValues values = new ContentValues();
        values.put(DatabaseUserHelper.username, username);
        return database.update(DatabaseUserHelper.user_table, values, DatabaseUserHelper.id + " = " + _id, null);
    }

    public int updateMail(long _id, String mail){
        ContentValues values = new ContentValues();
        values.put(DatabaseUserHelper.mail, mail);
        return database.update(DatabaseUserHelper.user_table, values, DatabaseUserHelper.id + " = " + _id, null);
    }

    public int updatePassword(long _id, String password){
        ContentValues values = new ContentValues();
        values.put(DatabaseUserHelper.password, password);
        return database.update(DatabaseUserHelper.user_table, values, DatabaseUserHelper.id + " = " + _id, null);
    }

    public int update(long _id, String username, String mail, String password){
        ContentValues values = new ContentValues();
        values.put(DatabaseUserHelper.username, username);
        values.put(DatabaseUserHelper.mail, mail);
        values.put(DatabaseUserHelper.password, password);
        return database.update(DatabaseUserHelper.user_table, values, DatabaseUserHelper.id + " = " + _id, null);
    }

    public int delete(long _id){
        return database.delete(DatabaseUserHelper.user_table, DatabaseUserHelper.id + " = " + _id, null);
    }
}

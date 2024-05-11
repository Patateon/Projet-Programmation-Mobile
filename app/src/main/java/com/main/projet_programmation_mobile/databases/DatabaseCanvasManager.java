package com.main.projet_programmation_mobile.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseCanvasManager {
    private DatabaseCanvasHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseCanvasManager(Context context) {
        this.context = context;
    }

    public DatabaseCanvasManager open() {
        dbHelper = new DatabaseCanvasHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, byte[] data){
        ContentValues values = new ContentValues();
        values.put(DatabaseCanvasHelper.name, name);
        values.put(DatabaseCanvasHelper.data, data);
    }

    public Cursor fetch() {
        String [] columns = new String[] {DatabaseCanvasHelper.name, DatabaseCanvasHelper.data};
        Cursor cursor = database.query(DatabaseCanvasHelper.canvas_table, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name, byte[] data) {
        ContentValues values = new ContentValues();
        values.put(DatabaseCanvasHelper.name, name);
        values.put(DatabaseCanvasHelper.data, data);
        return database.update(DatabaseCanvasHelper.canvas_table, values, DatabaseCanvasHelper.id + " = " + _id, null);
    }
}

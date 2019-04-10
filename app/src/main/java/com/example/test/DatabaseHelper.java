package com.example.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    /* Names for database and table */
    private static final String DATABASE_NAME = "contacts.db";
    private static final String TABLE_NAME = "contacts_data";

    /* naming columns in SQLite Database */
    public static final String COL1 = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "PHONE";
    public static final String COL4 = "EMAIL";

    //database contructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //creates database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " NAME TEXT, PHONE TEXT, EMAIL TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //called when we want to add data to the database
    public boolean addData(String name, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);
        contentValues.put(COL3, phone);
        contentValues.put(COL4, email);

        //insert the .put content values
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // getter
    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    //Use string email because emails are unique
    public Cursor getItemID(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL4 + " = '" + email + "'";
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    //called when item wants to be deleted
    public void deleteItem(int id, String name, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'" +
                " AND " + COL3 + " = '" + phone + "'" +
                " AND " + COL4 + " = '" + email + "'";
        db.execSQL(query);
    }
}
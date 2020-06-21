package com.example.b10709043finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="location.db";
    public static final int VERSION = 1;

    public MyDBHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
        System.out.println("in db helper constructor");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("in create table");
        final String sql = String.format(
                "CREATE TABLE '%s' " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT," +
                "%s REAL NOT NULL," +
                "%s REAL NOT NULL)",
                MyContract.Location.TABLE_NAME,
                MyContract.Location.COLUMN_NAME_NAME,
                MyContract.Location.COLUMN_NAME_LONGITUDE,
                MyContract.Location.COLUMN_NAME_LATITUDE);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+MyContract.Location.TABLE_NAME);
        onCreate(db);
    }
}

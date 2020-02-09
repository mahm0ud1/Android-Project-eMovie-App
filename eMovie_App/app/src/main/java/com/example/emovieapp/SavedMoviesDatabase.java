package com.example.emovieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SavedMoviesDatabase extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Tickets.db";

    public SavedMoviesDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME,null,1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE movies (ID INTEGER PRIMARY KEY AUTOINCREMENT, chairs TEXT, movie_name TEXT)");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS movies ");
        onCreate(db);
    }
    public void onDowngrade(String chair_list,String movie_name) {
        //onUpgrade(db, oldVersion, newVersion);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("chairs",chair_list);
        contentValues.put("movie_name",movie_name);
    }
}


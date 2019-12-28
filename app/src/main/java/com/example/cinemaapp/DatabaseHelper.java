package com.example.cinemaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "users_db";
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_FIRST_NAME = "FIRST_NAME";
    public static final String COLUMN_LAST_NAME = "LAST_NAME";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_BIRTH_DATE = "BIRTH_DATE";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_FIRST_NAME + " TEXT, "
                    + COLUMN_LAST_NAME + " TEXT, "
                    + COLUMN_EMAIL + " TEXT PRIMARY KEY , "
                    + COLUMN_PASSWORD + " TEXT, "
                    + COLUMN_BIRTH_DATE + " TEXT "
                    + ")";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //insert User data to SQLite datbase
    public boolean insertUser(String fname, String lname, String email, String password, String birthDate) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("FIRST_NAME", fname);
        values.put("LAST_NAME", lname);
        values.put("EMAIL", email);
        values.put("PASSWORD", password);
        values.put("BIRTH_DATE", birthDate);
        long ins = db.insert(TABLE_NAME, null, values);
        if (ins == -1) return false;
        else return true;
    }

    //checking if email exists;
    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT* FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
        if (cursor.getCount() > 0) return false;
        else return true;
    }


    //Get user datas by email

    public ArrayList<String> getDatas(String email) {
        ArrayList<String> datas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT* FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
        if (result.moveToFirst()) {
            datas.add(result.getString(result.getColumnIndex(COLUMN_FIRST_NAME)));
            datas.add(result.getString(result.getColumnIndex(COLUMN_LAST_NAME)));
            datas.add(result.getString(result.getColumnIndex(COLUMN_EMAIL)));
            datas.add(result.getString(result.getColumnIndex(COLUMN_BIRTH_DATE)));
            return datas;
        }
        return null;
    }

    //checking the email and the password(Login Fragment)
    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (email != null && password != null) {
            cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_EMAIL + "=?" + " and " + COLUMN_PASSWORD + " =?", new String[]{email, password});
        }
        if (cursor.getCount() > 0)
            return true;
        else return false;
    }

}
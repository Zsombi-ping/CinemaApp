package com.example.cinemaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.http.SslCertificate;
import android.text.TextUtils;
import android.util.Log;

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

    private static final String TABLE_FAVOURITES = "Favourites";
    private static final String FAVOURITES_TITLE = "title";
    private static final String FAVOURITES_DESCRIPT = "descript";
    private static final String FAVOURITES_EMAIL= "email";

    public static final String CREATE_FAVTABLE =
            "CREATE TABLE " + TABLE_FAVOURITES + "("
                    + FAVOURITES_TITLE + " TEXT, "
                    + FAVOURITES_DESCRIPT + " TEXT, "
                    + FAVOURITES_EMAIL + "TEXT"
                    + ")";



    public Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_FAVTABLE);
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
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT* FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
        if (result.moveToFirst()) {
            datas.add(result.getString(result.getColumnIndex(COLUMN_FIRST_NAME)));
            datas.add(result.getString(result.getColumnIndex(COLUMN_LAST_NAME)));
            datas.add(result.getString(result.getColumnIndex(COLUMN_EMAIL)));
            datas.add(result.getString(result.getColumnIndex(COLUMN_BIRTH_DATE)));
            datas.add(result.getString(result.getColumnIndex(COLUMN_PASSWORD)));
            return datas;
        }
        return null;
    }


    // Changing password
    public boolean changePass(String email, String oldpass, String newpass) {


        ArrayList<String> datas = new ArrayList<>();
        datas = this.getDatas(email);

        if (TextUtils.equals(oldpass, datas.get(4))) {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "update " + TABLE_NAME + " set " + COLUMN_PASSWORD + " =?" + " where " + COLUMN_EMAIL + " =?";
            String[] selections = {newpass, email};
            Cursor cursor = db.rawQuery(query, selections);
            if (cursor.moveToFirst())
                Log.d("ERTEK", cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
            datas = this.getDatas(email);
            if (TextUtils.equals(datas.get(4), oldpass))
                return false;
            return true;
        }
        return false;
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

    public boolean saveMovie(String email, String title , String description) {


            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("descript", description);
            values.put("email", email);
            long ins = db.insert(TABLE_FAVOURITES, null, values);
            return true;
    }


   public ArrayList<FavMovie> getFavoite (String email) {
       SQLiteDatabase db = this.getWritableDatabase();
       ArrayList<FavMovie> favourites = new ArrayList<>();

       Cursor cursor = db.rawQuery("SELECT 1,2 FROM " + TABLE_FAVOURITES + " WHERE " + FAVOURITES_EMAIL + "=?", new String[]{email});


       while (cursor.moveToNext()) {
           FavMovie fav = new FavMovie();
           fav.setTitle(cursor.getString(0));
           fav.setDescript(cursor.getString(1));
           favourites.add(fav);
       }
     return favourites;
    }

}
package com.example.myjurnl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "journals.db";
    public static final String TABLE_NAME = "entries";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TITLE";
    public static final String COL_3 = "ENTRY";
    public static final String COL_4 = "TIMESTAMP";
    public static final String COL_5 = "IMAGE";
    public static final String COL_6 = "BITMAP";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, ENTRY TEXT, TIMESTAMP DATE, IMAGE TEXT, BITMAP TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertDataToRow(int id, String title, String entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_2, title);
        cv.put(COL_3, entry);

        db.update(TABLE_NAME, cv, COL_1+"="+id, null);
    }

    public boolean insertData(String Title, String Entry, String timeStamp, String Image,
                              String Bitmap) {
        //Log.d("MATT", Title + Entry + timeStamp + Image + " - " + BitmapImage);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, Title);
        contentValues.put(COL_3, Entry);
        contentValues.put(COL_4, timeStamp);
        contentValues.put(COL_5, Image);
        contentValues.put(COL_6, Bitmap);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            Log.d("MATT", "did not work");
            return false;
        }
        else
            return true;
    }

    public Cursor getAllData()  {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }
    public boolean deleteRow(int rowToDelete){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_1 + "=" + rowToDelete, null) > 0;
    }
}

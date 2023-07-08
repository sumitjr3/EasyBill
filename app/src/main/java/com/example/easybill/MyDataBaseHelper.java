package com.example.easybill;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "my_database.db";

    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "product";

    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_NAME = "column_1";

    public static final String COLUMN_PRICE = "column_2";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_PRICE + " TEXT)";


    public MyDataBaseHelper(@Nullable Context context) {
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


    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public boolean deleteItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_NAME, "_id = ?", new String[]{String.valueOf(id)});
        db.close();

        return rowsAffected > 0;
    }

    public Cursor getFilteredItems(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = null; // null means return all columns
        String selection = COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = { "%" + query + "%" }; // Search for partial matches
        String sortOrder = null; // no specific order
        return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

}

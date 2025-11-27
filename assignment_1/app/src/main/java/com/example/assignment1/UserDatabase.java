package com.example.assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import kotlin.NotImplementedError;

public class UserDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "users.db";
    private static final int DB_VERSION = 1;
    private static final String TB_NAME = "users";
    private static final String COL_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String CREATE_USERS = (
            "CREATE TABLE `" + TB_NAME + "` (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", " + COL_USERNAME + " TEXT UNIQUE NOT NULL" +
                    ", " + COL_PASSWORD + " TEXT NOT NULL" +
                    ");"
    );

    public UserDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        throw new NotImplementedError();
    }

    public long add_user(String username, String password) {
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        long newRowId = getWritableDatabase().insert(TB_NAME, null, values);
        if (newRowId != -1) {
            Log.d("DB", "Insert username `" + username + "` password `" + password + "` with ID = " + newRowId);
        } else {
            Log.e("DB", "Insert username `" + username + "` password `" + password + "` Failed");
        }
        return newRowId;
    }

    public boolean check_user(String username, String password) {
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(
                    TB_NAME,
                    new String[]{
                            COL_USERNAME,
                            COL_PASSWORD,
                    },
                    COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?",
                    new String[]{
                            username,
                            password,
                    },
                    null,
                    null,
                    null,
                    "1"
            );
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}

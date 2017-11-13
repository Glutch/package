package com.apackage.app.apackage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tjoffex on 13/11/17.
 *
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, "OrderDataBase", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

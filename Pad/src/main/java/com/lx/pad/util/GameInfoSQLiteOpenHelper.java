package com.lx.pad.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/11/27.
 */

public class GameInfoSQLiteOpenHelper extends SQLiteOpenHelper {
    public static String table = "gameinfo";
    private static String databaseName = "mygamedata.db";

    public GameInfoSQLiteOpenHelper(Context context){
        super(context, databaseName, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + table + "(" + "packageName TEXT PRIMARY KEY," + "appName TEXT" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

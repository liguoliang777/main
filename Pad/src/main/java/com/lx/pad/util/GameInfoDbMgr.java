package com.lx.pad.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lx.pad.ItemType.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class GameInfoDbMgr {
    private GameInfoSQLiteOpenHelper dbOpenHelper;

    public GameInfoDbMgr(Context context){
        super();
        dbOpenHelper = new GameInfoSQLiteOpenHelper(context);
    }

    public List<String> queryAppInfoFromDB(){
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        List<String> aryList = new ArrayList<String>();
        Cursor cursor = db.query(GameInfoSQLiteOpenHelper.table, null, null, null, null, null, null);
        while(cursor.moveToNext()){
            aryList.add(cursor.getString(cursor.getColumnIndex("packageName")));
        }
        cursor.close();
        db.close();
        return aryList;
    }

    public void delAppInfoFromDB(String packageName){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(GameInfoSQLiteOpenHelper.table, "packageName=?", new String[]{packageName});
        db.close();
    }

    public boolean insertAppInfoFromDB(AppInfo appInfo){
        boolean bSuccess = false;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try{
            contentValues.put("packageName", appInfo.getPackageName());
            contentValues.put("appName", appInfo.getAppName());
            db.insert(GameInfoSQLiteOpenHelper.table, null, contentValues);
            bSuccess = true;
        }catch(SQLException e){
            Log.e("err", "insert failed");
        }finally {
            db.close();
        }
        return bSuccess;
    }
}

package cn.ngame.store.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.ngame.store.core.utils.Log;

/**
 * 数据库操作工具类
 * Created by zeng on 2016/5/19.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "store.db";

    //下载文件的新
    //public static final String TABLE_NAME_FILEINFO = "fileinfo";
    public static final String TABLE_NAME_FILEINFO = "fileloadinfo";
    private static final String CREATE_TABLE_FILEINFO = "CREATE TABLE " + TABLE_NAME_FILEINFO + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name VARCHAR(100) NOT NULL," +
            "url VARCHAR(250) NOT NULL UNIQUE," +
            "md5 VARCHAR(32) NOT NULL," +
            "packageName VARCHAR(60)," +
            "versionCode INTEGER," +
            "length LONG NOT NULL," +
            "finished LONG NOT NULL," +
            "status INTEGER NOT NULL," +

            "title VARCHAR(100) NOT NULL," +
            "previewUrl VARCHAR(250)," +
            "serverId INTEGER NOT NULL," +
            "type INTEGER NOT NULL);";

    //下载线程的信息
    //public static final String TABLE_NAME_THREADINFO = "threadinfo";
    public static final String TABLE_NAME_THREADINFO = "threadloadinfo";
    private static final String CREATE_TABLE_THREADINFO = "CREATE TABLE " + TABLE_NAME_THREADINFO + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name VARCHAR(100) NOT NULL," +
            "url VARCHAR(250) NOT NULL," +
            "start LONG NOT NULL," +
            "finished LONG NOT NULL," +
            "end LONG NOT NULL);";

    //视频观看记录
    static final String TABLE_NAME_WATCH_HISTORY = "watchHistory";
    private static final String CREATE_TABLE_WATCH_HISTORY = "CREATE TABLE " + TABLE_NAME_WATCH_HISTORY + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "vid LONG NOT NULL," +
            "title VARCHAR(100) NOT NULL," +
            "imgUrl VARCHAR(250) NOT NULL UNIQUE," +
            "length LONG NOT NULL," +
            "videoLength LONG NOT NULL," +
            "watchDate LONG NOT NULL);";

    //推送消息记录
    static final String TABLE_NAME_PUSH_MSG = "pushMsg";
    private static final String CREATE_TABLE_PUSH_MSG = "CREATE TABLE " + TABLE_NAME_PUSH_MSG + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "msgId LONG NOT NULL," +
            "type INTEGER NOT NULL," +
            "isRead INTEGER NOT NULL," +
            "title VARCHAR(100) NOT NULL," +
            "description VARCHAR(250) NOT NULL," +
            "receiveDate LONG NOT NULL);";

    //搜索历史记录
    static final String TABLE_NAME_SEARCH_HISTORY = "searchHistory";
    private static final String CREATE_TABLE_SEARCH_HISTORY = "CREATE TABLE " + TABLE_NAME_SEARCH_HISTORY + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "type INTEGER NOT NULL," +
            "title VARCHAR(100) NOT NULL," +
            "date LONG NOT NULL);";

    //系统消息记录
    static final String TABLE_NAME_SYSTEM_MSG = "systemMsg";
    private static final String CREATE_TABLE_SYSTEM_MSG = "CREATE TABLE " + TABLE_NAME_SYSTEM_MSG + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "msgId LONG NOT NULL," +
            "isRead INTEGER NOT NULL," +
            "date LONG NOT NULL);";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_FILEINFO);
        db.execSQL(CREATE_TABLE_THREADINFO);
        db.execSQL(CREATE_TABLE_WATCH_HISTORY);
        db.execSQL(CREATE_TABLE_PUSH_MSG);
        db.execSQL(CREATE_TABLE_SEARCH_HISTORY);
        db.execSQL(CREATE_TABLE_SYSTEM_MSG);

        Log.d(TAG, "数据库表创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "--------------------------->>>> 升级数据库操作");
        if (oldVersion == 1) {
            if (newVersion == 2) {
                db.execSQL(CREATE_TABLE_FILEINFO);
                db.execSQL(CREATE_TABLE_THREADINFO);

                db.execSQL("DROP TABLE IF EXISTS fileinfo");    //删除旧表
                db.execSQL("DROP TABLE IF EXISTS threadinfo");  //删除旧表

                db.execSQL(CREATE_TABLE_WATCH_HISTORY);
                Log.d(TAG, "--------------------------->>>> 升级数据库操作2");
            } else if (newVersion == 3) {
                db.execSQL(CREATE_TABLE_FILEINFO);
                db.execSQL(CREATE_TABLE_THREADINFO);

                db.execSQL("DROP TABLE IF EXISTS fileinfo");    //删除旧表
                db.execSQL("DROP TABLE IF EXISTS threadinfo");  //删除旧表

                db.execSQL(CREATE_TABLE_WATCH_HISTORY);

                db.execSQL(CREATE_TABLE_PUSH_MSG);
            }
        }
        if (oldVersion <= 2) {
            db.execSQL(CREATE_TABLE_PUSH_MSG);
            db.execSQL(CREATE_TABLE_SEARCH_HISTORY);
        }
        if (oldVersion <= 3) {
            db.execSQL(CREATE_TABLE_SEARCH_HISTORY);
        }
        if (oldVersion <= 4) {
            db.execSQL(CREATE_TABLE_SYSTEM_MSG);
        }
    }
}
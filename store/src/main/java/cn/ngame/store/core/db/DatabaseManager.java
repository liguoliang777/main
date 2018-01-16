package cn.ngame.store.core.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.ngame.store.bean.SearchHistoryBean;
import cn.ngame.store.bean.ThreadInfo;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.local.model.WatchRecord;
import cn.ngame.store.push.model.PushMessage;

import static android.R.attr.id;

/**
 * 数据库具体操作类
 * Created by zeng on 2016/5/19.
 */
public class DatabaseManager {

    public static final String TAG = DatabaseManager.class.getSimpleName();

    private static DatabaseManager manager = null;
    private SQLiteDatabase db;

    private DatabaseManager(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    public static DatabaseManager getInstance(Context context) {
        if (manager == null) {
            manager = new DatabaseManager(context);
        }
        return manager;
    }

    /**
     * 关闭数据库
     */
    public void close() {
        if (db != null) {
            db.close();
        }
    }

    //////////////////////////////////////  FileInfo 表的增删改查 ///////////////////////////////////////////////


    /**
     * 添加FileLoadInfo到数据库中
     *
     * @param info 文件信息
     */
    public void addFileLoadInfo(FileLoadInfo info) {

        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME_FILEINFO + " where url = ?", new String[]{info.getUrl()});

        if (cursor.moveToFirst()) {
            this.deleteFileLoadInfoByUrl(info.getUrl());
        }
        cursor.close();

        String sql = "INSERT INTO " + DatabaseHelper.TABLE_NAME_FILEINFO + " VALUES(null,?,?,?,?,?,?,?,?,?,?,?,?);";

        db.execSQL(sql, new Object[]{info.getName(), info.getUrl(), info.getMd5(), info.getPackageName(), info.getVersionCode(), info.getLength(), info.getFinished(), info.getStatus(),
                info.getTitle(), info.getPreviewUrl(), info.getServerId(), info.getType()});

        Log.d(TAG, "数据库操作：添加FileInfo成功");
    }

    /**
     * 通过URL删除文件信息
     *
     * @param url 文件的下载路径
     */
    public void deleteFileLoadInfoByUrl(String url) {
        db.execSQL("delete from " + DatabaseHelper.TABLE_NAME_FILEINFO + " where url = ?", new Object[]{url});
        Log.d(TAG, "删除文件：" + url + "在FileInfo表中的信息");
    }

    /**
     * 更新FileInfo的字段
     *
     * @param finished 文件下载进度
     * @param status   文件当前下载状态
     * @param url      文件下载地址
     */
    public synchronized void updateFileLoadInfoColumn(long finished, int status, String url) {

        db.execSQL("update " + DatabaseHelper.TABLE_NAME_FILEINFO + " set finished = ? , status = ?  where url = ?;", new Object[]{finished, status, url});

        Log.d(TAG, "更新FileInfo的finished 和 status 字段 finished " + finished + " status " + status);

    }

    /**
     * 通过apk包名查询 文件下载信息
     *
     * @param packageName apk文件包名
     * @return 下载文件的数据库中记录
     */
    public FileLoadInfo queryFileLoadInfoByPackageName(String packageName) {

        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME_FILEINFO + " where packageName = ?", new String[]{packageName});

        FileLoadInfo info = new FileLoadInfo();
        while (cursor.moveToNext()) {

            info.setId(cursor.getInt(cursor.getColumnIndex("id")));
            info.setName(cursor.getString(cursor.getColumnIndex("name")));
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setMd5(cursor.getString(cursor.getColumnIndex("md5")));
            info.setPackageName(cursor.getString(cursor.getColumnIndex("packageName")));
            info.setVersionCode(cursor.getInt(cursor.getColumnIndex("versionCode")));
            info.setLength(cursor.getInt(cursor.getColumnIndex("length")));
            info.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            info.setStatus(cursor.getInt(cursor.getColumnIndex("status")));

            info.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            info.setPreviewUrl(cursor.getString(cursor.getColumnIndex("previewUrl")));
            info.setServerId(cursor.getInt(cursor.getColumnIndex("serverId")));
            info.setType(cursor.getInt(cursor.getColumnIndex("type")));

            /*Log.e(TAG,"id "+info.id+" | name "+info.name+" | url "+info.url+" | md5 "+info.md5 +
                    " | packageName "+info.packageName+" | length "+info.length +" | finished "+info.finished+" | status "+info.status);*/
        }
        cursor.close();

        return info;
    }

    /**
     * 查询本地库中所有file信息
     *
     * @param type 0.所有 1.游戏 2.视频
     * @return 数据库中的下载文件信息
     */
    public List<FileLoadInfo> queryAllFileLoadInfo(int type) {

        ArrayList<FileLoadInfo> fileInfoList = new ArrayList<>();

        Cursor cursor;
        if (type == 0) {
            cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME_FILEINFO, null);
        } else {
            cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME_FILEINFO + " where type = ?", new String[]{String.valueOf(type)});
        }

        while (cursor.moveToNext()) {
            FileLoadInfo info = new FileLoadInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("id")));
            info.setName(cursor.getString(cursor.getColumnIndex("name")));
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setMd5(cursor.getString(cursor.getColumnIndex("md5")));
            info.setPackageName(cursor.getString(cursor.getColumnIndex("packageName")));
            info.setVersionCode(cursor.getInt(cursor.getColumnIndex("versionCode")));
            info.setLength(cursor.getInt(cursor.getColumnIndex("length")));
            info.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            info.setStatus(cursor.getInt(cursor.getColumnIndex("status")));

            info.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            info.setPreviewUrl(cursor.getString(cursor.getColumnIndex("previewUrl")));
            info.setServerId(cursor.getInt(cursor.getColumnIndex("serverId")));
            info.setType(cursor.getInt(cursor.getColumnIndex("type")));

            fileInfoList.add(info);

            /*Log.e(TAG,"id "+info.id+" | name "+info.name+" | url "+info.url+" | md5 "+info.md5 +
                    " | packageName "+info.packageName+" | length "+info.length +" | finished "+info.finished+" | status "+info.status);*/
        }
        cursor.close();


        return fileInfoList;
    }


    //////////////////////////////////////  ThreadInfo 表的增删改查 ///////////////////////////////////////////////

    /**
     * 添加下载线程的信息到数据库中
     *
     * @param info 线程信息
     */
    public void addThreadInfo(ThreadInfo info) {

        String sql = "INSERT INTO " + DatabaseHelper.TABLE_NAME_THREADINFO + " VALUES(null,?,?,?,?,?);";
        db.execSQL(sql, new Object[]{info.name, info.url, info.start, info.finished, info.end});

        Log.d(TAG, "数据库操作：添加下载线程信息成功！");
    }

    /**
     * 通过ID删除线程信息
     * @param id 线程信息的ID
     */
    /*public void deleteThreadInfoById(int id){
        db.execSQL("delete from "+DatabaseHelper.TABLE_NAME_THREADINFO+" where id = ?",new Object[]{id});

        Log.e(TAG,"数据库操作：删除下载线程 "+id+" 的信息成功！");
    }*/

    /**
     * 通过URL删除线程信息
     *
     * @param url 线程信息的url
     */
    public void deleteThreadInfoByUrl(String url) {
        db.execSQL("delete from " + DatabaseHelper.TABLE_NAME_THREADINFO + " where url = ?", new Object[]{url});
        Log.d(TAG, "数据库操作：删除下载线程 " + url + " 的信息成功！");
    }

    /**
     * 更新下载线程的字段
     *
     * @param info 线程信息
     */
    public void updateThreadInfoFinishedColumn(ThreadInfo info) {
        db.execSQL("update " + DatabaseHelper.TABLE_NAME_THREADINFO + " set finished = ? where id = ?;", new Object[]{info.finished, info.id});
        Log.d(TAG, "数据库操作：更新下载线程 " + info.url + " 的 finished = " + info.finished + " 字段成功！");
    }

    /**
     * 查询文件的下载线程信息
     *
     * @param url 文件的下载地址
     */
    public ArrayList<ThreadInfo> queryThreadInfoByUrl(String url) {

        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME_THREADINFO + " where url = ?", new String[]{url});
        ArrayList<ThreadInfo> infoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            ThreadInfo info = new ThreadInfo();
            info.id = cursor.getInt(cursor.getColumnIndex("id"));
            info.name = cursor.getString(cursor.getColumnIndex("name"));
            info.url = cursor.getString(cursor.getColumnIndex("url"));
            info.start = cursor.getLong(cursor.getColumnIndex("start"));
            info.finished = cursor.getLong(cursor.getColumnIndex("finished"));
            info.end = cursor.getLong(cursor.getColumnIndex("end"));
            infoList.add(info);
        }
        cursor.close();

        return infoList;
    }

    ///////////////////////////////////// searchHistory 表的增删改查 ////////////////////////////////////////

    /**
     * 添加搜索历史
     *
     * @param title 搜索信息
     */
    public void addSearchHistory(String title) {
        if (null==title) {
            return;
        }
        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME_SEARCH_HISTORY + " where title = ?", new String[]{title});
        if (cursor.moveToFirst()) {
            //存在搜索记录
            String sql = "UPDATE " + DatabaseHelper.TABLE_NAME_SEARCH_HISTORY + " SET date = ?  where title = ?;";
            db.execSQL(sql, new Object[]{System.currentTimeMillis(), title});
        } else {
            //不存在搜索记录
            String sql = "INSERT INTO " + DatabaseHelper.TABLE_NAME_SEARCH_HISTORY + " VALUES(null,?,?,?);";
            db.execSQL(sql, new Object[]{"1", title, System.currentTimeMillis()});
        }
        Log.d(TAG, "数据库操作：添加搜索记录成功！");

    }

    /**
     * 删除所有搜索记录
     */
    public void deleteAllSearchHistory() {
        db.execSQL("delete from " + DatabaseHelper.TABLE_NAME_SEARCH_HISTORY);
        Log.d(TAG, "删除所有搜索记录");
    }

    /**
     * 通过ID删除搜索记录
     *
     * @param title
     */
    public void deleteSearchHistoryById(String title) {
        db.execSQL("delete from " + DatabaseHelper.TABLE_NAME_SEARCH_HISTORY + " where title = ?", new Object[]{title});
        Log.d(TAG, "删除搜索记录：" + id);
    }

    /**
     * 查询搜索记录（需求最多存5条）
     *
     * @return
     */
    public List<SearchHistoryBean> queryAllSearchHistory() throws ParseException {
        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME_SEARCH_HISTORY
                + " order by date desc limit 5", new String[]{});
        ArrayList<SearchHistoryBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            SearchHistoryBean searchHistoryBean = new SearchHistoryBean();

            searchHistoryBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
            searchHistoryBean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            searchHistoryBean.setType(cursor.getString(cursor.getColumnIndex("type")));
            long time = cursor.getLong(cursor.getColumnIndex("date"));
            searchHistoryBean.setDate(time);
            list.add(searchHistoryBean);
        }
        cursor.close();
        return list;
    }

    ///////////////////////////////////// watchHistory 表的增删改查 ////////////////////////////////////////

    /**
     * 添加视频观看历史
     *
     * @param wh 观看信息
     */
    public void addWatchHistory(WatchRecord wh) {

        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME_WATCH_HISTORY + " where imgUrl = ?", new String[]{wh.getVideoImageLink()});
        if (cursor.moveToFirst()) {
            //存在观看记录
            String sql = "UPDATE " + DatabaseHelper.TABLE_NAME_WATCH_HISTORY + " SET length = ? , watchDate = ?  where imgUrl = ?;";
            db.execSQL(sql, new Object[]{wh.getViewDuration(), System.currentTimeMillis(), wh.getVideoImageLink()});
        } else {
            //不存在观看记录
            String sql = "INSERT INTO " + DatabaseHelper.TABLE_NAME_WATCH_HISTORY + " VALUES(null,?,?,?,?,?,?);";
            db.execSQL(sql, new Object[]{wh.getVideoId(), wh.getVideoName(), wh.getVideoImageLink(), wh.getViewDuration(), wh.getVideoDuration(), System.currentTimeMillis()});
        }

        Log.d(TAG, "数据库操作：添加观看记录成功！");

    }

    /**
     * 删除所有本地观看记录
     */
    public void deleteAllWatchHistory() {

        db.execSQL("delete from " + DatabaseHelper.TABLE_NAME_WATCH_HISTORY);
        Log.d(TAG, "删除所有观看记录");
    }

    /**
     * 通过ID删除观看记录
     *
     * @param records 观看记录的ID
     */
    public void deleteWatchHistoryById(List<WatchRecord> records) {

        if (records == null || records.size() <= 0) {
            return;
        }

        StringBuffer sb = new StringBuffer("(");
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            sb.append("?,");
            ids.add(records.get(i).getId());
        }
        String inStr = sb.substring(0, sb.length() - 1) + ")";

        db.execSQL("delete from " + DatabaseHelper.TABLE_NAME_WATCH_HISTORY + " where id in" + inStr, ids.toArray());
        Log.d(TAG, "删除观看记录：" + inStr);
    }

    /**
     * 查询所有的观看记录
     *
     * @return
     */
    public List<WatchRecord> queryAllHistory() throws ParseException {

        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME_WATCH_HISTORY, new String[]{});
        ArrayList<WatchRecord> infoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            WatchRecord wr = new WatchRecord();

            wr.setId(cursor.getLong(cursor.getColumnIndex("id")));
            wr.setVideoId(cursor.getLong(cursor.getColumnIndex("vid")));
            wr.setVideoName(cursor.getString(cursor.getColumnIndex("title")));
            wr.setVideoImageLink(cursor.getString(cursor.getColumnIndex("imgUrl")));
            wr.setViewDuration(cursor.getLong(cursor.getColumnIndex("length")));
            wr.setVideoDuration(cursor.getLong(cursor.getColumnIndex("videoLength")));
            long time = cursor.getLong(cursor.getColumnIndex("watchDate"));
            wr.setViewTime(time);
            infoList.add(wr);
        }
        cursor.close();

        return infoList;
    }

    /**
     * 查询一周内的观看记录
     *
     * @return
     */
    public List<WatchRecord> queryWeekHistory() throws ParseException {

        long dataStart = getStartTime();

        String dataStartStr = String.valueOf(dataStart - (7 * 24 * 60 * 60 * 1000));
        //String dataEndStr = String.valueOf(dataStart);

        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME_WATCH_HISTORY + " where watchDate > ? order by watchDate desc", new String[]{dataStartStr});
        ArrayList<WatchRecord> infoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            WatchRecord wr = new WatchRecord();

            wr.setId(cursor.getLong(cursor.getColumnIndex("id")));
            wr.setVideoId(cursor.getLong(cursor.getColumnIndex("vid")));
            wr.setVideoName(cursor.getString(cursor.getColumnIndex("title")));
            wr.setVideoImageLink(cursor.getString(cursor.getColumnIndex("imgUrl")));
            wr.setViewDuration(cursor.getLong(cursor.getColumnIndex("length")));
            wr.setVideoDuration(cursor.getLong(cursor.getColumnIndex("videoLength")));
            long time = cursor.getLong(cursor.getColumnIndex("watchDate"));
            wr.setViewTime(time);
            infoList.add(wr);
        }
        cursor.close();

        return infoList;

    }

    /**
     * 查询更早以前的观看记录
     *
     * @return
     */
    public List<WatchRecord> queryOtherHistory() throws ParseException {

        long dataStart = getStartTime() - (7 * 24 * 60 * 60 * 1000);

        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME_WATCH_HISTORY + " where watchDate < ? order by watchDate desc", new String[]{String.valueOf(dataStart)});
        ArrayList<WatchRecord> infoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            WatchRecord wr = new WatchRecord();

            wr.setId(cursor.getLong(cursor.getColumnIndex("id")));
            wr.setVideoId(cursor.getLong(cursor.getColumnIndex("vid")));
            wr.setVideoName(cursor.getString(cursor.getColumnIndex("title")));
            wr.setVideoImageLink(cursor.getString(cursor.getColumnIndex("imgUrl")));
            wr.setViewDuration(cursor.getLong(cursor.getColumnIndex("length")));
            wr.setVideoDuration(cursor.getLong(cursor.getColumnIndex("videoLength")));
            long time = cursor.getLong(cursor.getColumnIndex("watchDate"));
            wr.setViewTime(time);
            infoList.add(wr);
        }
        cursor.close();

        return infoList;
    }

    ////////////////////////////////////////  推送消息记录（pushMsg）相关操作   //////////////////////////////////////////////

    /**
     * 添加消息记录
     *
     * @param msg 消息信息
     */
    public void addPushMsg(PushMessage msg) {


        //Log.e(TAG,"------------------------->>>>　id "+msg.getMsgId() + " type "+msg.getType());
        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME_PUSH_MSG + " where type = ? and msgId = ?", new String[]{msg.getType() + "", msg.getMsgId() + ""});
        if (cursor.moveToFirst()) {
            //存在消息记录
            Log.d(TAG, "消息已存在，不再插入数据库");
        } else {
            //不存在消息记录
            String sql = "INSERT INTO " + DatabaseHelper.TABLE_NAME_PUSH_MSG + " VALUES(null,?,?,?,?,?,?);";
            db.execSQL(sql, new Object[]{msg.getMsgId(), msg.getType(), msg.getIsRead(), msg.getTitle(), msg.getDescription(), System.currentTimeMillis()});
            //Log.d(TAG,"------------------。。。。。 执行了写操作");
        }

        Log.d(TAG, "数据库操作：添加推送消息记录成功！");
    }

    /**
     * 将消息标记为已读
     *
     * @param msgType 服务端消息类型
     * @param msgId   服务端消息ID
     */
    public void markMsgHasRead(int msgType, long msgId) {


        db.execSQL("update " + DatabaseHelper.TABLE_NAME_PUSH_MSG + " set isRead = ? where  type = ? and msgId = ?;", new Object[]{1, msgType, msgId}); //1表示已读

        /*Cursor cursor = db.rawQuery("select * from "+DatabaseHelper.TABLE_NAME_PUSH_MSG+" where type = ? and msgId = ?;",new String[]{msgType+"",msgId+""});
        if(cursor.moveToFirst()){

        }else {
            Log.d(TAG,"消息不存在，无法标记！");
        }*/
    }

    /**
     * 将所有消息标记为已读
     *
     * @param type 0.所有  1.通知  2.活动
     */
    public void markMsgAllHasRead(int type) {

        db.execSQL("update " + DatabaseHelper.TABLE_NAME_PUSH_MSG + " set isRead = ? where type = ?;", new Object[]{1, type}); //1表示已读

    }

    /**
     * 查询未读消息数
     *
     * @param type 消息类型  0.所有  1.通知 2.活动
     * @return 未读消息数量
     */
    public int getUnReadMsgCount(int type) {

        Cursor cursor = null;
        if (type == 0) {
            String sql = "select count(*) from " + DatabaseHelper.TABLE_NAME_PUSH_MSG + " where isRead = ?";
            cursor = db.rawQuery(sql, new String[]{"0"});
        } else {
            String sql = "select count(*) from " + DatabaseHelper.TABLE_NAME_PUSH_MSG + " where isRead = ? and type = ?";
            cursor = db.rawQuery(sql, new String[]{"0", type + ""});
        }

        int count = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
        }
        return count;
    }

    /**
     * 删除所有推送消息记录
     */
    public void deleteAllPushMsg() {

        db.execSQL("delete from " + DatabaseHelper.TABLE_NAME_PUSH_MSG);
        Log.d(TAG, "删除所有推送消息记录");
    }

    /**
     * 通过ID删除消息记录
     *
     * @param id 消息在本地数据库中的ID
     */
    public void deletePushMsgById(int id) {

        if (id <= 0) {
            return;
        }

        db.execSQL("delete from " + DatabaseHelper.TABLE_NAME_PUSH_MSG + " where id = ?", new Object[]{id});
        Log.d(TAG, "删除推送消息记录：" + id);
    }

    /**
     * 查询本地库中的消息记录
     *
     * @param type 0.所有 1.预告 2.活动
     * @return 推送消息记录信息
     */
    public List<PushMessage> queryPushMessage(int pageNo, int pageSize, int type) {

        //Log.e(TAG,"--------------------------------->>>>>　　NO "+ pageNo + " page Size "+pageSize + "type "+type);

        ArrayList<PushMessage> msgList = new ArrayList<>();

        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME_PUSH_MSG,
                new String[]{"id", "msgId", "type", "isRead", "title", "description", "receiveDate"},
                "type = ?",
                new String[]{"" + type},
                null, null,
                "receiveDate desc",
                ((pageNo - 1) < 0 ? 0 : (pageNo - 1) * pageSize) + "," + pageSize);

        /*Cursor cursor = db.rawQuery("select * from "+DatabaseHelper.TABLE_NAME_PUSH_MSG +
                " where type = ? orderBy receiveDate desc limit ?,?",
                new String[]{String.valueOf(type),String.valueOf(pageNo-1),String.valueOf(pageSize)});*/

        while (cursor.moveToNext()) {
            PushMessage msg = new PushMessage();
            msg.setId(cursor.getInt(cursor.getColumnIndex("id")));
            msg.setMsgId(cursor.getLong(cursor.getColumnIndex("msgId")));
            msg.setType(cursor.getInt(cursor.getColumnIndex("type")));
            msg.setIsRead(cursor.getInt(cursor.getColumnIndex("isRead")));
            msg.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            msg.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            msg.setReceiveDate(cursor.getLong(cursor.getColumnIndex("receiveDate")));
            msgList.add(msg);

            /*Log.e(TAG,"id "+msg.getId()+" | msgId "+msg.getMsgId()+" | type "+msg.getType()+" | isRead "+msg.getIsRead() +
                    " | title "+msg.getTitle()+" | description "+msg.getDescription() +" | receiveDate "+msg.getReceiveDate());*/
        }
        cursor.close();

        return msgList;
    }

    /////////////////////////////////// 管理-系统消息 /////////////////////////////////

    /**
     * 添加系统消息记录
     *
     * @param msgId 系统消息
     */
    public void addSystemMsg(int msgId) {
        //Log.e(TAG,"------------------------->>>>　id "+msg.getMsgId() + " type "+msg.getType());
        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME_SYSTEM_MSG + " where msgId = ?", new String[]{msgId + ""});
        if (cursor.moveToFirst()) {
            //存在消息记录
            Log.d(TAG, "消息已存在，不再插入数据库");
        } else {
            //不存在消息记录
            String sql = "INSERT INTO " + DatabaseHelper.TABLE_NAME_SYSTEM_MSG + " VALUES(null,?,?,?);";
            db.execSQL(sql, new Object[]{msgId, 0, System.currentTimeMillis()});
            //Log.d(TAG,"------------------。。。。。 执行了写操作");
        }
        Log.d(TAG, "数据库操作：添加系统消息记录成功！");
    }

    /**
     * 将消息标记为已读
     *
     * @param msgId 服务端消息ID
     */
    public void markMsgReaded(int msgId) {

        db.execSQL("update " + DatabaseHelper.TABLE_NAME_SYSTEM_MSG + " set isRead = ? where msgId = ?;", new Object[]{1, msgId}); //1表示已读

    }

    /**
     * 查询未读消息
     */
    public List<String> getUnReadSystem() {
        Cursor cursor = null;
        String sql = "select * from " + DatabaseHelper.TABLE_NAME_SYSTEM_MSG + " where isRead = ?";
        cursor = db.rawQuery(sql, new String[]{"0"});

        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex("msgId")));
        }
        cursor.close();
        return list;
    }

    /**
     * 查询已读消息数
     *
     * @return 未读消息数量
     */
    public int getReadSystemCount() {

        Cursor cursor = null;
        String sql = "select count(*) from " + DatabaseHelper.TABLE_NAME_SYSTEM_MSG + " where isRead = ?";
        cursor = db.rawQuery(sql, new String[]{"1"});
        int count = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
        }
        return count;
    }


    /**
     * 获取刚存储对象的自增长ID
     *
     * @param tableName 表名
     * @return 最新的自增长ID值
     */
    public int getLastRowId(String tableName) {

        Cursor cursor = db.rawQuery("select last_insert_rowid() from " + tableName, null);
        int id = 0;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    private Long getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    private Long getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime();
    }

}

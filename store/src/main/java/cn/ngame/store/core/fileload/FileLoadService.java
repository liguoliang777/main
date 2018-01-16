package cn.ngame.store.core.fileload;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import cn.ngame.store.StoreApplication;
import cn.ngame.store.bean.ThreadInfo;
import cn.ngame.store.core.db.DatabaseHelper;
import cn.ngame.store.core.db.DatabaseManager;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.FileUtil;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.util.StringUtil;

/**
 * 处理文件下载的服务
 * Created by zeng on 2016/8/5.
 */
public class FileLoadService extends Service {

    public static final String TAG = FileLoadInfo.class.getName();

    private DatabaseManager dbManager;

    private static final int THREAD_COUNT = 2;

    /**
     * 存储用户下载的游戏文件信息
     * key 被下载文件的URL
     */
    public static ConcurrentHashMap<String, FileLoadInfo> gameFileStatusMap;
    /** 手机上已安装的应用包名 */
    //public static List<String> packageNameList = new ArrayList<>();
    public static ConcurrentHashMap<String,PackageInfo> packageInfoMap;
    /** 保存正在进行的下载任务 */
    private static ConcurrentHashMap<String, List<FileLoadTask>> taskMap;

    private Timer timer;
    private TimerTask progressUpdateTask;
    private TimerTask progressSaveTask;

    private FileLoadServiceBinder binder = new FileLoadServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbManager = DatabaseManager.getInstance(getApplicationContext());
        timer = new Timer();

        initGameFileStatus();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

       // clear();
        super.onDestroy();
    }


    private void clear(){

        //1.将所有下载中的任务暂停
        pauseAllLoadTemp();

        try {
            Thread.sleep(1000*2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //2.停止所有定时任务
        if(timer != null){
            timer.cancel();
        }
        if(progressUpdateTask != null){
            progressUpdateTask.cancel();
        }
        if(progressSaveTask != null){
            progressSaveTask.cancel();
        }

        if(gameFileStatusMap != null){
            gameFileStatusMap.clear();
            gameFileStatusMap = null;
        }
        if(taskMap != null){
            taskMap.clear();
            taskMap = null;
        }
        if(packageInfoMap != null){
            packageInfoMap.clear();
            packageInfoMap = null;
        }
    }

    /**
     * 初始化游戏文件的下载状态
     */
    private void initGameFileStatus(){
        gameFileStatusMap = new ConcurrentHashMap<>(); //当前正在下载的文件
        taskMap = new ConcurrentHashMap<>();
        packageInfoMap = new ConcurrentHashMap<>();

        //1.查询数据库中所有游戏记录
        final List<FileLoadInfo> fileLoadInfos = dbManager.queryAllFileLoadInfo(FileLoadInfo.TYPE_GAME);
        for (FileLoadInfo info : fileLoadInfos){
            gameFileStatusMap.put(info.getUrl(),info);
        }

        //2.查询本机上已安装的游戏
        //TODO 添加监听器，动态获取是否安装或删除游戏
        final PackageManager packageManager = this.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo info = packageInfos.get(i);
                String pn = info.packageName;
                packageInfoMap.put(pn,info);
            }
        }

        //3.自动开启没有完成的下载任务
        for(FileLoadInfo info : fileLoadInfos){
            if(info.getStatus() == FileLoadInfo.STATUS_LOADING || info.getStatus() == FileLoadInfo.STATUS_PAUSE_TEMP){
                if(StoreApplication.net_status == Constant.NET_STATUS_WIFI ||
                        (StoreApplication.net_status == Constant.NET_STATUS_4G && StoreApplication.allowAnyNet)){

                    info.setStatus(FileLoadInfo.STATUS_LOADING);    //统一将状态恢复为下载中
                    info.setAllowAnyNet(StoreApplication.allowAnyNet);
                    startLoad(info);
                }else {
                    info.setStatus(FileLoadInfo.STATUS_PAUSE_TEMP);    //统一将状态恢复为下载中
                    info.setAllowAnyNet(StoreApplication.allowAnyNet);
                }

            }
        }

        //4.启动定时任务，更新文件下载进度，每0.5秒更新一次
        progressUpdateTask = new ProgressUpdateTimeTask();
        timer.schedule(progressUpdateTask, 1000 * 5, 500);

        //5.启动定时任务，保存文件下载进度，每3秒保存一次
        progressSaveTask = new ProgressSaveTimeTask();
        timer.schedule(progressSaveTask,1000*8,1000*3);

    }

    /**
     * 删除下载任务
     * @param fileUrl 下载文件的URL
     */
    public void deleteLoad(String fileUrl){

        FileLoadInfo fileLoadInfo = null;
        //1.删除map中的记录
        Iterator<Map.Entry<String, FileLoadInfo>> entries = gameFileStatusMap.entrySet().iterator();
        while(entries.hasNext()){
            Map.Entry<String, FileLoadInfo> entry = entries.next();
            String key = entry.getKey();
            if(key != null && key.equals(fileUrl)){
                fileLoadInfo = entry.getValue();
                entries.remove();
            }
        }

        //2.停止正在下载中的任务
        List<FileLoadTask> taskList = taskMap.get(fileUrl);
        if(taskList != null && taskList.size() > 0){
            for (FileLoadTask task : taskList){
                task.stopDownload();
            }
            taskMap.remove(fileUrl);
        }

        //3.删除gameFileStatusMa 中的记录
        if(gameFileStatusMap != null){
            gameFileStatusMap.remove(fileUrl);
        }

        //4.删除数据库中FileInfo的记录
        dbManager.deleteFileLoadInfoByUrl(fileUrl);
        //5.删除数据库中ThreadInfo的记录
        dbManager.deleteThreadInfoByUrl(fileUrl);
        //6.删除本地安装文件
        if(fileLoadInfo != null){
            FileUtil.deleteFile(fileLoadInfo.getName());
        }

    }

    /**
     * 根据游戏包名删除apk文件
     * @param packageName 下载文件的URL
     */
    public void deleteByPackage(String packageName){

        //1.通过报名查询游戏下载记录
        FileLoadInfo fileLoadInfo = dbManager.queryFileLoadInfoByPackageName(packageName);
        //2.删除本地安装文件
        if(fileLoadInfo != null){
            if(!StringUtil.isEmpty(fileLoadInfo.getName())){
                FileUtil.deleteFile(fileLoadInfo.getName());
            }
        }

    }

    /**
     * 删除所有已下载完成的游戏
     */
    public void deleteAllFinishedRecord(){

        //1.找出所有已经下载完成的记录，并从map中删除
        List<FileLoadInfo> infoList = new ArrayList<>();
        Iterator<Map.Entry<String,FileLoadInfo>> entries = gameFileStatusMap.entrySet().iterator();
        while (entries.hasNext()){
            Map.Entry<String,FileLoadInfo> entry = entries.next();
            FileLoadInfo info = entry.getValue();
            if(info.getStatus() != FileLoadInfo.STATUS_LOADING && info.getStatus() != FileLoadInfo.STATUS_PAUSE &&
                    info.getStatus() != FileLoadInfo.STATUS_PAUSE_TEMP){
                infoList.add(info);
                entries.remove();
            }
        }

        for (FileLoadInfo info : infoList){

            List<FileLoadTask> taskList = taskMap.get(info.getUrl());
            if(taskList != null && taskList.size() > 0){
                for (FileLoadTask task : taskList){
                    task.stopDownload();
                }
                taskMap.remove(info.getUrl());
            }

            //2.删除数据库中的记录
            dbManager.deleteFileLoadInfoByUrl(info.getUrl());
            //3.删除数据库中ThreadInfo的记录
            dbManager.deleteThreadInfoByUrl(info.getUrl());
            //4.删除本地安装文件
            FileUtil.deleteFile(info.getName());
        }
    }

    /**
     * 将所有下载任务临时暂停，随后择期继续下载
     */
    public void pauseAllLoadTemp(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                if(taskMap != null){
                    //1.暂停所有下载任务
                    for(List<FileLoadTask> tasks : taskMap.values()){
                        for (FileLoadTask task : tasks){
                            task.stopDownload();
                        }
                    }
                    taskMap.clear();
                }

                if(gameFileStatusMap != null){
                    //2.将所有下载中的任务状态设为TEMP
                    for (FileLoadInfo info : gameFileStatusMap.values()){
                        if(info.getStatus() == FileLoadInfo.STATUS_LOADING){
                            info.setStatus(FileLoadInfo.STATUS_PAUSE_TEMP);
                            dbManager.updateFileLoadInfoColumn(info.getFinished(),FileLoadInfo.STATUS_PAUSE_TEMP,info.getUrl());
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * 暂停下载
     * @param fileUrl 被暂停下载文件的下载地址
     */
    public void pauseLoad(final String fileUrl){

        Log.d(TAG,"---------- 单个暂停 --------->>>> 执行单个下载任务暂停");

        if(taskMap != null){
            //1.找出下载线程
            List<FileLoadTask> tasks = taskMap.get(fileUrl);
            if(tasks != null){
                Log.d(TAG,"---------- 单个暂停 --------->>>> 删除TaskMap中的记录");
                //2.删除map中的任务记录
                taskMap.remove(fileUrl);
                //3.停止下载线程
                for(FileLoadTask task : tasks){
                    task.stopDownload();
                    Log.d(TAG,"---------- 单个暂停 --------->>>> 停止任务中的下载线程："+task.toString());
                }
            }
        }

        if(gameFileStatusMap != null){
            final FileLoadInfo fileLoadInfo = gameFileStatusMap.get(fileUrl);
            if(fileLoadInfo != null){

                Log.d(TAG,"---------- 单个暂停 --------->>>> 保存文件下载进度到数据库");
                fileLoadInfo.setStatus(FileLoadInfo.STATUS_PAUSE);  //将下载状态设为暂停

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(fileLoadInfo.getStatus() == FileLoadInfo.STATUS_PAUSE){
                            //4.保存文件的下载进度到数据库
                            dbManager.updateFileLoadInfoColumn(fileLoadInfo.getFinished(),FileLoadInfo.STATUS_PAUSE,fileLoadInfo.getUrl());
                        }
                    }
                }).start();
            }
        }
    }

    /**
     * 开始下载所有临时暂停的任务
     */
    public void startLoadTempPause(){
        for (FileLoadInfo info : gameFileStatusMap.values()){
            if(info.getStatus() == FileLoadInfo.STATUS_PAUSE_TEMP){
                info.setStatus(FileLoadInfo.STATUS_LOADING);
                try {
                    downloadByPool(info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载文件
     */
    public void startLoad(final FileLoadInfo newFileLoadInfo){

        new Thread(new Runnable() {
            @Override
            public void run() {

                //1.查询数据库中是否已有该文件
                FileLoadInfo dbFileLoadInfo = gameFileStatusMap.get(newFileLoadInfo.getUrl());

                if(dbFileLoadInfo == null){
                    Log.d(TAG,"--------- 开始下载文件 ----------->>> 初次下载新文件");

                    //1.将下载信息添加进map中
                    newFileLoadInfo.setStatus(FileLoadInfo.STATUS_LOADING);
                    gameFileStatusMap.put(newFileLoadInfo.getUrl(),newFileLoadInfo);
                    //2.开始新的下载
                    loadNewFile(newFileLoadInfo);

                }else {
                    Log.d(TAG,"--------- 开始下载文件 ----------->>> 断点续传,下载上次未完成的文件！");

                    dbFileLoadInfo.setStatus(FileLoadInfo.STATUS_LOADING);
                    dbFileLoadInfo.setAllowAnyNet(newFileLoadInfo.isAllowAnyNet());

                    boolean isFileExist = FileUtil.isFileExist(newFileLoadInfo.getName());
                    if(isFileExist){
                        try {
                            downloadByPool(dbFileLoadInfo);//继续下载
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {

                        //用户可能使用其他工具清理了未下载完成的文件
                        //1.清楚map中的记录
                        gameFileStatusMap.remove(newFileLoadInfo.getUrl());
                        //2.将FileInfo表中本文件记录删除
                        dbManager.deleteFileLoadInfoByUrl(newFileLoadInfo.getUrl());
                        //3.删除ThreadInfo表中本文件的所有下载线程信息
                        dbManager.deleteThreadInfoByUrl(newFileLoadInfo.getUrl());

                        //3.重新开始新的下载
                        loadNewFile(dbFileLoadInfo);
                    }
                }

            }
        }).start();


    }

    /**
     * 下载新文件
     * @param fileInfo 下载文件的信息
     */
    private void loadNewFile(FileLoadInfo fileInfo){

        RandomAccessFile accessFile = null;
        HttpURLConnection conn = null;
        try {

            URL url = new URL(fileInfo.getUrl());
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000*10);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.connect(); //执行连接

            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {

                File file = new File(CommonUtil.getFileLoadBasePath());
                if (!file.exists()) {
                    file.mkdirs();
                }

                file = new File(file,fileInfo.getName());
                if(file.exists() && file.isFile()){
                    file.delete();
                }

                accessFile = new RandomAccessFile(file, "rwd");     //创建可随机访问的文件
                int length = conn.getContentLength();
                accessFile.setLength(length);

                //将文件信息写入数据库
                fileInfo.setLength(length);
                dbManager.addFileLoadInfo(fileInfo);

                //获取文件在数据库中的ID
                fileInfo.setId(dbManager.getLastRowId(DatabaseHelper.TABLE_NAME_FILEINFO));

                //TODO 使用线程池开始下载文件
                downloadByPool(fileInfo);

            }else {

                Log.d(TAG,"------------------------->>>>>>>>>>>>> 文件下载失败：http连接异常");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (SocketTimeoutException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(accessFile != null){
                try {
                    accessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null){
                conn.disconnect();
            }
        }
    }

    /**
     * 使用线程池下载文件
     * @param fileInfo 下载文件的信息
     */
    private void downloadByPool(FileLoadInfo fileInfo) throws Exception {


        if(fileInfo.getStatus() != FileLoadInfo.STATUS_LOADING){
            return;
        }

        //检索数据库中是否已有文件的下载线程
        ArrayList<ThreadInfo> threadInfoList = dbManager.queryThreadInfoByUrl(fileInfo.getUrl());

        if(threadInfoList == null || threadInfoList.size() <= 0){ //第一次下载

            Log.d(TAG,"<<== 文件下载 ==>> 第一次下载将文件分成 "+THREAD_COUNT+" 个线程进行下载");

            int block = Math.round(fileInfo.getLength() / THREAD_COUNT);// 每个线程下载的块大小
            ArrayList<FileLoadTask> tasks = new ArrayList<>();
            for (int i = 0; i < THREAD_COUNT; i++) {
                int start = block * i;          // 开始位置
                int end = block * (i + 1) - 1;  // 结束位置

                ThreadInfo threadInfo;
                if (i == (THREAD_COUNT - 1)) {
                    threadInfo = new ThreadInfo(fileInfo.getName(), fileInfo.getUrl(), start, fileInfo.getLength());
                } else {
                    threadInfo = new ThreadInfo(fileInfo.getName(), fileInfo.getUrl(), start, end);
                }

                dbManager.addThreadInfo(threadInfo);
                threadInfo.id = dbManager.getLastRowId(DatabaseHelper.TABLE_NAME_THREADINFO);

                FileLoadTask task = new FileLoadTask(threadInfo, CommonUtil.getFileLoadBasePath(),fileInfo.isAllowAnyNet());

                if(fileInfo.getStatus() == FileLoadInfo.STATUS_LOADING) {
                    new Thread(task).start();
                    tasks.add(task);
                }
            }
            if(fileInfo.getStatus() == FileLoadInfo.STATUS_LOADING) {
                taskMap.put(fileInfo.getUrl(), tasks);
            }

        }else { //已经有下载线程信息

            Log.d(TAG,"<<== 文件下载 ==>> 下载数据库中已存在的线程信息");


            if(fileInfo.getStatus() == FileLoadInfo.STATUS_LOADING) {
                Log.d(TAG,"<<== 文件下载 ==>> ------------------------ 我进入下载循环了1111111");

                List<FileLoadTask> alreadyTasks = taskMap.get(fileInfo.getUrl());
                if(alreadyTasks == null){
                    Log.d(TAG,"<<== 文件下载 ==>> ------------------------ 我进入下载循环了222222");
                    ArrayList<FileLoadTask> tasks = new ArrayList<>();
                    for (int i = 0;i < threadInfoList.size();i++){

                        ThreadInfo threadInfo = threadInfoList.get(i);
                        FileLoadTask task = new FileLoadTask(threadInfo, CommonUtil.getFileLoadBasePath(),fileInfo.isAllowAnyNet());
                        tasks.add(task);
                        new Thread(task).start();
                    }
                    taskMap.put(fileInfo.getUrl(), tasks);
                }
            }
        }
    }

    /**
     * 更新文件下载进度的定时任务
     */
    class ProgressUpdateTimeTask extends TimerTask{

        @Override
        public void run() {
            //Log.d(TAG,"------------------>>>> 没O.5秒定时任务开开启了:"+taskMap.size());

            if(taskMap != null){
                Iterator<Map.Entry<String, List<FileLoadTask>>> entries = taskMap.entrySet().iterator();
                while (entries.hasNext()){
                    Map.Entry<String, List<FileLoadTask>> entry = entries.next();
                    List<FileLoadTask> tasks = entry.getValue();
                    if(tasks.size() > 0){
                        Iterator<FileLoadTask> iterator = tasks.iterator();
                        long finished = 0;
                        String url = null;
                        while (iterator.hasNext()){
                            FileLoadTask task = iterator.next();
                            ThreadInfo threadInfo = task.getThreadInfo();
                            url = threadInfo.url;
                            finished += threadInfo.finished;
                        }

                        if(url != null && gameFileStatusMap != null){
                            FileLoadInfo info = gameFileStatusMap.get(url);
                            if(info != null){

                                Log.d(TAG,"------------------>>>> 每O.5秒我更新下进度"+finished);

                                info.setFinished(finished); //更新下载进度

                                if(finished == info.getLength()){
                                    info.setStatus(FileLoadInfo.STATUS_FINISHED);   //更新下载状态
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 保存文件下载进度的定时任务
     */
    class ProgressSaveTimeTask extends TimerTask{

        @Override
        public void run() {

            if(gameFileStatusMap != null){
                //2.保存文件的下载进度
                for(FileLoadInfo fileLoadInfo : gameFileStatusMap.values()){
                    if(fileLoadInfo.getStatus() == FileLoadInfo.STATUS_LOADING){
                        dbManager.updateFileLoadInfoColumn(fileLoadInfo.getFinished(),FileLoadInfo.STATUS_LOADING,fileLoadInfo.getUrl());
                    }else if(fileLoadInfo.getStatus() == FileLoadInfo.STATUS_PAUSE_TEMP){
                        dbManager.updateFileLoadInfoColumn(fileLoadInfo.getFinished(),FileLoadInfo.STATUS_PAUSE_TEMP,fileLoadInfo.getUrl());
                    }else if(fileLoadInfo.getStatus() == FileLoadInfo.STATUS_FINISHED){
                        dbManager.updateFileLoadInfoColumn(fileLoadInfo.getFinished(),FileLoadInfo.STATUS_FINISHED,fileLoadInfo.getUrl());
                    }
                }
            }

            if(taskMap != null){
                //1.保存线程的下载进度
                Iterator<Map.Entry<String, List<FileLoadTask>>> entries = taskMap.entrySet().iterator();
                while (entries.hasNext()){
                    Map.Entry<String, List<FileLoadTask>> entry = entries.next();
                    List<FileLoadTask> tasks = entry.getValue();
                    if(tasks.size() > 0){

                        Iterator<FileLoadTask> iterator = tasks.iterator();
                        while (iterator.hasNext()){
                            FileLoadTask task = iterator.next();
                            ThreadInfo threadInfo = task.getThreadInfo();
                            if(!task.isStopDownload()){
                                dbManager.updateThreadInfoFinishedColumn(threadInfo);    //更新线程进度
                            }
                        }
                    }
                }
            }
        }
    }

    class FileLoadServiceBinder extends Binder{

        FileLoadService getService(){
            return FileLoadService.this;
        }
    }

}

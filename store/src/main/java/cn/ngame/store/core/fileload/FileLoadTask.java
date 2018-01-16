package cn.ngame.store.core.fileload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

import cn.ngame.store.StoreApplication;
import cn.ngame.store.bean.ThreadInfo;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;

/**
 * 文件下载任务
 * Created by zeng on 2016/5/19.
 */
class FileLoadTask implements Runnable {

    private static final String TAG = FileLoadTask.class.getSimpleName();
    private ThreadInfo threadInfo;
    private String filePath;

    private boolean stopDownload = false;   //停止文件下载的标志
    private boolean isFinished = false;     //是否正确的完成下载任务
    private boolean allowAnyNet = false;    //是否允许任意网络下执行下载任务

    FileLoadTask(ThreadInfo threadInfo, String filePath,boolean allowAnyNet) {
        this.threadInfo = threadInfo;
        this.filePath = filePath;
        this.allowAnyNet = allowAnyNet;
    }

    /**
     * 获取下载进度信息
     */
    public ThreadInfo getThreadInfo(){
        return threadInfo;
    }

    @Override
    public void run() {

        if(threadInfo == null){
            return;
        }

        isFinished = doTask();
        //如果中途出现异常，重新下
        while (!isFinished && !stopDownload){
            try {
                Thread.sleep(1000*5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isFinished = doTask();
        }
    }

    /**
     * 执行下载任务逻辑
     */
    private boolean doTask(){

        boolean isFinished = false;

        RandomAccessFile accessFile = null;
        InputStream is = null;

        try {
            URL url = new URL(threadInfo.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000*10);
            conn.setRequestMethod("GET");
            //设置一般请求属性 范围
            conn.setRequestProperty("Range", "bytes=" + (threadInfo.start + threadInfo.finished) + "-" + threadInfo.end);
            conn.connect(); //执行连接

            int code = conn.getResponseCode();
            if(code == HttpURLConnection.HTTP_PARTIAL){

                is = conn.getInputStream();
                byte[] data = new byte[2048];
                int len;

                File file = new File(filePath + threadInfo.name);
                accessFile = new RandomAccessFile(file, "rwd");
                accessFile.seek(threadInfo.start+threadInfo.finished);

                while ((len = is.read(data)) != -1) {

                    if(StoreApplication.net_status == Constant.NET_STATUS_4G && !allowAnyNet){
                        Log.e(TAG,"----------------------------------------------------------  4G网 不下载 ");

                        return true;
                    }

                    if (!stopDownload) {

                        accessFile.write(data, 0, len); //写文件

                        threadInfo.finished += len;     //累加已经下载的进度

                        //Log.d(TAG, "****************************************下载线程 id: " + Thread.currentThread().getId() + " 正在下载 process：" + threadInfo.finished);
                    } else {
                        Log.d(TAG, "下载线程 id: " + Thread.currentThread().getId() + " 被暂停了 process：" + threadInfo.finished);
                        return true;
                    }
                }
                isFinished = true;

            }else {

                //服务器连接失败，暂停该文件下载
                isFinished = false;
                Log.d(TAG, "文件下载线程:" + Thread.currentThread().getId() + " 下载失败,网络连接错误,暂停该文件下载进程!");
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (SocketTimeoutException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(accessFile != null){
                    accessFile.close();
                }
                if(is != null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,"输入流关闭异常："+e.getMessage());
            }
        }
        return isFinished;
    }

    /**停止下载线程*/
    public void stopDownload() {
        stopDownload = true;
    }

    public boolean isStopDownload(){
        return stopDownload;
    }

    public boolean isFinished(){
        return isFinished;
    }
}

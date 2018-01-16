package cn.ngame.store.bean;

import java.io.Serializable;

/**
 * 被下载文件的信息
 * Created by zeng on 2016/5/19.
 */
public class FileInfo implements Serializable{

    public static final String TAG = FileInfo.class.getSimpleName();

    public static final int TYPE_GAME = 1;
    public static final int TYPE_VIDEO = 2;

    /** 文件在SD卡已经存在，且MD5值相同 */
    public static final int STATUS_FILE_EXIST       = 1;
    /** 文件在SD已经存在，但MD5值不同 */
    public static final int STATUS_FILE_EXIST_OLD   = 2;
    /** 文件在SD不存在 */
    public static final int STATUS_FILE_UN_EXIST    = 3;

    /**未安装*/
    public static final int STATUS_UN_INSTALL       = 4;
    /**已安装*/
    public static final int STATUS_INSTALLED        = 5;
    /**下载中*/
    public static final int STATUS_DOWNLOAD         = 6;
    /**暂停中*/
    public static final int STATUS_PAUSE            = 7;
    /**已下载完成*/
    public static final int STATUS_HAS_DOWNLOAD     = 8;
    /**正在解压缩*/
    public static final int STATUS_UNZIP_ING        = 9;
    /**解压缩完成*/
    public static final int STATUS_HAS_UNZIPED      = 10;

    /** 网络连接超时 */
    public static final int STATUS_NET_TIMEOUT             = 14;
    /** 网络连接失败，服务器响应异常 */
    public static final int STATUS_NET_CONNECT_FAILED      = 15;



    public int id;          //唯一ID，数据库自增长
    public String name;     //文件名
    public String url;      //文件的下载地址
    public String md5;      //文件的哈希值
    public String packageName;  //Apk文件包名 如果文件是APP

    public long length = 0;      //文件的总长度
    public int finished = 0;     //已下载完成的长度
    public int status = 0;       //文件的当前状态

    public String title;       //用来显示的文件标题
    public String previewUrl;  //预览图片地址
    public long serverId;       //文件在服务端数据库中的ID
    /**
     * 文件类型，1.安装文件 2.普通文件  默认是游戏
     */
    public int type = TYPE_GAME;

    public FileInfo() {}

    /*public FileInfo(String name, String url, String md5) {
        this.name = name;
        this.url = url;
        this.md5 = md5;
    }*/

    /*public FileInfo(String name, String url, String md5, String packageName) {
        this.name = name;
        this.url = url;
        this.md5 = md5;
        this.packageName = packageName;
    }*/

    public FileInfo(String name, String url, String md5, String packageName,String title,String previewUrl,long serverId,int type) {
        this.name = name;
        this.url = url;
        this.md5 = md5;
        this.packageName = packageName;

        this.title = title;
        this.previewUrl = previewUrl;
        this.serverId = serverId;
        this.type = type;
    }

    /**
     * 设置文件已完成的进度
     *
     * @param newFinished
     */
    public synchronized void updateFinished(int newFinished) {

        this.finished += newFinished;
    }
}

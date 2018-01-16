package cn.ngame.store.core.fileload;

import java.io.Serializable;

/**
 * 文件下载信息实体类
 * Created by zeng on 2016/10/24.
 */
public class FileLoadInfo implements Serializable{

    public static final int TYPE_ALL = 0;
    public static final int TYPE_GAME = 1;
    public static final int TYPE_VIDEO = 2;

    public static final int STATUS_LOADING      = 1;     //下载中
    public static final int STATUS_PAUSE_TEMP   = 2;   //程序临时暂停
    public static final int STATUS_PAUSE        = 3;     //用户主动暂停
    public static final int STATUS_FINISHED     = 4;     //已下载完成
//    public static final int STATUS_UPDATE       = 5;     //更新 (因为无法更新) 在管理下载更新里面

    private int id;          //唯一ID，数据库自增长
    private String name;     //文件名
    private String url;      //文件的下载地址
    private String md5;      //文件的哈希值
    private String packageName;  //Apk文件包名 如果文件是APP
    private int versionCode;

    private long length = 0;      //文件的总长度
    private long finished = 0;     //已下载完成的长度
    private int status = 0;       //文件的当前状态

    private String title;       //用来显示的文件标题
    private String previewUrl;  //预览图片地址
    private long serverId;       //文件在服务端数据库中的ID

    private int type = TYPE_GAME;   //文件类型，1.安装文件 2.普通文件  默认是游戏

    private boolean allowAnyNet = false;        //是否允许任意网络状态下下载

    public FileLoadInfo() {}

    public FileLoadInfo(String name, String url, String md5, int versionCode, String title, String previewUrl, long serverId, int type) {
        this.name = name;
        this.url = url;
        this.md5 = md5;
        this.versionCode = versionCode;
        this.title = title;
        this.previewUrl = previewUrl;
        this.serverId = serverId;
        this.type = type;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isAllowAnyNet() {
        return allowAnyNet;
    }

    public void setAllowAnyNet(boolean allowAnyNet) {
        this.allowAnyNet = allowAnyNet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileLoadInfo that = (FileLoadInfo) o;

        if (id != that.id) return false;
        if (type != that.type) return false;
        if (!name.equals(that.name)) return false;
        if (!url.equals(that.url)) return false;
        return md5.equals(that.md5);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + md5.hashCode();
        result = 31 * result + type;
        return result;
    }
}

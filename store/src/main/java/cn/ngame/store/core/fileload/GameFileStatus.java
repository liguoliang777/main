package cn.ngame.store.core.fileload;

/**
 * 游戏文件下载状态
 * Created by zeng on 2016/10/25.
 */
public class GameFileStatus {

    /**未安装*/
    public static final int STATE_UN_INSTALL = 0;
    /**已安装*/
    public static final int STATE_HAS_INSTALL = 1;
    /**下载中*/
    public static final int STATE_DOWNLOAD = 2;
    /**暂停中*/
    public static final int STATE_PAUSE = 3;
    /**已下载完成*/
    public static final int STATE_HAS_DOWNLOAD = 4;

    /**已经安装了旧版本*/
    public static final int STATE_HAS_INSTALL_OLD = 5;
    /**解压中*/
    //public static final int STATE_UNZIP_ING = 5;
    /**解压完成*/
    //public static final int STATE_HAS_UNZIP = 6;

    private String url;
    private long finished;
    private long length;

    private int status;

    public GameFileStatus() {
    }

    public GameFileStatus(int status) {
        this.status = status;
    }

    public GameFileStatus(String url, long finished, long length, int status) {
        this.url = url;
        this.finished = finished;
        this.length = length;
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

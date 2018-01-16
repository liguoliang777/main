package cn.ngame.store.bean;

import java.io.Serializable;

/**
 * @author flan
 * @since 2016/5/9
 */
public class VersionInfo implements Serializable{

    public long id;
    public int versionCode;
    public String versionName;
    public String appName;
    public String fileName;
    public long fileSize;
    public String md5;
    public String url;
    public String packageName = "cn.ngame.store";
    public String content;

}

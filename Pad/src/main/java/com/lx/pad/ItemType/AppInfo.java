package com.lx.pad.ItemType;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/11/27.
 */

public class AppInfo {
    private Drawable icon;
    private String appName;
    private String packageName;

    public AppInfo(){
        super();
    }

    public void setAppIcon(Drawable icon){
        this.icon = icon;
    }

    public void setAppName(String appName){
        this.appName = appName;
    }

    public void setPackageName(String packageName){
        this.packageName = packageName;
    }

    public Drawable getAppIcon(){
        return this.icon;
    }

    public String getAppName(){
        return this.appName;
    }

    public String getPackageName(){
        return this.packageName;
    }

    public String toString(){
        return "AppInfo [icon=" + this.icon + ", appName=" + this.appName + ", packageName=" + this.packageName + "]";
    }


}

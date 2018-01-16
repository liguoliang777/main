package cn.ngame.store.ota.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 蓝牙设备信息
 * Created by zeng on 2016/9/19.
 */
public class DeviceInfo implements Parcelable {

    /** 蓝牙名称 --手柄*/
    private String name;
    /** mac地址*/
    private String mac;
    /** 蓝牙设备类型 ble、经典蓝牙*/
    private int   type;

    /** 设备当前版本号 */
    private int currentVersionCode;
    /** 设备当前版本名称*/
    private String currentVersionName;

    /**最新版本号*/
    private int newVersionCode;
    /**最新版本名称*/
    private String newVersionName;

    /**OTA文件下载地址*/
    private String otaUrl;
    /**OTA文件的MD5*/
    private String otaMd5;
    /** 更新内容*/
    private String content;

    public DeviceInfo() {}

    public DeviceInfo(String name, String mac, int type) {
        this.name = name;
        this.mac = mac;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCurrentVersionCode() {
        return currentVersionCode;
    }

    public void setCurrentVersionCode(int currentVersionCode) {
        this.currentVersionCode = currentVersionCode;
    }

    public String getCurrentVersionName() {
        return currentVersionName;
    }

    public void setCurrentVersionName(String currentVersionName) {
        this.currentVersionName = currentVersionName;
    }

    public int getNewVersionCode() {
        return newVersionCode;
    }

    public void setNewVersionCode(int newVersionCode) {
        this.newVersionCode = newVersionCode;
    }

    public String getNewVersionName() {
        return newVersionName;
    }

    public void setNewVersionName(String newVersionName) {
        this.newVersionName = newVersionName;
    }

    public String getOtaUrl() {
        return otaUrl;
    }

    public void setOtaUrl(String otaUrl) {
        this.otaUrl = otaUrl;
    }

    public String getOtaMd5() {
        return otaMd5;
    }

    public void setOtaMd5(String otaMd5) {
        this.otaMd5 = otaMd5;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    protected DeviceInfo(Parcel in) {
        name = in.readString();
        mac = in.readString();
        type = in.readInt();
        currentVersionCode = in.readInt();
        currentVersionName = in.readString();
        newVersionCode = in.readInt();
        newVersionName = in.readString();
        otaUrl = in.readString();
        otaMd5 = in.readString();
        content = in.readString();
    }

    public static final Creator<DeviceInfo> CREATOR = new Creator<DeviceInfo>() {
        @Override
        public DeviceInfo createFromParcel(Parcel in) {
            return new DeviceInfo(in);
        }

        @Override
        public DeviceInfo[] newArray(int size) {
            return new DeviceInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(mac);
        dest.writeInt(type);
        dest.writeInt(currentVersionCode);
        dest.writeString(currentVersionName);
        dest.writeInt(newVersionCode);
        dest.writeString(newVersionName);
        dest.writeString(otaUrl);
        dest.writeString(otaMd5);
        dest.writeString(content);
    }
}

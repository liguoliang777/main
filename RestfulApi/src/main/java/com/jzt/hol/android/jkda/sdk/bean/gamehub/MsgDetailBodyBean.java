package com.jzt.hol.android.jkda.sdk.bean.gamehub;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class MsgDetailBodyBean {
    int id;
    String userCode;
    String deviceOnlyNum;
    int type;
    int postId;
    String appTypeId;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(String appTypeId) {
        this.appTypeId = appTypeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getDeviceOnlyNum() {
        return deviceOnlyNum;
    }

    public void setDeviceOnlyNum(String deviceOnlyNum) {
        this.deviceOnlyNum = deviceOnlyNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

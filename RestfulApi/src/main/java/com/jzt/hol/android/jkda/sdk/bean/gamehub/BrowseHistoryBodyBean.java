package com.jzt.hol.android.jkda.sdk.bean.gamehub;

/**
 * Created by Administrator on 2017/3/10 0010.
 */

public class BrowseHistoryBodyBean {
    String userCode;
    int postId;
    String deviceOnlyNum;
    int appTypeId = 0;

    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getDeviceOnlyNum() {
        return deviceOnlyNum;
    }

    public void setDeviceOnlyNum(String deviceOnlyNum) {
        this.deviceOnlyNum = deviceOnlyNum;
    }

    public int getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(int appTypeId) {
        this.appTypeId = appTypeId;
    }
}

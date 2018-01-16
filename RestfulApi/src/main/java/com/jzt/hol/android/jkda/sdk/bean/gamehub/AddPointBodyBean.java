package com.jzt.hol.android.jkda.sdk.bean.gamehub;

/**
 * Created by Administrator on 2017/3/8 0008.
 */

public class AddPointBodyBean {
    int type;
    String userCode;
    String deviceOnlyNum;
    int postId;
    String appTypeId;

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

    public String getDeviceOnlyNum() {
        return deviceOnlyNum;
    }

    public void setDeviceOnlyNum(String deviceOnlyNum) {
        this.deviceOnlyNum = deviceOnlyNum;
    }

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
}

package com.jzt.hol.android.jkda.sdk.bean.gamehub;

/**
 * Created by Administrator on 2017/3/8 0008.
 */

public class CommentListBodyBean {
    int commentType = 3;
    int code;
    String userCode;
    String deviceOnlyNum;
    int pageIndex;
    int pageSize;

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

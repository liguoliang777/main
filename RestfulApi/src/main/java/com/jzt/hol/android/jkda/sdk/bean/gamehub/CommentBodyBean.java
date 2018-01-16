package com.jzt.hol.android.jkda.sdk.bean.gamehub;

/**
 * Created by Administrator on 2017/3/10 0010.
 */

public class CommentBodyBean {
    String token;
    int commentType = 3;
    String userCode;
    int appTypeId = 0;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public int getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(int appTypeId) {
        this.appTypeId = appTypeId;
    }
}

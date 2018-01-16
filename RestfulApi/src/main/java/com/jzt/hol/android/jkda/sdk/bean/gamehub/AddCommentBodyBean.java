package com.jzt.hol.android.jkda.sdk.bean.gamehub;

/**
 * Created by Administrator on 2017/3/8 0008.
 */

public class AddCommentBodyBean {
    int categoryId = 3;
    int value;
    String token;
    int toMatterCode; //帖子id
    String content;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getToMatterCode() {
        return toMatterCode;
    }

    public void setToMatterCode(int toMatterCode) {
        this.toMatterCode = toMatterCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

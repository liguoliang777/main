package com.jzt.hol.android.jkda.sdk.bean.gamehub;

/**
 * Created by Administrator on 2017/3/4 0004.
 */

public class PostMsgBodyBean {

    int postTagId;
    String postTitle;
    String postContent;
    String postImage;
    String token;
    int appTypeId = 0;
    String voteGame;

    public String getVoteGame() {
        return voteGame;
    }

    public void setVoteGame(String voteGame) {
        this.voteGame = voteGame;
    }

    public int getPostTagId() {
        return postTagId;
    }

    public void setPostTagId(int postTagId) {
        this.postTagId = postTagId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(int appTypeId) {
        this.appTypeId = appTypeId;
    }
}

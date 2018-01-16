package com.jzt.hol.android.jkda.sdk.bean.gamehub;

import java.util.List;

/**
 * Created by Administrator on 2017/3/18 0018.
 */

public class PostSearchListBean {

    /**
     * code : 0
     * msg : null
     * data : [{"id":31,"postTitle":"便宜","postContent":"很好玩"},{"id":30,"postTitle":"唯品会","postContent":"便宜吗"},{"id":29,"postTitle":"聚划算","postContent":"东西很便宜"}]
     * map : null
     * innerResult : null
     */

    private int code;
    private String msg;
    private Object map;
    private Object innerResult;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getMap() {
        return map;
    }

    public void setMap(Object map) {
        this.map = map;
    }

    public Object getInnerResult() {
        return innerResult;
    }

    public void setInnerResult(Object innerResult) {
        this.innerResult = innerResult;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 31
         * postTitle : 便宜
         * postContent : 很好玩
         */

        private int id;
        private String postTitle;
        private String postContent;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
    }
}

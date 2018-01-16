package com.jzt.hol.android.jkda.sdk.bean.gamehub;

import java.util.List;

/**
 * Created by Administrator on 2017/3/8 0008.
 */

public class VoteListBean {

    /**
     * code : 0
     * msg : null
     * data : [{"id":19,"userCode":"UC0000000000069","postPublisher":"高攀测试账号","headPhoto":"http://oss.ngame.cn/upload/userHead/1488614496280.jpg","postTitle":"测试游戏2","postTagId":3,"watchNum":0,"postContent":"还滴滴滴","createTime":1488944013000,"updateTime":null,"appTypeId":0,"commentNum":0,"postImage":"http://oss.ngame.cn/upload/postImage/1488944013253.png,http://oss.ngame.cn/upload/postImage/1488944015429.png","isDelete":null,"isReport":null,"orderNo":null,"isPoint":null,"pointCount":null,"voteCount":0},{"id":18,"userCode":"UC0000000000069","postPublisher":"高攀测试账号","headPhoto":"http://oss.ngame.cn/upload/userHead/1488614496280.jpg","postTitle":"王者荣耀","postTagId":3,"watchNum":0,"postContent":"头诺克的可乐乐克乐克","createTime":1488940806000,"updateTime":null,"appTypeId":0,"commentNum":0,"postImage":null,"isDelete":null,"isReport":null,"orderNo":null,"isPoint":null,"pointCount":null,"voteCount":0},{"id":17,"userCode":"UC0000000000069","postPublisher":"高攀测试账号","headPhoto":"http://oss.ngame.cn/upload/userHead/1488614496280.jpg","postTitle":"高攀专属游戏","postTagId":3,"watchNum":0,"postContent":"XP我改名后明明一下子","createTime":1488940139000,"updateTime":null,"appTypeId":0,"commentNum":0,"postImage":null,"isDelete":null,"isReport":null,"orderNo":null,"isPoint":null,"pointCount":null,"voteCount":0}]
     * map : null
     * innerResult : null
     * page : 1
     * pageSize : 10
     * totals : 3
     */

    private int code;
    private String msg;
    private Object map;
    private Object innerResult;
    private int page;
    private int pageSize;
    private int totals;
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 19
         * userCode : UC0000000000069
         * postPublisher : 高攀测试账号
         * headPhoto : http://oss.ngame.cn/upload/userHead/1488614496280.jpg
         * postTitle : 测试游戏2
         * postTagId : 3
         * watchNum : 0
         * postContent : 还滴滴滴
         * createTime : 1488944013000
         * updateTime : null
         * appTypeId : 0
         * commentNum : 0
         * postImage : http://oss.ngame.cn/upload/postImage/1488944013253.png,http://oss.ngame.cn/upload/postImage/1488944015429.png
         * isDelete : null
         * isReport : null
         * orderNo : null
         * isPoint : null
         * pointCount : null
         * voteCount : 0
         */

        private int id;
        private String userCode;
        private String postPublisher;
        private String headPhoto;
        private String postTitle;
        private int postTagId;
        private int watchNum;
        private String postContent;
        private long createTime;
        private Object updateTime;
        private int appTypeId;
        private int commentNum;
        private String postImage;
        private Object isDelete;
        private Object isReport;
        private Object orderNo;
        private int isPoint;
        private Object pointCount;
        private int voteCount;

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

        public String getPostPublisher() {
            return postPublisher;
        }

        public void setPostPublisher(String postPublisher) {
            this.postPublisher = postPublisher;
        }

        public String getHeadPhoto() {
            return headPhoto;
        }

        public void setHeadPhoto(String headPhoto) {
            this.headPhoto = headPhoto;
        }

        public String getPostTitle() {
            return postTitle;
        }

        public void setPostTitle(String postTitle) {
            this.postTitle = postTitle;
        }

        public int getPostTagId() {
            return postTagId;
        }

        public void setPostTagId(int postTagId) {
            this.postTagId = postTagId;
        }

        public int getWatchNum() {
            return watchNum;
        }

        public void setWatchNum(int watchNum) {
            this.watchNum = watchNum;
        }

        public String getPostContent() {
            return postContent;
        }

        public void setPostContent(String postContent) {
            this.postContent = postContent;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public int getAppTypeId() {
            return appTypeId;
        }

        public void setAppTypeId(int appTypeId) {
            this.appTypeId = appTypeId;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public String getPostImage() {
            return postImage;
        }

        public void setPostImage(String postImage) {
            this.postImage = postImage;
        }

        public Object getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(Object isDelete) {
            this.isDelete = isDelete;
        }

        public Object getIsReport() {
            return isReport;
        }

        public void setIsReport(Object isReport) {
            this.isReport = isReport;
        }

        public Object getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(Object orderNo) {
            this.orderNo = orderNo;
        }

        public int getIsPoint() {
            return isPoint;
        }

        public void setIsPoint(int isPoint) {
            this.isPoint = isPoint;
        }

        public Object getPointCount() {
            return pointCount;
        }

        public void setPointCount(Object pointCount) {
            this.pointCount = pointCount;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(int voteCount) {
            this.voteCount = voteCount;
        }
    }
}

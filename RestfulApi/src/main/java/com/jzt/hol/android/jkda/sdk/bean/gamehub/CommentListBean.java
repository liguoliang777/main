package com.jzt.hol.android.jkda.sdk.bean.gamehub;

import java.util.List;

/**
 * Created by Administrator on 2017/3/8 0008.
 */

public class CommentListBean {

    /**
     * code : 0
     * msg : null
     * data : [{"id":119,"categoryId":1,"chooseItem":null,"chooseItemValue":null,"userCode":"UC0000000000049","nickName":"咯婆婆婆婆哦去在去我婆婆哦POS亲住也破我人陪我","toMatterCode":105,"content":"111","headPhoto":"http://oss.ngame.cn/upload/1467278829480.jpg","ip":null,"deviceType":"iphone6s","createTime":1467601682000,"updateTime":null,"isAnalyse":null,"isDelete":null,"isPoint":0,"pointCount":0},{"id":33,"categoryId":1,"chooseItem":null,"chooseItemValue":null,"userCode":"UC0000000000049","nickName":"15527580825","toMatterCode":105,"content":"巧虎","headPhoto":null,"ip":null,"deviceType":"iphone6s","createTime":1466236783000,"updateTime":null,"isAnalyse":null,"isDelete":null,"isPoint":0,"pointCount":0},{"id":29,"categoryId":1,"chooseItem":null,"chooseItemValue":null,"userCode":"UC0000000000049","nickName":"15527580825","toMatterCode":105,"content":"dkdkdkdkdk","headPhoto":null,"ip":null,"deviceType":"iphone6s","createTime":1466236469000,"updateTime":null,"isAnalyse":null,"isDelete":null,"isPoint":0,"pointCount":0},{"id":28,"categoryId":1,"chooseItem":null,"chooseItemValue":null,"userCode":"UC0000000000049","nickName":"15527580825","toMatterCode":105,"content":"djdjdkdkkddk","headPhoto":null,"ip":null,"deviceType":"iphone6s","createTime":1466236455000,"updateTime":null,"isAnalyse":null,"isDelete":null,"isPoint":0,"pointCount":0},{"id":23,"categoryId":1,"chooseItem":null,"chooseItemValue":null,"userCode":"UC0000000000049","nickName":"15527580825","toMatterCode":105,"content":"咯哦哦","headPhoto":null,"ip":null,"deviceType":"iphone6s","createTime":1466236309000,"updateTime":null,"isAnalyse":null,"isDelete":null,"isPoint":0,"pointCount":0}]
     * map : null
     * innerResult : null
     * page : 1
     * pageSize : 10
     * totals : 5
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
         * id : 119
         * categoryId : 1
         * chooseItem : null
         * chooseItemValue : null
         * userCode : UC0000000000049
         * nickName : 咯婆婆婆婆哦去在去我婆婆哦POS亲住也破我人陪我
         * toMatterCode : 105
         * content : 111
         * headPhoto : http://oss.ngame.cn/upload/1467278829480.jpg
         * ip : null
         * deviceType : iphone6s
         * createTime : 1467601682000
         * updateTime : null
         * isAnalyse : null
         * isDelete : null
         * isPoint : 0
         * pointCount : 0
         */

        private int id;
        private int categoryId;
        private Object chooseItem;
        private Object chooseItemValue;
        private String userCode;
        private String nickName;
        private int toMatterCode;
        private String content;
        private String headPhoto;
        private Object ip;
        private String deviceType;
        private long createTime;
        private Object updateTime;
        private Object isAnalyse;
        private Object isDelete;
        private int isPoint;
        private int pointCount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public Object getChooseItem() {
            return chooseItem;
        }

        public void setChooseItem(Object chooseItem) {
            this.chooseItem = chooseItem;
        }

        public Object getChooseItemValue() {
            return chooseItemValue;
        }

        public void setChooseItemValue(Object chooseItemValue) {
            this.chooseItemValue = chooseItemValue;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
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

        public String getHeadPhoto() {
            return headPhoto;
        }

        public void setHeadPhoto(String headPhoto) {
            this.headPhoto = headPhoto;
        }

        public Object getIp() {
            return ip;
        }

        public void setIp(Object ip) {
            this.ip = ip;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
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

        public Object getIsAnalyse() {
            return isAnalyse;
        }

        public void setIsAnalyse(Object isAnalyse) {
            this.isAnalyse = isAnalyse;
        }

        public Object getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(Object isDelete) {
            this.isDelete = isDelete;
        }

        public int getIsPoint() {
            return isPoint;
        }

        public void setIsPoint(int isPoint) {
            this.isPoint = isPoint;
        }

        public int getPointCount() {
            return pointCount;
        }

        public void setPointCount(int pointCount) {
            this.pointCount = pointCount;
        }
    }
}

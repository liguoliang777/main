package com.jzt.hol.android.jkda.sdk.bean.gamehub;

import java.util.List;

/**
 * Created by Administrator on 2017/3/11 0011.
 */

public class AppCarouselBean {

    /**
     * code : 0
     * msg : null
     * data : [{"id":247,"advLink":"taobao.com","advImageLink":"http://oss.ngame.cn/upload/1468308598053.gif","advName":"APP内部大屏广告","openType":null,"adTypeId":78,"gameId":null,"videoId":null,"labelId":null,"typeId":null,"isDelete":0,"createTime":1487743600000,"updateTime":1487743600000,"orderNo":0,"advDesc":"","type":null}]
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
         * id : 247
         * advLink : taobao.com
         * advImageLink : http://oss.ngame.cn/upload/1468308598053.gif
         * advName : APP内部大屏广告
         * openType : null
         * adTypeId : 78
         * gameId : null
         * videoId : null
         * labelId : null
         * typeId : null
         * isDelete : 0
         * createTime : 1487743600000
         * updateTime : 1487743600000
         * orderNo : 0
         * advDesc :
         * type : null
         */

        private int id;
        private String advLink;
        private String advImageLink;
        private String advName;
        private Object openType;
        private int adTypeId;
        private long gameId;
        private Object videoId;
        private Object labelId;
        private Object typeId;
        private int isDelete;
        private long createTime;
        private long updateTime;
        private int orderNo;
        private String advDesc;
        private Object type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAdvLink() {
            return advLink;
        }

        public void setAdvLink(String advLink) {
            this.advLink = advLink;
        }

        public String getAdvImageLink() {
            return advImageLink;
        }

        public void setAdvImageLink(String advImageLink) {
            this.advImageLink = advImageLink;
        }

        public String getAdvName() {
            return advName;
        }

        public void setAdvName(String advName) {
            this.advName = advName;
        }

        public Object getOpenType() {
            return openType;
        }

        public void setOpenType(Object openType) {
            this.openType = openType;
        }

        public int getAdTypeId() {
            return adTypeId;
        }

        public void setAdTypeId(int adTypeId) {
            this.adTypeId = adTypeId;
        }

        public long getGameId() {
            return gameId;
        }

        public void setGameId(long gameId) {
            this.gameId = gameId;
        }

        public Object getVideoId() {
            return videoId;
        }

        public void setVideoId(Object videoId) {
            this.videoId = videoId;
        }

        public Object getLabelId() {
            return labelId;
        }

        public void setLabelId(Object labelId) {
            this.labelId = labelId;
        }

        public Object getTypeId() {
            return typeId;
        }

        public void setTypeId(Object typeId) {
            this.typeId = typeId;
        }

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(int orderNo) {
            this.orderNo = orderNo;
        }

        public String getAdvDesc() {
            return advDesc;
        }

        public void setAdvDesc(String advDesc) {
            this.advDesc = advDesc;
        }

        public Object getType() {
            return type;
        }

        public void setType(Object type) {
            this.type = type;
        }
    }
}

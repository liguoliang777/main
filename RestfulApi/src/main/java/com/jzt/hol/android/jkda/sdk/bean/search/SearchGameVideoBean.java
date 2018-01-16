package com.jzt.hol.android.jkda.sdk.bean.search;

import java.util.List;

/**
 * Created by gp on 2017/2/22 0022.
 */

public class SearchGameVideoBean {

    /**
     * code : 0
     * msg : hotSearchGameList表示热搜游戏列表，hotSearchVideoList表示热搜视频列表
     * data : {"hotSearchGameList":[{"id":243,"advLink":"","advImageLink":"","advName":"机动骑士","openType":null,"adTypeId":75,"gameId":94,"videoId":null,"labelId":null,"typeId":null,"isDelete":0,"createTime":1487575590000,"updateTime":1487575590000,"orderNo":0,"advDesc":"","type":1},{"id":242,"advLink":"","advImageLink":"","advName":"精忠岳飞","openType":null,"adTypeId":75,"gameId":91,"videoId":null,"labelId":null,"typeId":null,"isDelete":0,"createTime":1487575569000,"updateTime":1487575569000,"orderNo":0,"advDesc":"","type":1}],"hotSearchVideoList":[{"id":245,"advLink":"","advImageLink":"","advName":"霍思燕","openType":null,"adTypeId":76,"gameId":null,"videoId":57,"labelId":null,"typeId":null,"isDelete":0,"createTime":1487575813000,"updateTime":1487575813000,"orderNo":0,"advDesc":"","type":2},{"id":244,"advLink":"","advImageLink":"","advName":"朴信惠","openType":null,"adTypeId":76,"gameId":null,"videoId":55,"labelId":null,"typeId":null,"isDelete":0,"createTime":1487575803000,"updateTime":1487575803000,"orderNo":0,"advDesc":"","type":2}]}
     * map : null
     * innerResult : null
     */

    private int code;
    private String msg;
    private DataBean data;
    private Object map;
    private Object innerResult;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public static class DataBean {
        private List<HotSearchGameListBean> hotSearchGameList;
        private List<HotSearchVideoListBean> hotSearchVideoList;

        public List<HotSearchGameListBean> getHotSearchGameList() {
            return hotSearchGameList;
        }

        public void setHotSearchGameList(List<HotSearchGameListBean> hotSearchGameList) {
            this.hotSearchGameList = hotSearchGameList;
        }

        public List<HotSearchVideoListBean> getHotSearchVideoList() {
            return hotSearchVideoList;
        }

        public void setHotSearchVideoList(List<HotSearchVideoListBean> hotSearchVideoList) {
            this.hotSearchVideoList = hotSearchVideoList;
        }

        public static class HotSearchGameListBean {
            /**
             * id : 243
             * advLink :
             * advImageLink :
             * advName : 机动骑士
             * openType : null
             * adTypeId : 75
             * gameId : 94
             * videoId : null
             * labelId : null
             * typeId : null
             * isDelete : 0
             * createTime : 1487575590000
             * updateTime : 1487575590000
             * orderNo : 0
             * advDesc :
             * type : 1
             */

            private int id;
            private String advLink;
            private String advImageLink;
            private String advName;
            private Object openType;
            private int adTypeId;
            private Long gameId;
            private Object videoId;
            private Object labelId;
            private Object typeId;
            private int isDelete;
            private long createTime;
            private long updateTime;
            private int orderNo;
            private String advDesc;
            private int type;

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

            public Long getGameId() {
                return gameId;
            }

            public void setGameId(Long gameId) {
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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }

        public static class HotSearchVideoListBean {
            /**
             * id : 245
             * advLink :
             * advImageLink :
             * advName : 霍思燕
             * openType : null
             * adTypeId : 76
             * gameId : null
             * videoId : 57
             * labelId : null
             * typeId : null
             * isDelete : 0
             * createTime : 1487575813000
             * updateTime : 1487575813000
             * orderNo : 0
             * advDesc :
             * type : 2
             */

            private int id;
            private String advLink;
            private String advImageLink;
            private String advName;
            private Object openType;
            private int adTypeId;
            private Object gameId;
            private Long videoId;
            private Object labelId;
            private Object typeId;
            private int isDelete;
            private long createTime;
            private long updateTime;
            private int orderNo;
            private String advDesc;
            private int type;

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

            public Object getGameId() {
                return gameId;
            }

            public void setGameId(Object gameId) {
                this.gameId = gameId;
            }

            public Long getVideoId() {
                return videoId;
            }

            public void setVideoId(Long videoId) {
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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}

package com.jzt.hol.android.jkda.sdk.bean.main;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class MallBean {

    /**
     * code : 0
     * msg : null
     * data : [{"id":218,"typeName":"云端适配","orderNo":0,"isDelete":0,"parentId":217,"appTypeId":0,"logoUrl":"http://oss.ngame.cn/upload/1490059023280.png","iosCompany":1},{"id":219,"typeName":"原生适配","orderNo":0,"isDelete":0,"parentId":217,"appTypeId":0,"logoUrl":"http://oss.ngame.cn/upload/1490059033848.png","iosCompany":1}]
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
         * id : 218
         * typeName : 云端适配
         * orderNo : 0
         * isDelete : 0
         * parentId : 217
         * appTypeId : 0
         * logoUrl : http://oss.ngame.cn/upload/1490059023280.png
         * iosCompany : 1
         */

        private Long id;
        private String content;
        private String typeDesc;
        private String mallLink;
        private String orderNo;
        private String mallImg;
        private long updateTime;
        private String title;

        public Long getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        public String getTypeDesc() {
            return typeDesc;
        }

        public String getMallLink() {
            return mallLink;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public String getMallImg() {
            return mallImg;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public String getTitle() {
            return title;
        }
    }
}

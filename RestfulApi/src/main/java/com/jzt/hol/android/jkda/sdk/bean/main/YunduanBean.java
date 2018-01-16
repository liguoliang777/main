package com.jzt.hol.android.jkda.sdk.bean.main;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class YunduanBean {

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
        private String typeName;
        private String typeDesc;
        private int orderNo;
        private int isDelete;
        private int parentId;
        private int appTypeId;
        private String logoUrl;
        private int iosCompany;
        private String text;

        public String getTypeDesc() {
            return typeDesc;
        }

        public void setTypeDesc(String typeDesc) {
            this.typeDesc = typeDesc;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public int getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(int orderNo) {
            this.orderNo = orderNo;
        }

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public int getAppTypeId() {
            return appTypeId;
        }

        public void setAppTypeId(int appTypeId) {
            this.appTypeId = appTypeId;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public int getIosCompany() {
            return iosCompany;
        }

        public void setIosCompany(int iosCompany) {
            this.iosCompany = iosCompany;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}

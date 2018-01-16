package com.jzt.hol.android.jkda.sdk.bean.search;

import java.util.List;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class SearchBean {

    /**
     * code : 0
     * msg : null
     * data : [{"id":null,"keywords":"安卓测试","type":1,"typeId":649,"appTypeId":0,"iosCompany":1}]
     * map : null
     * innerResult : null
     */

    private int code;
    private Object msg;
    private Object map;
    private Object innerResult;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
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
         * id : null
         * keywords : 安卓测试
         * type : 1
         * typeId : 649
         * appTypeId : 0
         * iosCompany : 1
         */

        private Object id;
        private String keywords;
        private int type;
        private long typeId;
        private int appTypeId;
        private int iosCompany;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public long getTypeId() {
            return typeId;
        }

        public void setTypeId(long typeId) {
            this.typeId = typeId;
        }

        public int getAppTypeId() {
            return appTypeId;
        }

        public void setAppTypeId(int appTypeId) {
            this.appTypeId = appTypeId;
        }

        public int getIosCompany() {
            return iosCompany;
        }

        public void setIosCompany(int iosCompany) {
            this.iosCompany = iosCompany;
        }
    }
}

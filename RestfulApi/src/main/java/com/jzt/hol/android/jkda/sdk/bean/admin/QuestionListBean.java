package com.jzt.hol.android.jkda.sdk.bean.admin;

import java.util.List;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class QuestionListBean {
    /**
     * code : 0
     * msg : null
     * data : [{"id":10,"helpTitle":"其他问题","helpContent":"其他问题其他问题","type":4,"isHot":1,"appTypeId":0,"orderNo":0,"resolvedNum":0,"unresolvedNum":0,"isDelete":0,"createTime":1492072044000,"updateTime":1492072086000}]
     * map : null
     * innerResult : null
     * page : 0
     * pageSize : 10
     * totals : 10
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
         * id : 10
         * helpTitle : 其他问题
         * helpContent : 其他问题其他问题
         * type : 4
         * isHot : 1
         * appTypeId : 0
         * orderNo : 0
         * resolvedNum : 0
         * unresolvedNum : 0
         * isDelete : 0
         * createTime : 1492072044000
         * updateTime : 1492072086000
         */
        private String url;
        private int id;
        private String helpTitle;
        private String helpContent;
        private int type;
        private int isHot;
        private int appTypeId;
        private int orderNo;
        private int resolvedNum;
        private int unresolvedNum;
        private int isDelete;
        private long createTime;
        private long updateTime;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getHelpTitle() {
            return helpTitle;
        }

        public void setHelpTitle(String helpTitle) {
            this.helpTitle = helpTitle;
        }

        public String getHelpContent() {
            return helpContent;
        }

        public void setHelpContent(String helpContent) {
            this.helpContent = helpContent;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIsHot() {
            return isHot;
        }

        public void setIsHot(int isHot) {
            this.isHot = isHot;
        }

        public int getAppTypeId() {
            return appTypeId;
        }

        public void setAppTypeId(int appTypeId) {
            this.appTypeId = appTypeId;
        }

        public int getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(int orderNo) {
            this.orderNo = orderNo;
        }

        public int getResolvedNum() {
            return resolvedNum;
        }

        public void setResolvedNum(int resolvedNum) {
            this.resolvedNum = resolvedNum;
        }

        public int getUnresolvedNum() {
            return unresolvedNum;
        }

        public void setUnresolvedNum(int unresolvedNum) {
            this.unresolvedNum = unresolvedNum;
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
    }
}

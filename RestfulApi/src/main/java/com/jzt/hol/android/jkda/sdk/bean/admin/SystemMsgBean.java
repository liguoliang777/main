package com.jzt.hol.android.jkda.sdk.bean.admin;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public class SystemMsgBean implements Serializable{

    /**
     * code : 0
     * msg : null
     * data : [{"id":11,"title":"测试6","content":"测试6","state":2,"appTypeId":0,"isDelete":null,"createTime":1492482355000,"updateTime":1492487098000},{"id":4,"title":"发布过的消息，不能再修改","content":"发布过的消息，不能再修改666","state":2,"appTypeId":0,"isDelete":null,"createTime":1492481923000,"updateTime":1492494209000},{"id":3,"title":"消息提醒：可以勾选多个消息发布999","content":"666666666669999","state":2,"appTypeId":0,"isDelete":null,"createTime":1492481906000,"updateTime":1492482015000},{"id":1,"title":"今日头条","content":"好玩呢","state":2,"appTypeId":0,"isDelete":null,"createTime":1492415641000,"updateTime":1492494223000}]
     * map : null
     * innerResult : null
     * page : 1
     * pageSize : 10
     * totals : 4
     */

    private int code;
    private Object msg;
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

    public static class DataBean implements Serializable{
        /**
         * id : 11
         * title : 测试6
         * content : 测试6
         * state : 2
         * appTypeId : 0
         * isDelete : null
         * createTime : 1492482355000
         * updateTime : 1492487098000
         */
        private String url;
        private int id;
        private String title;
        private String content;
        private int state;
        private int appTypeId;
        private Object isDelete;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getAppTypeId() {
            return appTypeId;
        }

        public void setAppTypeId(int appTypeId) {
            this.appTypeId = appTypeId;
        }

        public Object getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(Object isDelete) {
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

package com.jzt.hol.android.jkda.sdk.bean.gamehub;

/**
 * Created by Administrator on 2017/3/6 0006.
 */

public class GameHubMainBodyBean {
    int appTypeId = 0;
    int pageIndex;
    int pageSize = 10;

    int type;

    public int getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(int appTypeId) {
        this.appTypeId = appTypeId;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

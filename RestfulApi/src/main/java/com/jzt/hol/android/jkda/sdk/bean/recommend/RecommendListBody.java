package com.jzt.hol.android.jkda.sdk.bean.recommend;

/**
 * Created by Administrator on 2017/3/18 0018.
 */

public class RecommendListBody {
    int appTypeId = 0;
    int pageIndex;
    int pageSize;

    public int getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(int appTypeId) {
        this.appTypeId = appTypeId;
    }


    public int getCategoryId() {
        return appTypeId;
    }

    public void setCategoryId(int categoryId) {
        this.appTypeId = categoryId;
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
}

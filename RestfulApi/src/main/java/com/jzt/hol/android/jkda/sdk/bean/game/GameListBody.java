package com.jzt.hol.android.jkda.sdk.bean.game;

/**
 * Created by Administrator on 2017/3/18 0018.
 */

public class GameListBody {
    int gameLabelId = 0;
    int appTypeId = 0;
    int categoryId = 0;
    int pageIndex;
    int pageSize;
    int iosCompany = 1;

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

    public int getGameLabelId() {
        return gameLabelId;
    }

    public void setGameLabelId(int gameLabelId) {
        this.gameLabelId = gameLabelId;
    }

    public int getCategoryId() {
        return appTypeId;
    }

    public void setCategoryId(int categoryId) {
        this.appTypeId = categoryId;
    }

    public int getCategoryId2() {
        return categoryId;
    }

    public void setCategoryId2(int categoryId) {
        this.categoryId = categoryId;
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

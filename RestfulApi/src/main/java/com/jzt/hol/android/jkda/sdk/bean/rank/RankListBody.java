package com.jzt.hol.android.jkda.sdk.bean.rank;

/**
 * Created by Administrator on 2017/3/18 0018.
 */

public class RankListBody {

    /**
     * appTypeId : 0
     * startRecord : 0
     * categoryId : 0
     * records : 10
     */

    private int appTypeId = 0;
    private int startRecord;
    private int parentCategoryId;//第一级
    private int categoryId;//第二级
    private int records;

    public int getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(int parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public int getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(int appTypeId) {
        this.appTypeId = appTypeId;
    }

    public int getStartRecord() {
        return startRecord;
    }

    public void setStartRecord(int startRecord) {
        this.startRecord = startRecord;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }
}

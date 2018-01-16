package com.jzt.hol.android.jkda.sdk.bean.search;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class SearchBodyBean {
    String keywords;
    int appTypeId;
    int iosCompany;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
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

package com.jzt.hol.android.jkda.sdk.bean.gamehub;

/**
 * Created by Administrator on 2017/3/6 0006.
 */

public class ReportPostBodyBean {
    int appTypeId = 0;
    int postId;
    int reportTypeId;

    public int getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(int appTypeId) {
        this.appTypeId = appTypeId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getReportTypeId() {
        return reportTypeId;
    }

    public void setReportTypeId(int reportTypeId) {
        this.reportTypeId = reportTypeId;
    }
}

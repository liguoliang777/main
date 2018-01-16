package com.jzt.hol.android.jkda.sdk.bean.manager;

/**
 * Created by Administrator on 2017/3/18 0018.
 */

public class LikeListBody {

    /**
     * userCode : UC1500625400607
     * appTypeId : 0
     * startRecord : 0
     * records : 10
     */

    private String userCode;
    private int appTypeId = 0;
    private int startRecord;
    private int records;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public int getAppTypeId() {
        return appTypeId;
    }


    public int getStartRecord() {
        return startRecord;
    }

    public void setStartRecord(int startRecord) {
        this.startRecord = startRecord;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "LikeListBody{" +
                "userCode='" + userCode + '\'' +
                ", appTypeId=" + appTypeId +
                ", startRecord=" + startRecord +
                ", records=" + records +
                '}';
    }
}

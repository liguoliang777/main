package com.jzt.hol.android.jkda.sdk.bean.admin;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class AdminGameUpdateBody {
    int appTypeId = 0;
    int type = 0;
    int pageIndex = 0;
    int pageSize = 10;
    String gameIdAndVisionCodeStr;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(int appTypeId) {
        this.appTypeId = appTypeId;
    }

    public String getGameIdAndVisionCodeStr() {
        return gameIdAndVisionCodeStr;
    }

    public void setGameIdAndVisionCodeStr(String gameIdAndVisionCodeStr) {
        this.gameIdAndVisionCodeStr = gameIdAndVisionCodeStr;
    }
}

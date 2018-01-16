package cn.ngame.store.local.model;

import java.util.List;

/**
 * 保存今天，一周内，更早的观看记录
 * Created by zeng on 2016/10/22.
 */
public class WatchRecordGroup {

    private List<WatchRecord> weekPlayRecord;
    private List<WatchRecord> earlierPlayRecord;

    public List<WatchRecord> getWeekPlayRecord() {
        return weekPlayRecord;
    }

    public void setWeekPlayRecord(List<WatchRecord> weekPlayRecord) {
        this.weekPlayRecord = weekPlayRecord;
    }

    public List<WatchRecord> getEarlierPlayRecord() {
        return earlierPlayRecord;
    }

    public void setEarlierPlayRecord(List<WatchRecord> earlierPlayRecord) {
        this.earlierPlayRecord = earlierPlayRecord;
    }
}

package cn.ngame.store.local.view;

import java.util.List;

import cn.ngame.store.local.model.WatchRecord;

/**
 * 视图层 接口
 * Created by zeng on 2016/10/11.
 */
public interface IWatchRecordView {

    /**
     * 显示观看记录
     * @param weekRecord        一周内观看记录
     * @param otherRecord       更早以前观看记录
     */
    void showWatchRecord(List<WatchRecord> weekRecord,List<WatchRecord> otherRecord);

}

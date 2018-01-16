package cn.ngame.store.local.model;

import java.util.List;

/**
 * 本地管理，视频观看记录model层接口
 * Created by zeng on 2016/10/11.
 */
public interface IWatchRecordModel {

    /**
     * 同步本地与服务器上的记录
     */
    void synchronizeWatchRecord();

    /**
     * 添加观看记录
     * @param wh        观看的视频信息
     */
    void addWatchRecord(WatchRecord wh);

    /**
     * 删除观看记录
     * @param token     用户登录令牌，未登录用户为null
     * @param userCode  登录用户的ID
     * @param deleteData 观看记录
     */
    void deleteRecord(String token,String userCode,List<WatchRecord> deleteData);

    /**
     * 查询观看记录
     * @param token         用户登录token
     * @param userCode      用户id
     * @return  观看记录
     */
    WatchRecordGroup queryWatchRecord(String token,String userCode);

    /**
     * 查询一周内的观看记录
     */
    List<WatchRecord> queryWeekWatchRecord();

    /**
     * 查询更早以前的观看记录
     */
    List<WatchRecord> queryOtherWatchRecord();

}

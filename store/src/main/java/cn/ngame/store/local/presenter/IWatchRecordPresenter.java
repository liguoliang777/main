package cn.ngame.store.local.presenter;

import java.util.List;

import cn.ngame.store.local.model.WatchRecord;

/**
 * 本地管理，视频观看记录控制层接口
 * Created by zeng on 2016/10/11.
 */
public interface IWatchRecordPresenter {

    /**
     * 显示观看记录
     * @param token        用户登录令牌，未登录用户为null
     * @param userCode      用于ID
     */
    void showWatchRecord(String token,String userCode);

    /**
     * 删除观看记录
     * @param token        用户登录令牌，未登录用户为null
     * @param userCode      用于ID
     * @param deleteData    观看记录的数据库ID
     */
    void deleteRecord(final String token, final String userCode,List<WatchRecord> deleteData);
}

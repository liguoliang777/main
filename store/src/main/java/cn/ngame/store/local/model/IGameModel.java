package cn.ngame.store.local.model;

import java.util.List;

import cn.ngame.store.bean.FileInfo;

/**
 * 获取游戏下载记录的接口
 * Created by zeng on 2016/10/11.
 */
public interface IGameModel {

    /***
     * 获取下载中和已下载的游戏
     * @return
     */
    List<FileInfo> queryLoadGame();
}

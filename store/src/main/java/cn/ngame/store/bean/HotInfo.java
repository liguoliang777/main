package cn.ngame.store.bean;

import java.io.Serializable;

/**
 * 热门推荐信息
 * Created by zeng on 2016/6/15.
 */
public class HotInfo implements Serializable {

    public long id;
    public String advLink;
    /**
     * 预览图片地址
     */
    public String advImageLink;
    /***/
    public String advName;
    /***/
    public String openType;
    /***/
    public String adTypeId;
    /** 推荐短语 */
    public String advDesc;
    /**
     * 游戏ID
     */
    public long gameId;
    /**
     * 视频ID
     */
    public long videoId;
    /**
     * 1.游戏，2.视频
     */
    public int type;
}

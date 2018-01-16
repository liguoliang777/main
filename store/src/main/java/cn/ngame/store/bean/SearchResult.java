package cn.ngame.store.bean;

import java.io.Serializable;

/**
 * 聚合搜索结果
 * Created by zeng on 2016/6/23.
 */
public class SearchResult implements Serializable{

    public static final int TYPE_GAME = 1;
    public static final int TYPE_VIDEO = 2;

    public long id;
    public String name;
    public String summary;
    public String imgUrl;
    /**总评分*/
    public float percentage;
    /**下载量*/
    public int downloadCount;
    /**文件大小*/
    public long fileSize;

    /**
     * 1.游戏
     * 2.视频
     */
    public int type;

}
